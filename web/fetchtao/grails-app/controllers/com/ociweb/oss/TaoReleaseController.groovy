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
        def pllist = TaoLegacyService.patchlevelFor (rel)
        render template: 'selectPatchLevel', model: [plList: pllist]
    }

    def populateContent (TaoRelease rel)
    {
        def contlist = TaoLegacyService.contentFor (rel, params)
        render template: 'selectContent', model: [contList: contlist]
    }

    def populateContent2 (TaoRelease rel)
    {
        String tname = params.template ? params.template : 'taoLegacyOptions'
        String changeFuncBody = "\${remoteFunction(controller: 'TaoRelease', " +
                                "action: 'populateCompress', " +
                                "params: '\\'id=\\'+ escape(rlsVersion.value) + \\'&patchLevel=\\' + escape(patchLevel.value) + \\'&content=\\' + escape(this.value)', " +
                                "update: 'selectCompress')}"
        def contlist = TaoLegacyService.contentFor (rel, params)
        def optionDescriptor = [header:"Choose contents for " + TaoLegacyService.patchList.find { it.value.equals (params.patchLevel) }?.name,
                                selectlabel: "content",
                                nvlist: contlist,
                                deflabel: "['" + "':'-Select Content-']",
                                changeFunc: changeFuncBody]
        def optionMap = [content: optionDescriptor]
//        def keyOrder = ["rlsver", "plevel", "content", "compress", "download"]
        def keyOrder = ["content"]
        def model = [keys : keyOrder, options: optionMap]
        render template: tname, model: model
    }

    def populateCompress (TaoRelease rel)
    {
        def cmplist = TaoLegacyService.compressFor (rel, params)
        render template: 'selectCompress', model: [cmpList: cmplist]
    }

    def taoDownloadLink (TaoRelease rel)
    {
        def pkg = TaoLegacyService.target(rel, params)
        render template:'downloadLinkTao', model: [pkg: pkg, basePath: rel.basePath]
    }

}
