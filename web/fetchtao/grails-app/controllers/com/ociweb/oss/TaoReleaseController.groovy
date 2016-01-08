package com.ociweb.oss

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)
class TaoReleaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true


    def index() {
        def prod = Product.list().find {
            if (it.name.equals ("OCI TAO"))
                return it
        }
        respond TaoRelease.list(), model: [ product: prod ]
    }

    def show (TaoRelease rel)
    {
        respond rel, model: [ product: rel.product ]
    }

    def taoDownloadLink (TaoRelease rel)
    {

        def pkg = TaoLegacyService.target(rel, params)

        render template:'downloadLinkTao', model: [pkg: pkg, basePath: rel.basePath]
    }

}
