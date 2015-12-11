package com.ociweb.fetchtao

/**
 * Created by phil on 12/11/15.
 */

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = false)
class TaoPackageController {

    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def productName
    def taoDownloadService

    def productNameProperty = "OCI TAO"
    def index() {
        respond TaoPackage.list()
    }

}
