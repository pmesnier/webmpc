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
        println "show GHsource, name = " + prod.name
        if (prod.name.equals("OCI TAO"))
            redirect (controller: 'taoRelease', action: 'index')
        String srcurl = prod.sourceURL ()
        println "showSource : " + srcurl
        if (srcurl != null)
            redirect (url:srcurl)
        else
            respond prod
    }

    def showReleases (GitHubProduct prod) {
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
            respond prod, model: [rlist: rlist]
        }
    }

    def showLicense (GitHubProduct prod) {
        String lictext = prod.fetchLicense()
        if (lictext == "" && prod.license != null && prod.license.length() > 0)
            redirect (url:prod.license)
        else
        respond prod, model: [license: lictext]
    }

    def showDocs (GitHubProduct prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.docs)
        else
            respond prod
    }

    def showFAQ (GitHubProduct prod) {
        if (prod.name.equals ("Grails"))
            redirect (url:prod.faq)
        else
            respond prod
    }

    def downloadRelease (GitHubProduct prod) {
        if (params.bundle == null) {
            params.bundle = "tar.gz"
        }
        println "params.release: " + params.release + " bundle = " + params.bundle
        String targeturl = prod.targetLink (params)
        redirect (url:targeturl)
    }


}
