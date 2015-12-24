package com.ociweb.oss

/**
 * Created by phil on 12/25/15.
 */
class ProductController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def index() {
        respond Product.list(), model: [pkgCount: Product.count()]
    }

    def show (Product prod) {

        respond prod, model: [name: prod.name, logo: prod.logo, title: prod.title]
    }

    def showSource (Product prod) {
        if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        respond prod, model: [name: prod.name, logo: prod.logo, title: prod.title]
    }

    def showReleases (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.rlsurl)
        else if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        def rlist = prod.fetchReleaseInfo()
        if (rlist == null || rlist.size() == 0)
            rlist = prod.fetchTagInfo()
        if (rlist == null || rlist.size() == 0) {
            rlist = []
            rlist << [name : "none", tgzurl : "", zipurl : ""]
        }
        println "list size = " + rlist.size()
        respond prod, model: [name: prod.name, rlist: rlist, logo: prod.logo, title: prod.title]
    }

    def showLicense (Product prod) {
        String lictext = prod.fetchLicense()
        if (lictext == "" && prod.license != null && prod.license.length() > 0)
            redirect (url:prod.license)
        respond prod, model: [name: prod.name, license: lictext, logo: prod.logo, title: prod.title]
    }

    def showDocs (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.docs)
        respond prod, model: [name: prod.name, logo: prod.logo, title: prod.title]
    }

    def showFAQ (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.faq)

        respond prod, model: [name: prod.name, logo: prod.logo, title: prod.title]
    }

}
