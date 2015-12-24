package com.ociweb.oss

import groovy.json.JsonSlurper
/**
 * Created by phil on 12/18/15.
 */
class Product {
    static hasMany = [releases: Release]
    List releases

    String name
    String descstr
    String descref
    String source
    String rlsurl
    String docs
    String license
    String faq
    String logo
    String title
    String githubowner
    String githubrepo
    String githublicense
    String githubdocs
    String tagsLastMod
    String relsLastMod
    String licLastMod
    String licenseText
    List rlss =[]
    List githubtags = []

    static transients = [ "rlss", "githubtags"]
    static constraints = {
        descstr nullable:true
        descstr maxSize:1000
        descref nullable: true
        source nullable:true
        rlsurl nullable:true
        docs nullable:true
        license nullable:true
        faq nullable:true
        logo nullable:true
        title nullable:true
        githubowner nullable:true
        githubrepo nullable:true
        githublicense nullable:true
        githubdocs nullable:true
        tagsLastMod nullable:true
        relsLastMod nullable:true
        licLastMod nullable:true
        licenseText nullable:true
        rlss nullable:true
        githubtags nullable:true

    }

    def initRelease (params) {
        if ((descstr == null || descstr.length() == 0) && (descref != null && descref.length() > 0)) {
            descstr = getClass().getClassLoader().getResourceAsStream(descref).text
        }
    }

    def openGitHubPage (String link, String lastMod) {
        String urlstr = (link.startsWith ("http") ) ? link : "https://api.github.com/repos/" + githubowner + "/" + githubrepo + link
        URL url = new URL (urlstr)
        HttpURLConnection con = (HttpURLConnection)url.openConnection()
        con.addRequestProperty("Authorization", "")
        if (lastMod != null && lastMod.length() > 0) {
            con.addRequestProperty("If-Modified-Since", lastMod)
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
        if (code == HttpURLConnection.HTTP_NOT_MODIFIED)
            return null
        if (code == HttpURLConnection.HTTP_OK && lastMod != null) {
            lastMod = con.getHeaderField("Last-Modified")
            if (link.equals ("/tags")) {
                tagsLastMod = lastMod
            }
            else {
                relsLastMod = lastMod
            }
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
            reflist << [ name: it.name, tgzurl: it.tarball_url, zipurl: it.zipball_url]
        }
        if (nextPage.length() > 0) {
            fetchRevInfo_i(nextPage).each {
                reflist << it
            }
        }
        return reflist
    }

    def fetchReleaseInfo () {
        if (githubowner != null) {
            List reflist = fetchRevInfo_i ("/releases")
            if (reflist != null)
                rlss = reflist
        }
        return rlss
    }

    def fetchTagInfo () {
        if (githubowner != null) {
            List reflist = fetchRevInfo_i("/tags")
            if (reflist != null)
                githubtags = reflist
        }
        return githubtags
    }

    def fetchLicense () {
        if (githubowner != null) {
            openGitHubPage ("/contents/" + githublicense, licLastMod)

            HttpURLConnection con = openGitHubPage (link, lastMod)
            int code = con.getResponseCode()
            if (code == HttpURLConnection.HTTP_NOT_MODIFIED)
                return license
            if (code == HttpURLConnection.HTTP_OK && licLastMod != null) {
                licLastMod = con.getHeaderField("Last-Modified")
            }

            def slurp = new JsonSlurper()
            def licobj = slurp.parse (con.getContent())
            if (licobj.encoding.equals ("base64")) {
                byte[] decoded = licobj.content.decodeBase64()
                return new String(decoded)
            }
            else {
                println "license string using encoding \"" + licobj.encoding + "\""
            }
        }
        return license
    }
}
