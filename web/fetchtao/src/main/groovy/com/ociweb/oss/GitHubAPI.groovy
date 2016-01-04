package com.ociweb.oss

/**
 * Created by phil on 1/4/16.
 */
class GitHubAPI {
    static String authToken

    static void initAuthToken (String filename) {
        println "GitHbuAPI initAuthToken called, filename = " + filename + " token null? " + (authToken == null)
        if (authToken == null) {
            authToken = new File(filename).text
        }
    }
}
