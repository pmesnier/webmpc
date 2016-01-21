package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 12/9/15.
 */

public class TaoRelease extends Release {

    static hasMany = [legacy:TaoLegacyPackage, active:TaoActivePackage]
    Map legacy = [:]
    Map active = [:]

    String rlsVersion
    String basePath
    String readmePath
    String patchReadmePath
    String relNotesPath

    int lastTarget

    static constraints = {
        readmePath nullable:true
        patchReadmePath nullable:true
        relNotesPath nullable:true
    }

}
