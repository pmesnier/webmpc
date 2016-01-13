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
            if (it.name.equals("OCI TAO"))
                return it
        }

        respond TaoRelease.list(), model: [product: prod,
                                           plList : TaoLegacyService.patchList,
                                           conList: TaoLegacyService.contentList,
                                           cmpList: TaoLegacyService.compressList
        ]
    }

    def show(TaoRelease rel) {
        respond rel, model: [product: rel.product]
    }

    def updateTaoSelector(TaoRelease rel) {
        def model = [:]
        int val = 0
        def nvlist = TaoLegacyService.patchlevelFor(rel)
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

            nvlist = TaoLegacyService.contentFor(rel, params)
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
                nvlist = TaoLegacyService.compressFor(rel, params)
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
                              pkg     : TaoLegacyService.target(rel, params),
                              basePath: rel.basePath]
                }
            } else {
                model << [cmpList: TaoLegacyService.compressList]
            }
        } else {
            model << [conList: TaoLegacyService.contentList,
                      cmpList: TaoLegacyService.compressList]
        }

        render template: 'taoLegacyOptions', model: model
    }

    def runMPC(TaoRelease rel) {

    }
}
