package com.ociweb.oss

/**
 * Created by phil on 1/29/16.
 */
class DownloadController {

    def show () {
        if (!params.name)
        {
            render "You need a product name"
        }

        Product prod = OciProduct.find {
            it.name.equals (params.name)
        }
        if (prod)
            redirect (controller: 'ociProduct', action: "show", resource: (OciProduct)prod)
        prod = GitHubProduct.find {
            it.name.equals (params.name)
        }
        if (prod)
            redirect (controller: 'gitHubProduct', action: "show", resource: (GitHubProduct)prod)
    return 404
    }

}
