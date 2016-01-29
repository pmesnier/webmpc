package com.ociweb.oss

import static grails.artefact.Interceptor$Trait$Helper.render

/**
 * Created by phil on 1/29/16.
 */
class DownloadController {

    def show () {
        if (!params.name)
        {
            render "You need a product name"
        }

        Product prod = OciProduct.list().find {
             it.name.equalsIgnoreCase (params.name)
        }
        if (prod) {
            redirect(controller: 'ociProduct', action: "show", resource: (OciProduct) prod)
        }
        else {
            prod = GitHubProduct.list().find {
                it.name.equalsIgnoreCase (params.name)
            }
            if (prod) {
                redirect(controller: 'gitHubProduct', action: "show", resource: (GitHubProduct) prod)
            }
            else {
                render status: 404
            }
        }
    }

}
