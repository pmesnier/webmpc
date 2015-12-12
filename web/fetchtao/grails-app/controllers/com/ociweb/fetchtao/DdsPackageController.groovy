package com.ociweb.fetchtao

/**
 * Created by phil on 12/12/15.
 */

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = false)

class DdsPackageController {
    static allowedMethods = [save: "POST"] //, update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def productName
    def taoDownloadService

    def productNameProperty = "OpenDDS"
    def index() {
        respond DdsPackage.list()
    }

    def getLastPatch (String base) {
        DdsPackage.each ( {
            if (it.baseVersion.equals(base)) {
                respond it.lastPatch;
            }
        });
    }



}
