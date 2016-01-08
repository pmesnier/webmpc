package com.ociweb.oss

/**
 * Created by phil on 12/19/15.
 */

class TaoLegacyPackage {

    static belongsTo = [release: TaoRelease]
    TaoRelease release

    String targetName
    String md5sum
    int content
    int patchLevel
    String timestamp
    int filesize

    static constraints = {
        release nullable:true
        targetName nullable:true
        md5sum nullable:true
        timestamp nullable: true
    }

 }
