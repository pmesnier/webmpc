package com.ociweb.oss

import org.hibernate.mapping.Map

/**
 * Created by phil on 12/18/15.
 */
class Release {

    static belongsTo = [product: Product]
    def product

}
