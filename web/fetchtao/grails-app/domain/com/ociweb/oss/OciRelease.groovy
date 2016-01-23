package com.ociweb.oss
/**
 * Created by phil on 12/9/15.
 */

public class OciRelease extends Release {

    static hasMany = [legacy:OciAsset]
    Map legacy = [:]

    String rlsVersion
    String basePath
    String readmePath
    String patchReadmePath
    String relNotesPath
    String vcs
    String repourl

    int lastTarget

    static constraints = {
        readmePath nullable:true
        patchReadmePath nullable:true
        relNotesPath nullable:true

        vcs nullable:true
        repourl nullable:true
    }

}
