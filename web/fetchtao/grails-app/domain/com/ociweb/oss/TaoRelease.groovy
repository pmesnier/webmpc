package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 12/9/15.
 */

//@Resource(uri="/downloadtao")
public class TaoRelease extends Release {

    static hasMany = [legacy:TaoLegacyPackage]
    static defaultPackage = new TaoLegacyPackage ([targetName: "Desired package not available",
                                                   md5sum    :"",
                                                   patchLevel:0,
                                                   filesize: 0,
                                                   timestamp:"no date"])

    Map legacy = [:]

    String rlsVersion
    String basePath
    String patchesPath
    String readmePath
    String patchReadmePath
    String relNotesPath

    int lastPatch
    boolean patchsrc
    int lastTarget

    static constraints = {
        readmePath nullable:true
        patchReadmePath nullable:true
        relNotesPath nullable:true
    }

}
