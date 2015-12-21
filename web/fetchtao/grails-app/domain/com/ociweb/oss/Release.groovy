package com.ociweb.oss

import org.hibernate.mapping.Map

/**
 * Created by phil on 12/18/15.
 */
class Release {
    List packages

    static belongsTo = [product: Product]
    static hasMany = [packages: Package]

    def initPackages () {

    }
}
