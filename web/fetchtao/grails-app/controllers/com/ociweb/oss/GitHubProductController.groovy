package com.ociweb.oss

/**
 * Created by phil on 1/3/16.
 */
class GitHubProductController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def index() {
        showReleases (prod)
    }

    def show (GitHubProduct prod) {
        showReleases (prod)
    }

    def showReleases (GitHubProduct prod) {
        if (prod.rlsurl && prod.rlsurl.length() > 0)
            redirect (url:prod.rlsurl)
        else {

            Product p = prod
            def rlist = GitHubService.fetchReleaseInfo(prod)
            respond prod, model: [rlist : rlist , product : p ]
        }
    }

    def downloadRelease (GitHubProduct prod) {
        String targeturl = GitHubService.targetLink (prod,params)
        redirect (url:targeturl)
    }


}
