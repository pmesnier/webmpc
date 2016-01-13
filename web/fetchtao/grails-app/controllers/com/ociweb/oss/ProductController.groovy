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

    def showName () {
        println "request for " + params.pname

        def prod = Product.findByNameIlike (params.pname)
        if (!prod) {
            prod = Product.findByUriIlike (params.pname)
        }
        if (prod) {
            showReleases(prod)
        }
        else {
            println "could not find named product"
            redirect (url:"/notFound")
        }
    }

    def showSource (Product prod) {
        println "show source, name = " + prod.name
        if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        String srcurl = prod.sourceURL ()
        println "showSource : " + srcurl
        if (srcurl != null)
            redirect (url:srcurl)
        else{
            Product p = prod
            [product:p]
        }

    }

    def showReleases (Product prod) {
        if (prod.rlsurl && prod.rlsurl.length() > 0)
            redirect (url:prod.rlsurl)
        else if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        else if (prod instanceof GitHubProduct) {
            redirect (controller: 'gitHubProduct', action: 'showReleases', resource: (GitHubProduct)prod )
        } else {
            Product p = prod
            [rlist: rlist, product:p]
        }
    }

    def showLicense (Product prod) {
        String lictext = ""
        if (prod.hasProperty("githubowner"))
             lictext = GitHubService.fetchLicense((GitHubProduct)prod)
        if (lictext == "" && prod.license != null && prod.license.length() > 0)
            redirect (url:prod.license)
        else {
            Product p = prod
            [license: lictext, product:p]
        }
    }

    def showDocs (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.docs)
        else {
            Product p = prod
            [product:p]
        }
    }

    def showFAQ (Product prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.faq)
        else {
            Product p = prod
            [product: p]
        }
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
