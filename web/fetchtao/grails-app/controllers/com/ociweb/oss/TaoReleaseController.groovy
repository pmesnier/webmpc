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
        def pllist = ["nothing", "defined", "yet"]

        respond TaoRelease.list(), model: [ product: prod, plList: pllist ]
    }

    def show (TaoRelease rel)
    {
      respond rel, model: [ product: rel.product ]
    }

    def populatePatchLevel (TaoRelease rel) {
        def pllist = TaoLegacyService.patchlevelFor (rel)
        render template: 'selectPatchLevel', model: [rls: rel, lastPatch: rel.lastPatch, plList: pllist]
    }

    def populateContent (TaoRelease rel)
    {
        def contlist = TaoLegacyService.contentFor (rel, params)
        render template: 'selectContent', model: [rls: rel, contList: contlist]
    }

    def populateFmt (TaoRelease rel)
    {
        def fmtlist = TaoLegacyService.compressFor (rel, params)
        render template: 'selectCompress', model: [rls: rel, fmtList: fmtlist]
    }

    def taoDownloadLink (TaoRelease rel)
    {
       // TaoRelease rel = new TaoRelease(params.rlsVersion)
        def pkg = TaoLegacyService.target(rel, params)

        render template:'downloadLinkTao', model: [pkg: pkg, basePath: rel.basePath]
    }

}
