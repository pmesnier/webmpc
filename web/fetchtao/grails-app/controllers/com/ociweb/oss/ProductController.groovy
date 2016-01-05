package com.ociweb.oss

/**
 * Created by phil on 12/25/15.
 */
class ProductController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def index() {
        [productList: Product.list()]
    }

    def show (Product prod) {
        respond prod
    }

    def showSource (Product prod) {
        println "show source, name = " + prod.name
        if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        String srcurl = prod.sourceURL ()
        println "showSource : " + srcurl
        if (srcurl != null)
            redirect (url:srcurl)
        else
            respond prod
    }

    def showReleases (Product prod) {
        if (prod.rlsurl && prod.rlsurl.length() > 0)
            redirect (url:prod.rlsurl)
        else if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        else {
            def rlist = prod.fetchReleaseInfo()
            if (rlist == null || rlist.size() == 0) {
                rlist = []
                rlist << [name: "none", tarball_url: "", zipball_url: ""]
            }
            if (prod instanceof GitHubProduct) {
                redirect (controler: 'gitHubProuct', action: 'showReleases')
            } else {
                respond prod, model: [rlist: rlist]
            }
        }
    }

    def showLicense (Product prod) {
        String lictext = prod.fetchLicense()
        if (lictext == "" && prod.license != null && prod.license.length() > 0)
            redirect (url:prod.license)
        else
            respond prod, model: [license: lictext]
    }

    def showDocs (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.docs)
        else
            respond prod
    }

    def showFAQ (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.faq)
        else
            respond prod
    }

    def downloadRelease (Product prod) {
        if (params.format == null) {
            params.format = "tar.gz"
        }
        println "params.release: " + params.release + " format = " + params.format
        String targeturl = prod.targetLink (params)
        redirect (url:targeturl)
    }

}
