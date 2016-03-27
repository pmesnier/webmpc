package com.ociweb.oss
/**
 * Created by phil on 12/9/15.
 */

public class OciRelease extends Release {
    static ociService

    static hasMany = [legacy:OciAsset, plList:OciSelectorInfo]
    SortedSet<OciSelectorInfo> plList

    String rlsVersion
    String basePath
    String readmePath
    String patchReadmePath
    String relNotesPath
    String vcs
    String repourl
    String customizer
    int major
    int minor
    int micro
    char ext

    static constraints = {
        readmePath nullable:true
        patchReadmePath nullable:true
        relNotesPath nullable:true

        vcs nullable:true
        repourl nullable:true
        customizer nullable:true
    }

    @Override
    int compareTo(Release o) {
        return -1 * ociService.compareRelease (this, o)
    }
}
