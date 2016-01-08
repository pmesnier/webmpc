package com.ociweb.oss

/**
 * Created by phil on 1/3/16.
 */
class GitHubProductController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def index() {
        redirect (controller: 'product', action: 'index')
    }

    def show (GitHubProduct prod) {
        redirect (controller: 'product', action: 'show')
    }

    def showSource (GitHubProduct prod) {
        if (prod.name.equals("OCI TAO"))
            redirect(controller: 'taoRelease', action: 'index')
        String srcurl = prod.sourceURL()
        if (srcurl != null)
            redirect(url: srcurl)
        else {
            Product p = prod
            respond prod, model: [product: p]
        }
    }

    def showReleases (GitHubProduct prod) {
        if (prod.rlsurl && prod.rlsurl.length() > 0)
            redirect (url:prod.rlsurl)
        else {
            Product p = prod
            respond prod, model: [rlist : GitHubService.fetchReleaseInfo(prod), product : p ]
        }
    }

//    def showDocs (GitHubProduct prod) {
//        if (prod.name.equals ("Grails"))
//            redirect (url:prod.docs)
//        else
//            Product p = prod
//            respond prod, model: [ product : p ]
//    }
//
//    def showFAQ (GitHubProduct prod) {
//        if (prod.name.equals ("Grails"))
//            redirect (url:prod.faq)
//        else
//            Product p = prod
//            respond prod, model: [ product : p ]
//    }

    def downloadRelease (GitHubProduct prod) {
        String targeturl = GitHubService.targetLink (prod,params)
        redirect (url:targeturl)
    }


}
