package com.ociweb.fetchtao

import com.ociweb.oss.Product

/**
 * Created by phil on 12/15/15.
 */
class TaoProduct extends Product {

    String rootServerName

    String rootName () {
        "http://" + rootServerName
    }
}
