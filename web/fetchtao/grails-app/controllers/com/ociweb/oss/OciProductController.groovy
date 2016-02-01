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
        nvlist.add ([name: "-Select Content-", value: ""])
        OciRelease rel = prod.latest
        def defPlList = rel.plList.collect() { p->
            [name: message(code: "ociPatchBase.${p.patchKind}",
                    args: [p.patchNum, p.testNum]),
             value: ociService.targetKey(p)]}
        params.patchLevel = defPlList[0].value
        OciSelectorInfo osi = rel.plList[0]
        def defConList = osi.contentKind.collect { c ->
            [name: message (code : "ociContent.${c}"), value: "C:${c}"]}
        params.content = defConList[0].value
        def pkg = ociService.target(rel, params)
        respond prod.releases,  model: [product: prod,
                                        latest: prod.latest,
                                        plSelector: defPlList,
                                        plsel: defPlList[0].value,
                                        conList: defConList,
                                        consel: defConList[0].value,
                                        pkg: pkg]
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

        if (params.patchLevel) {
            osi = rel.plList.find { p ->
                params.patchLevel.equals(ociService.targetKey(p))
            }
        }

        if (osi == null) {
            osi = rel.plList[0]
            params.patchLevel = model.plSelector[0].value
        }

        model << [plsel: params.patchLevel]
        model << [conList: osi.contentKind.collect { c ->
            [name: message (code : "ociContent.${c}"), value: "C:${c}"]
        } ]

        if (!params.content || !osi.contentKind.contains(params.content.substring(2) as OciContent)) {
            params.content = model.conList[0].value
        }

        def pkg = ociService.target(rel, params)
        model << [consel: params.content, pkg: pkg ]

        render template: tmpl, model: model
    }

    def runMPC(OciRelease rel) {

    }
}
