package com.ociweb.oss

/**
 * Created by phil on 1/2/16.
 */
class GitHubRelease extends Release {
    static hasMany = [asset : GitHubAsset]
    Map assets = [:]

    String name
    String tarball_url
    String zipball_url
    String body

    static constraints = {
        name nullable:true
        tarball_url nullable:true
        zipball_url nullable:true
        body nullable:true, maxSize:20000
    }

}
