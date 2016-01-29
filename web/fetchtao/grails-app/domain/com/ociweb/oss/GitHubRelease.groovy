package com.ociweb.oss

/**
 * Created by phil on 1/2/16.
 */
class GitHubRelease extends Release {
    static hasMany = [assets : GitHubAsset]

    String name
    String tarball_url
    String zipball_url
    String body
    String created_at

    static transients = ['createdAtDate']

    static constraints = {
        name nullable:true
        tarball_url nullable:true
        zipball_url nullable:true
        body nullable:true, maxSize:20000
        created_at nullable:true
    }

    String getCreatedAtDate () {
        if (created_at == null)
            return "n/a"
        Date.parse("yyyy-mm-dd'T'hh:mm:ss", created_at).toString()
    }

    @Override
    int compareTo(Release o) { // reverse order
        return -1 * name.compareTo(o.name)
    }

}
