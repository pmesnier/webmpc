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


    static String authToken

    static void initAuthToken(String token) {
        println "GitHubService initAuthToken called, token = " + token + " token null? " + (authToken == null)
        if (authToken == null) {
            authToken = token
        }
    }


    static def openGitHubPage(GitHubProduct prod, String link, InfoType info) {
        String urlstr = (link.startsWith("http")) ? link : "https://api.github.com/repos/" + prod.githubowner + "/" + prod.githubrepo + link
        println "openGitHubPage " + urlstr
        URL url = new URL(urlstr)
        HttpURLConnection con = (HttpURLConnection) url.openConnection()
        con.addRequestProperty("Authorization", authToken)
        println "    adding prop Authorization = " + authToken
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

    static def fetchRevInfo_i(GitHubProduct prod, String link, InfoType info) {
        List reflist = []

        HttpURLConnection con = openGitHubPage(prod, link, info)
        int code = con.getResponseCode()
        println "fetchRevInfo_i link = " + link + " got code " + code
        switch (code) {
            case HttpURLConnection.HTTP_NOT_MODIFIED:
                return null
            case HttpURLConnection.HTTP_OK:
                if (info == InfoType.INFO_TAG) {
                    prod.tagsLastMod = con.getHeaderField("Last-Modified")
                } else if (info == InfoType.INFO_RELEASE) {
                    prod.relsLastMod = con.getHeaderField("Last-Modified")
                    if (prod.relsLastMod == null) {
                        prod.relsLastMod = con.getHeaderField("Date")
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
        def slurp = new JsonSlurper()
        slurp.parse(con.getContent()).each {
            reflist << [name: it.name, tarball_url: it.tarball_url, zipball_url: it.zipball_url]
        }
        if (nextPage.length() > 0) {
            fetchRevInfo_i(prod, nextPage, info).each {
                reflist << it
            }
        }
        return reflist
    }

    static void fetchLicense_i(GitHubProduct prod)
    {
        HttpURLConnection con = openGitHubPage (prod, "/contents/" + prod.githublicense, InfoType.INFO_LICENSE)

        int code = con.getResponseCode()
        if (code == HttpURLConnection.HTTP_OK) {
            prod.licLastMod = con.getHeaderField("Last-Modified")
            def slurp = new JsonSlurper()
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

    static sourceURL (GitHubProduct ghp) {
        if (ghp.source == null || ghp.source.length() == 0) {
            ghp.source = "https://github.com/" + ghp.githubowner + "/" + ghp.githubrepo
        }
        return ghp.source
    }

    static fetchReleaseInfo (GitHubProduct ghp) {
        List reflist = fetchRevInfo_i (ghp, "/releases", InfoType.INFO_RELEASE)
        if (reflist != null && reflist.size() == 0) {
            reflist = fetchRevInfo_i(ghp, "/tags", InfoType.INFO_TAG)
        }
        if (reflist != null) {
            reflist.each { rlsdef ->
                def rls =  new GitHubRelease (rlsdef)
                ghp.addToReleases (rls)
            }

        }

        return (ghp.releases == null || ghp.releases.size() == 0) ?
             [[name: "none", tarball_url: "", zipball_url: ""]]
             : ghp.releases
    }

    static fetchLicense (GitHubProduct ghp) {
        fetchLicense_i (ghp)
        return ghp.license
    }

    static targetLink (GitHubProduct ghp, def params) {
        println "TargetLink params.release = " + params.release + " bundle = " + params.bundle

        def link = ghp.releases.find ({ if (it.toString().equals(params.release)) return it })
        if (link != null)
            return (params.bundle.equals ("zip")) ? link.zipball_url : link.tarball_url
        return null
    }


}
