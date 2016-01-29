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
        def nvlist = []
        rel.plList.each { p->
            nvlist.add([name: message(code: "ociPatchBase.${p.patchKind}", args: [p.patchNum, p.testNum]), value: ociService.targetKey(p)])
        }

        model << [plSelector: nvlist]
        OciSelectorInfo osi

        if (nvlist.size() == 1) {
            osi = rel.plList[0]
            params << [patchLevel: nvlist[0].value]
        }
        else {
            if (params.patchLevel) {
                osi = rel.plList.find { p ->
                    String testval = ociService.targetKey(p)
                    params.patchLevel.equals(testval)
                }
                if (osi == null) {
                    params.remove('patchLevel')
                }
            }
        }

        if (params.patchLevel) {
            model << [plsel: params.patchLevel]

            nvlist = []
            osi.contentKind.each { c ->
                String n = message (code : "ociContent.${c}")
                nvlist.add ([name: n, value: "C:${c}"])
            }

            model << [conList: nvlist]
            if (nvlist.size() == 1) {
                params << [content: nvlist[0].value]
            }
            else if (params.content) {
                if (!osi.contentKind.find {
                        String testVal = "C:${it}"
                        params.content.equals (testVal)}) {
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
            nvlist = []
            nvlist.add ([name: "-Select Content-", value: ""])
            model << [conList: nvlist]
        }

        render template: tmpl, model: model
    }

    def runMPC(OciRelease rel) {

    }
}
