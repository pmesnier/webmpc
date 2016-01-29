package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/5/16.
 */
class GitHubService {
    static enum InfoType {
        INFO_TAG,
        INFO_RELEASE,
        INFO_DOCS,
        INFO_LICENSE,
        INFO_NONE
    }

    def slurp = new JsonSlurper()

    String authToken

    void initAuthToken(String token) {
        println "GitHubService initAuthToken called, token = " + token + " token null? " + (authToken == null)
        if (authToken == null) {
            authToken = token
        }
    }


    def openGitHubPage(GitHubProduct prod, String link, InfoType info) {
        String urlstr = (link.startsWith("http")) ? link : "https://api.github.com/repos/" + prod.githubowner + "/" + prod.githubrepo + link
        println "openGitHubPage " + urlstr
        URL url = new URL(urlstr)
        HttpURLConnection con = (HttpURLConnection) url.openConnection()
        if (authToken) {
            con.addRequestProperty("Authorization", authToken)
            println "    adding prop Authorization = " + authToken
        }
        String lastMod
        switch (info) {
            case InfoType.INFO_TAG:
                lastMod = prod.tagsLastMod
                break
            case InfoType.INFO_RELEASE:
                lastMod = prod.relsLastMod
                break
            case InfoType.INFO_LICENSE:
                lastMod = prod.licLastMod
                break
            default:
                break
        }
        if (lastMod != null && lastMod.length() > 0) {
            con.addRequestProperty("If-Modified-Since", lastMod)
            println "    adding prop If-Modified-Since = " + lastMod
        }
        con.connect()
        return con
    }

    def fetchLatest_i (GitHubProduct prod) {
        HttpURLConnection con = openGitHubPage(prod, "/releases/latest", InfoType.INFO_RELEASE)
        int code = con.getResponseCode()
        println "fetch latest returnd code = ${code} "
        if (code == HttpURLConnection.HTTP_OK) {
            def latestProps = slurp.parse (con.getContent())
            println "latest name = ${latestProps.name} created at ${latestProps.created_at}"
            prod.relsLastMod = latestProps.created_at
        }

        return code

    }

    def fetchRevInfo_i(GitHubProduct prod, String link, InfoType info) {
        List reflist = []
        def lastMod
        HttpURLConnection con = openGitHubPage(prod, link, info)
        int code = con.getResponseCode()
        println "fetchRevInfo_i link = " + link + " got code " + code
        switch (code) {
            case HttpURLConnection.HTTP_NOT_MODIFIED:
                return null
            case HttpURLConnection.HTTP_OK:
                lastMod = con.getHeaderField("Last-Modified")
                if (info == InfoType.INFO_RELEASE) {
                    if (lastMod == null) {
                        lastMod = con.getHeaderField("Date")
                    }
                }
                break
            default:
                return reflist
        }

        String nextPage = ""
        String val = con.getHeaderField("Link")

        if (val != null && val.contains("next")) {
            def nextChunk = val.split(",").find { it.contains("rel=\"next\"") }
            int head = nextChunk.indexOf('<') + 1;
            int tail = nextChunk.lastIndexOf('>');
            nextPage = nextChunk.substring(head, tail)
        }

        slurp.parse(con.getContent()).each {
            reflist << it
        }
        if (nextPage.length() > 0) {
            fetchRevInfo_i(prod, nextPage, info).each {
                reflist << it
            }
        }
        if (info == InfoType.INFO_RELEASE) {
            prod.relsLastMod = lastMod
        } else if (info == InfoType.INFO_TAG) {
            prod.tagsLastMod = lastMod
        }
        return reflist
    }

    void fetchLicense_i(GitHubProduct prod)
    {
        HttpURLConnection con = openGitHubPage (prod, "/contents/" + prod.githublicense, InfoType.INFO_LICENSE)

        int code = con.getResponseCode()
        if (code == HttpURLConnection.HTTP_OK) {
            prod.licLastMod = con.getHeaderField("Last-Modified")
            def licobj = slurp.parse (con.getContent())
            if (licobj.encoding.equals ("base64")) {
                byte[] decoded = licobj.content.decodeBase64()
                prod.license = new String(decoded)
            }
            else {
                println "license string using encoding \"" + licobj.encoding + "\""
            }
        }
    }

    def sourceURL (GitHubProduct ghp) {
        if (ghp.source == null || ghp.source.length() == 0) {
            ghp.source = "https://github.com/" + ghp.githubowner + "/" + ghp.githubrepo
        }
        return ghp.source
    }

    def fetchReleaseInfo (GitHubProduct ghp) {
        int res = fetchLatest_i(ghp)
        if (res == HttpURLConnection.HTTP_NOT_MODIFIED) {
            return ghp.releases
        }

        List reflist = (res == HttpURLConnection.HTTP_OK) ?
             fetchRevInfo_i(ghp, "/releases", InfoType.INFO_RELEASE) :
                fetchRevInfo_i(ghp, "/tags", InfoType.INFO_TAG)

        if (reflist != null) {
            ghp.releases.clear()
            reflist.each { rlsdef ->
                def ghr = new GitHubRelease(rlsdef)
                ghp.addToReleases(ghr)
            }
        }

        return (ghp.releases == null || ghp.releases.size() == 0) ?
             [[name: "none", tarball_url: "", zipball_url: "", created_at: "", body: ""]]
             : ghp.releases
    }

    def fetchLicense (GitHubProduct ghp) {
        fetchLicense_i (ghp)
        return ghp.license
    }

    def truncate (String full_url, boolean minimum) {
        int pos = full_url.lastIndexOf('/')
        if (!minimum) {
            pos = full_url.lastIndexOf('/', pos-1)
            pos = full_url.lastIndexOf('/', pos-1)
        }
        return full_url.substring(pos + 1)
    }

    def target (GitHubRelease ghr, def params) {
        List result = []

        if (ghr.assets) {
            ghr.assets.each { ass ->
                result.add([targetName: ass.browser_download_url,
                            displayName: truncate(ass.browser_download_url, true),
                            fileSize: ass.fmtFileSize, timeStamp: ghr.createdAtDate])
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
