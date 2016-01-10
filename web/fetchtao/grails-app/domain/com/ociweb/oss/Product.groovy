package com.ociweb.oss

import groovy.json.JsonSlurper
/**
 * Created by phil on 12/18/15.
 */
class Product {
    static hasMany = [releases: Release]
    List releases

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

//    static transients = ["descstr", "license"]

    static constraints = {
//        descstr bindable:true
        descstr length: 1000
        descstr nullable: true
        descref nullable: true
        source nullable:true
        rlsurl nullable:true
        docs nullable:true
//        license bindable:true
        license length:10000
        license nullable:true
        faq nullable:true
        license bindable:true
        logo nullable:true
        title nullable:true
    }

    def initRelease (params) {
        if ((descstr == null || descstr.length() == 0) && (descref != null && descref.length() > 0)) {
            descstr = getClass().getClassLoader().getResourceAsStream(descref).text
            }
    }

    def sourceURL () {
         return source
    }

    def fetchReleaseInfo () {
        return releases
    }

    def fetchLicense () {
        return license
    }

    def fetchDoc () {
        return docs
    }

    def targetLink (params) {
        return ""
    }
}
