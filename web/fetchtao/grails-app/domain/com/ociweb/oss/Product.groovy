package com.ociweb.oss
/**
 * Created by phil on 12/18/15.
 */
class Product {
    static hasMany = [releases: Release]
    SortedSet<Release> releases

    String name
    String descstr
    String descref
    String source
    String rlsurl
    String docs
    String license
    String faq
    String logo
    String title
    String updateAction
    String dynamicDivId

    static constraints = {
        descstr maxSize: 1000, nullable: true
        descref nullable: true
        source nullable:true
        rlsurl nullable:true
        docs nullable:true
        license maxSize:10000, nullable:true
        faq nullable:true
        logo nullable:true
        title nullable:true
        updateAction nullable:true
        dynamicDivId nullable:true

    }
}
