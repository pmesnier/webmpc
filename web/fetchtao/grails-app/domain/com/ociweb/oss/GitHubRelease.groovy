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
        created_at ?
                Date.parse("yyyy-mm-dd'T'hh:mm:ss", created_at).toString() :
                "n/a"

    }

    @Override
    int compareTo(Release o) { // reverse order
        if (created_at && ((GitHubRelease)o).created_at) {
            Date me = Date.parse("yyyy-mm-dd'T'hh:mm:ss", created_at)
            Date other = Date.parse("yyyy-mm-dd'T'hh:mm:ss", ((GitHubRelease)o).created_at)
            -1 * me.compareTo(other)
        } else
            -1 * name.compareTo(o.name)
    }

}
