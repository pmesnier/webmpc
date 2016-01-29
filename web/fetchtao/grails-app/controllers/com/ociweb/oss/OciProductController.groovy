package com.ociweb.oss

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional
class OciProductController {

    static allowedMethods = [] //save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def ociService

    def show(OciProduct prod) {
        def nvlist = []
        nvlist.add ([name: "Select a release version first", value: ""])
        respond prod.releases,  model: [product: prod,
                                        plSelector: nvlist,
                                        conList: nvlist]
    }

    def showMpc(OciRelease rel) {
        respond rel
    }

    def viewRelNotes(OciRelease rel) {
        String rnurl = rel?.relNotesPath
        if (rnurl != null)
            redirect (url:rnurl)

    }

    def updateOciSelector(OciRelease rel) {
        def model = [product: rel.product]
        def tmpl = rel.product.dynamicDivId

        model << [plSelector: rel.plList.collect() { p->
            [name: message(code: "ociPatchBase.${p.patchKind}", args: [p.patchNum, p.testNum]), value: ociService.targetKey(p)]
        } ]
        OciSelectorInfo osi = null

        if (rel.plList.size() == 1) {
            osi = rel.plList[0]
            params << [patchLevel: model.plSelector[0].value]
        }
        else {
            if (params.patchLevel) {
                osi = rel.plList.find { p ->
                    params.patchLevel.equals(ociService.targetKey(p))
                }
                if (osi == null) {
                    params.remove('patchLevel')
                }
            }
        }

        if (params.patchLevel) {
            model << [plsel: params.patchLevel]

            model << [conList: osi.contentKind.collect { c ->
                [name: message (code : "ociContent.${c}"), value: "C:${c}"]
            } ]

            if (osi.contentKind.size() == 1) {
                params << [content: model.conList[0].value]
            }
            else if (params.content) {
                if (!osi.contentKind.contains (params.content.substring (2) as OciContent)) {
                    //find { params.content.equals ("C:${it}") }) {
                    params.remove('content')
                }
            }

            if (params.content) {
                def pkg = ociService.target(rel, params)
                model << [consel: params.content,
                          pkg   : pkg ]
            }
        }
        else {
            model << [conList: [[name: "-Select Content-", value: ""]] ]
        }

        render template: tmpl, model: model
    }

    def runMPC(OciRelease rel) {

    }
}
