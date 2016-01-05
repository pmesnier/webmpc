package com.ociweb.oss

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)
class TaoReleaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def stashRelease

    def index() {
        def prod = Product.list().find {
            if (it.name.equals ("OCI TAO"))
                return it
        }
        respond TaoRelease.list(), model: [pkgCount: TaoRelease.count(), prodid: prod.id, title:prod.title, logo:prod.logo]
    }

    def show (TaoRelease rel)
    {
        println "TaoReleaseController rel = " + rel
        stashRelease = rel
        respond rel, model: [title: rel.product.title, logo: rel.product.logo];
    }

    def showLicense (TaoProduct rel)
    {
        Product prod = rel.product
        redirect (url: prod.license)
    }

    def showDocs (TaoProduct prod)
    {
        redirect (url: prod.docs)
    }

    def showFAQ (TaoProduct prod)
    {
        redirect (url: prod.faq)
    }
    def save () {
        render "save called"
    }

    def taoDownloadLink (TaoRelease rel)
    {
        if (rel == null) rel = stashRelease
        println "rel version = " + rel.rlsVersion + " lastPatch = " + rel.lastPatch + " lastTarget = " + rel.lastTarget
        def pkg = rel.target(params)

        render template:'downloadLinkTao', model: [pkg: pkg, basePath: rel.basePath]
    }

}
