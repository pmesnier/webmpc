package com.ociweb.oss

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional
import grails.converters.*

@Transactional(readOnly = false)
class TaoReleaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true


    def index() {
        def prod = Product.list().find {
            if (it.name.equals ("OCI TAO"))
                return it
        }

        respond TaoRelease.list(), model: [ product: prod,
                                            plList: TaoLegacyService.patchList,
                                            conList: TaoLegacyService.contentList,
                                            cmpList: TaoLegacyService.compressList ]
    }

    def show (TaoRelease rel)
    {
      respond rel, model: [ product: rel.product ]
    }

    def ajaxGetPatchLevel = {
        def rel = TaoRelease.get(params.id)
        if (rel)
            render TaoLegacyService.patchlevelFor(rel) as JSON
    }

    def populatePatchLevel (TaoRelease rel) {
        println "Populate Patch Level ->"
        params.each { println " " + it }
        def pllist = TaoLegacyService.patchlevelFor (rel)
        render template: 'selectPatchLevel', model: [plList: pllist]
    }

    def populateContent (TaoRelease rel)
    {
        println "Populate Content ->"
        params.each { println " " + it }
        def contlist = TaoLegacyService.contentFor (rel, params)
        render template: 'selectContent', model: [contList: contlist]
    }

    def populateCompress (TaoRelease rel)
    {
        println "Populate Compress ->"
        params.each { println " " + it }
        def cmplist = TaoLegacyService.compressFor (rel, params)
        render template: 'selectCompress', model: [cmpList: cmplist]
    }

    def taoDownloadLink (TaoRelease rel)
    {
        println "Populate tao Download Link ->"
        params.each { println " " + it }
        def pkg = TaoLegacyService.target(rel, params)

        render template:'downloadLinkTao', model: [pkg: pkg, basePath: rel.basePath]
    }

}
