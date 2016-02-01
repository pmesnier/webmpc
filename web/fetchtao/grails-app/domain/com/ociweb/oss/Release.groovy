package com.ociweb.oss

import org.hibernate.mapping.Map

abstract class Release implements Comparable<Release> {

    static belongsTo = [product: Product]

}
