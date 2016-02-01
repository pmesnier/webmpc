package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/5/16.
 */
class GitHubService {
    def formatter = new Formatter()
    def slurp = new JsonSlurper()
    String authToken

    void initAuthToken(String token) {
        if (authToken == null) {
            authToken = token
        }
    }


    def openGitHubPage(Map args) {
        if (args.perpage == null)
            args.perpage = ""
        String urlstr = (args.link.startsWith("http")) ? args.link :
                "https://api.github.com/repos/${args.githubowner}/${args.githubrepo}${args.link}${args.perpage}"
        //println "openGitHubPage " + urlstr
        URL url = new URL(urlstr)
        HttpURLConnection con = (HttpURLConnection) url.openConnection()
        if (authToken) {
            con.addRequestProperty("Authorization", authToken)
            //println "    adding prop Authorization = " + authToken
        }
       if (args.lastmod) {
            con.addRequestProperty("If-Modified-Since", args.lastmod)
            //println "    adding prop If-Modified-Since = " + args.lastmod
        }
        con.connect()
        return con
    }

    def fetchLatest_i (Map args ) { //GitHubProduct prod) {
        HttpURLConnection con = openGitHubPage(args)
        int code = con.getResponseCode()
        //println "fetch latest returnd code = ${code} "
        if (code == HttpURLConnection.HTTP_OK) {
            def latestProps = slurp.parse (con.getContent())
            //println "latest name = ${latestProps.name} created at ${latestProps.created_at}"
            args.lastmod = latestProps.created_at //prod.lastMod.releases = latestProps.created_at
        }

        return code

    }


    void fetchTagCommitInfo_i (Map args) {
        HttpURLConnection con = openGitHubPage(args)
        int code = con.getResponseCode()
        //println "fetchTagCommitInfo_i link = ${args.link} got code ${code}"
        if (code == HttpURLConnection.HTTP_OK) {
            def data;
            data = slurp.parse (con.getContent());
            args.created_at = data?.commit?.committer?.date
            if (!args.created_at)
                args.created_at = data?.commit?.author?.date
            //println "Found created_at date = ${args.created_at}"
        }
    }

    def fetchRevInfo_i(Map args) {
        HttpURLConnection con = openGitHubPage(args)
        int code = con.getResponseCode()
        //println "fetchRevInfo_i link = ${args.link} got code ${code}"
        switch (code) {
            case HttpURLConnection.HTTP_NOT_MODIFIED:
                return null
            case HttpURLConnection.HTTP_OK:
                if (args.lastmod_i)
                    break
                if ((args.lastmod_i = con.getHeaderField("Last-Modified")) == null)
                    args.lastmod_i = con.getHeaderField("Date")
                break
            default:
                return []
        }

        String nextPage = ""
        String val = con.getHeaderField("Link")

        if (val != null && val.contains("next")) {
            def nextChunk = val.split(",").find { it.contains("rel=\"next\"") }
            int head = nextChunk.indexOf('<') + 1;
            int tail = nextChunk.lastIndexOf('>');
            args.link = nextChunk.substring(head, tail)
            //println "Next page = " + args.link
        }
        else {
            args.remove ("link")
        }

        List reflist = []
        reflist.addAll( slurp.parse(con.getContent()))
        if (args.link) {
            def extra = fetchRevInfo_i(args)
            if (extra)
                reflist.addAll(extra)
        }
        return reflist
    }

    def sourceURL (GitHubProduct ghp) {
        if (ghp.source == null || ghp.source.length() == 0) {
            ghp.source = "https://github.com/${ghp.githubowner}/${ghp.githubrepo}"
        }
        return ghp.source
    }

    def fetchReleaseInfo (GitHubProduct ghp) {
        Map args = [githubowner:ghp.githubowner,
                    githubrepo: ghp.githubrepo,
                    link: "/releases/latest",
                    lastmod: ghp.lastMod.releases]
        String lmkey = 'releases'
        int res = fetchLatest_i(args)
        if (res == HttpURLConnection.HTTP_NOT_MODIFIED) {
            return ghp.releases
        }
        if (res == HttpURLConnection.HTTP_OK) {
            args.link = "/releases"
        }
        else {
            args.link = "/tags"
            args.lastmod = ghp.lastMod.tags
            lmkey = 'tags'
        }
        args.perpage="?per_page=100"

        List reflist = fetchRevInfo_i(args)
        ghp.lastMod."${lmkey}" = args.lastmod_i
        if (reflist != null) {
            ghp.releases.clear()
            reflist.each { rlsdef ->
                def ghr = new GitHubRelease(rlsdef)
                if (rlsdef?.commit) {
                    args.link = rlsdef.commit?.url
                    if (args.link)
                        fetchTagCommitInfo_i (args)
                    ghr.created_at = args.created_at
                }
                ghp.addToReleases(ghr)

            }
        }

        return (ghp.releases == null || ghp.releases.size() == 0) ?
             [[name: "none", tarball_url: "", zipball_url: "", created_at: "", body: ""]]
             : ghp.releases
    }

    def getLicenseText (ProductService pi, GitHubProduct ghp) {
        Map args = [githubowner:ghp.githubowner,
                    githubrepo: ghp.githubrepo,
                    link: "/contents/${ghp.githublicense}",
                    lastmod: ghp.lastMod.license]

        HttpURLConnection con = openGitHubPage (args)

        int code = con.getResponseCode()
        if (code == HttpURLConnection.HTTP_OK) {
            ghp.addToLastMod([license:con.getHeaderField("Last-Modified")])

            def licobj = slurp.parse (con.getContent())
            if (licobj.encoding.equals ("base64")) {
                byte[] decoded = licobj.content.decodeBase64()
                pi.licenseCache."${prod}" = new String(decoded)
            }
            else {
                println "license string using encoding \"" + licobj.encoding + "\""
            }
        }
        return pi.licenseCache."${prod}"
    }

    def truncate (String full_url, boolean minimum) {
        int pos = full_url.lastIndexOf('/')
        if (!minimum) {
            pos = full_url.lastIndexOf('/', pos-1)
            pos = full_url.lastIndexOf('/', pos-1)
        }
        return full_url.substring(pos + 1)
    }

    def target (GitHubRelease ghr) {
        List result = []

        if (ghr.assets) {
            ghr.assets.each { ass ->
                result.add([targetName: ass.browser_download_url,
                            displayName: truncate(ass.browser_download_url, true),
                            fileSize: formatter.fmtFileSize (ass.size), timeStamp: ghr.createdAtDate])
            }
        }
        else {
            result.addAll ([[targetName: ghr.zipball_url, displayName: truncate(ghr.zipball_url,false),
                             fileSize: "n/a", timeStamp: ghr.createdAtDate],
                            [targetName: ghr.tarball_url, displayName: truncate(ghr.tarball_url,false),
                             fileSize: "n/a", timeStamp: ghr.createdAtDate]])
        }

        return result
    }

}
