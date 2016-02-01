package com.ociweb.oss

/**
 * Created by phil on 1/3/16.
 */
class GitHubProductController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def gitHubService

    def index() {
        show (prod)
    }

    def show (GitHubProduct prod) {
        if (prod.rlsurl && prod.rlsurl.length() > 0)
            redirect (url:prod.rlsurl)
        else {
            def rlist = gitHubService.fetchReleaseInfo(prod)
            def pkg = gitHubService.target(prod.latest)
            respond prod, model: [rlist : rlist ,
                                  product : prod,
                                  pkg : pkg]
        }
    }

    def updateGitHubSelector (GitHubRelease rel) {
        def model = [product: rel.product]
        def tmpl = rel.product.dynamicDivId
        def pkg = gitHubService.target(rel)
        model << [pkg : pkg ]

        render template: tmpl, model: model
    }

    def licenseFor (GitHubProduct prod ) {
        render prod.licenseText
    }

}
