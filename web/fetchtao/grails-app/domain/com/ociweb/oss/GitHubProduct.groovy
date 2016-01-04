package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/2/16.
 */
class GitHubProduct extends Product {

    String githubowner
    String githubrepo
    String githublicense
    String githubdocs
    String tagsLastMod
    String relsLastMod
    String licLastMod

    static constraints = {
        githublicense nullable:true
        githubdocs nullable:true
        tagsLastMod nullable:true
        relsLastMod nullable:true
        licLastMod nullable:true
    }

    def sourceURL () {
        if (source == null || source.length() == 0) {
            source = "https://github.com/" + githubowner + "/" + githubrepo
        }
        return source
    }

    def openGitHubPage (String link, String lastMod) {
        String urlstr = (link.startsWith ("http") ) ? link : "https://api.github.com/repos/" + githubowner + "/" + githubrepo + link
        println "openGitHubPage " + urlstr
        URL url = new URL (urlstr)
        HttpURLConnection con = (HttpURLConnection)url.openConnection()
        con.addRequestProperty("Authorization", GitHubAPI.authToken)
        println "    adding prop Authorization = " + GitHubAPI.authToken
        if (lastMod != null && lastMod.length() > 0) {
            con.addRequestProperty("If-Modified-Since", lastMod)
            println "    adding prop If-Modified-Since = " + lastMod
        }
        con.connect()
        return con
    }

    def fetchRevInfo_i (String link) {
        List reflist = []
        String lastMod = null
        if (link.equals("/tags")) {
            lastMod = tagsLastMod == null ? "" : tagsLastMod
        }
        else if (link.equals("/releases")) {
            lastMod = relsLastMod == null ? "" : relsLastMod
        }

        HttpURLConnection con = openGitHubPage (link, lastMod)
        int code = con.getResponseCode()
        println "fetchRevInfo_i link = " + link + " got code " + code
        switch (code) {
        case HttpURLConnection.HTTP_NOT_MODIFIED:
            return null
        case HttpURLConnection.HTTP_OK:
            if (link.equals ("/tags")) {
                tagsLastMod = con.getHeaderField("Last-Modified")
            }
            else if (link.equals ("/releases")) {
                relsLastMod = con.getHeaderField("Last-Modified")
                if (relsLastMod == null) {
                    relsLastMod = con.getHeaderField("Date")
                }
            }
            break
        default:
            return reflist
        }

        String nextPage = ""
        String val = con.getHeaderField("Link")

        if ( val != null && val.contains ("next")) {
            def nextChunk = val.split (",").find {it.contains("rel=\"next\"")}
            int head = nextChunk.indexOf('<') + 1;
            int tail = nextChunk.lastIndexOf('>');
            nextPage = nextChunk.substring (head, tail)
        }
        def slurp = new JsonSlurper()
        slurp.parse (con.getContent()).each {
            reflist << [ name: it.name, tarball_url: it.tarball_url, zipball_url: it.zipball_url]
        }
        if (nextPage.length() > 0) {
            fetchRevInfo_i(nextPage).each {
                reflist << it
            }
        }
        return reflist
    }

    def fetchReleaseInfo () {
        List reflist = fetchRevInfo_i ("/releases")
        if (reflist != null && reflist.size() == 0) {
            reflist = fetchRevInfo_i("/tags")
        }
        if (reflist != null) {
            reflist.each { rlsdef ->
                def rls =  new GitHubRelease (rlsdef)
                addToReleases (rls)
            }

        }
        return releases
    }

    def fetchLicense () {

        HttpURLConnection con = openGitHubPage ("/contents/" + githublicense, licLastMod)

        int code = con.getResponseCode()
        if (code == HttpURLConnection.HTTP_NOT_MODIFIED)
            return license
        if (code == HttpURLConnection.HTTP_OK) {
            licLastMod = con.getHeaderField("Last-Modified")
        } else {
            return license
        }

        def slurp = new JsonSlurper()
        def licobj = slurp.parse (con.getContent())
        if (licobj.encoding.equals ("base64")) {
            byte[] decoded = licobj.content.decodeBase64()
            license = new String(decoded)
        }
        else {
            println "license string using encoding \"" + licobj.encoding + "\""
        }
        return license
    }

    def targetLink (params) {
        println "TargetLink params.release = " + params.release + " format = " + params.format

        def link = releases.find ({ if (it.toString().equals(params.release)) return it })
        if (link != null)
            return (params.format.equals ("zip")) ? link.zipball_url : link.tarball_url
        return null
    }

}
