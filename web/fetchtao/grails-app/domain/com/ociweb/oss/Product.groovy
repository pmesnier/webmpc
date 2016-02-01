package com.ociweb.oss
/**
 * Created by phil on 12/18/15.
 */
class Product {
    static productService
    static hasMany = [releases: Release, lastMod: String]
    SortedSet<Release> releases
    Map lastMod

    String name
    String label
    String source
    String rlsurl
    String descstr
    String logo
    String title
    String updateAction
    String dynamicDivId

    static constraints = {
        label nullable:true
        source nullable:true
        rlsurl nullable:true
        descstr maxSize: 1000, nullable:true
        logo nullable:true
        title nullable:true
        updateAction nullable:true
        dynamicDivId nullable:true
    }
    static transients = ['latest','licenseText']
    Release getLatest ()
    {
        releases?.getAt(0)
    }

    String getLicenseText () {
        productService.getLicenseText (this)
    }

}
