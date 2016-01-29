package com.ociweb.oss

import java.text.DecimalFormat

/**
 * Created by phil on 12/19/15.
 */

class OciAsset {

    static belongsTo = [release: OciRelease]

    String key
    String filePath
    String md5
    String sha1sum
    String sha256sum
    int content
    int patchLevel
    String fileDate
    long fileSize

    static constraints = {
        release nullable:true
        filePath nullable:true
        md5 nullable:true
        sha1sum nullable:true
        sha256sum nullable:true
        fileDate nullable: true
    }

    static transients = ['fmtFileSize', 'shortUrlName']

    String getShortUrlName () {
        int ndx = filePath?.lastIndexOf('/')
        return (ndx > 0) ? filePath.substring(ndx+1) : filePath
    }

    String getFmtFileSize () {
        def fmt = new DecimalFormat("###.##")
        def klimit=1024
        if (fileSize > (klimit * klimit * klimit)) {
            fmt.format(fileSize / (klimit * klimit * klimit)) + "G"
        }
        else if (fileSize > (klimit * klimit)) {
            fmt.format(fileSize / (klimit * klimit)) + "M"
        }
        else if (fileSize > (klimit)) {
            fmt.format (fileSize / (klimit)) + "K"
        }
        else {
            fileSize as String
        }
    }

 }
