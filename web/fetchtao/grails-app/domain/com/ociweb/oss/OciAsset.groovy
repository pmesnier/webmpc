package com.ociweb.oss

/**
 * Created by phil on 12/19/15.
 */

class OciAsset {

    static belongsTo = [release: OciRelease]
    OciRelease release

    String targetName
    String md5sum
    String sha1sum
    String sha256sum
    int content
    int patchLevel
    String timestamp
    int filesize

    static constraints = {
        release nullable:true
        targetName nullable:true
        md5sum nullable:true
        sha1sum nullable:true
        sha256sum nullable:true
        timestamp nullable: true
    }

 }
