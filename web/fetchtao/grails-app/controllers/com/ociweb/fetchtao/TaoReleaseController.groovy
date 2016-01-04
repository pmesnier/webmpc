package com.ociweb.fetchtao

import com.ociweb.oss.Product

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)
class TaoReleaseController {
    static productNameProperty = "OCI TAO - for reals"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def stashRelease

    def index() {
        def prod = Product.list().find {
            if (it.name.equals ("OCI TAO"))
                return it
        }
        respond TaoRelease.list(), model: [pkgCount: TaoRelease.count(), title:prod.title, logo:prod.logo]
    }

    def show (TaoRelease rel)
    {
        println "TaoReleaseController rel = " + rel
        stashRelease = rel
        respond rel, model: [title: rel.product.title, logo: rel.product.logo];
    }

    def save () {
        render "save called"
    }

    def taoDownloadLink (TaoRelease rel)
    {
        if (rel == null) rel = stashRelease
        println "rel version = " + rel.rlsVersion + " lastPatch = " + rel.lastPatch + " lastTarget = " + rel.lastTarget
        def pkg = rel.target(params)

        render template:'downloadLinkTao', model: [urlstr: pkg.targetName, md5str: pkg.md5sum]
    }

}
