package com.ociweb.fetchtao

import com.ociweb.fetchdds.DdsRelease

/**
 * Created by phil on 12/12/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)

class DdsReleaseController {
    static allowedMethods = [save: "POST"] //, update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def productName
    def taoDownloadService

    def productNameProperty = "OpenDDS"
    def index() {
        respond DdsRelease.list()
    }

    def getLastPatch (String base) {
        DdsRelease.each ( {
            if (it.baseVersion.equals(base)) {
                respond it.lastPatch;
            }
        });
    }



}
