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

}
