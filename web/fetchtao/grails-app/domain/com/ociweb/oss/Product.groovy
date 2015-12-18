package com.ociweb.oss

/**
 * Created by phil on 12/18/15.
 */
class Product {
    static hasMany = [releases: Release]
    List releases

    String name
}
