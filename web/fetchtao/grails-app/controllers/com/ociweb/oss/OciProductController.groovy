package com.ociweb.oss

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)
class OciProductController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def show(OciProduct prod) {
        println "OCIProductController.show, prod = " + prod.name + " and has " + prod.releases.size() + " entries "
        respond prod.releases,  model: [product: prod,
                                        updateAction: prod.updateAction,
                                        dynamicDivId: prod.dynamicDivId,
                                        ociReleases: prod.releases,
                                        plList : OciAssetService.patchList,
                                        conList: OciAssetService.contentList,
                                        cmpList: OciAssetService.compressList
        ]
    }

    def showMpc(OciRelease rel) {
        respond rel
    }

    def showReleaseNotes(OciRelease rel) {
        String rnurl = rel.relNotesPath
        if (rnurl != null)
            redirect (url:rnurl)
            redirect (url:rnurl)
    }

    def updateTaoSelector(OciRelease rel) {
        def model = [:]
        int val = 0
        def nvlist = OciAssetService.patchlevelFor(rel)
        model << [plList: nvlist]
        if (params.patchLevel) {
            val = params.patchLevel as int
            if (nvlist.find { it.value as int == val } == null) {
                params.remove('patchLevel')
                val = 0
            }
        }
        if (val == 0 && nvlist.size() == 1) {
            val = nvlist[0].value as int
            params << [patchLevel: val]
        }

        if (val > 0) {
            model << [plsel: val]

            nvlist = OciAssetService.contentFor(rel, params)
            model << [conList: nvlist]
            if (params.content) {
                val = params.content as int
                if (nvlist.find { it.value as int == val } == null) {
                    params.remove('content')
                    val = 0
                }
            } else
                val = 0

            if (val == 0 && nvlist.size() == 1) {
                val = nvlist[0].value as int
                params << [content: val]
            }

            if (val > 0) {
                model << [consel: val]
                nvlist = OciAssetService.compressFor(rel, params)
                model << [cmpList: nvlist]
                if (params.compress) {
                    val = params.compress as int
                    if (nvlist.find { it.value as int == val } == null) {
                        params.remove('compress')
                        val = 0
                    }
                } else
                    val = 0

                if (val == 0 && nvlist.size() == 1) {
                    val = nvlist[0].value as int
                    params << [compress: val]
                }

                if (val > 0) {
                    model << [cmpsel  : val,
                              pkg     : OciAssetService.target(rel, params),
                              basePath: rel.basePath]
                }
            } else {
                model << [cmpList: OciAssetService.compressList]
            }
        } else {
            model << [conList: OciAssetService.contentList,
                      cmpList: OciAssetService.compressList]
        }

        render template: 'taoLegacyOptions', model: model
    }

    def runMPC(OciRelease rel) {

    }
}
