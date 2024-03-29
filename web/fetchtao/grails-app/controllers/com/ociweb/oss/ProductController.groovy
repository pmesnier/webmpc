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

    def showReleases (Product prod) {
        if (prod instanceof OciProduct)
            redirect (controller: 'ociProduct', action: "show", resource: (OciProduct)prod)
        else if (prod instanceof GitHubProduct) {
            redirect (controller: 'gitHubProduct', action: 'show', resource: (GitHubProduct)prod )
        }
        else if (prod.rlsurl) {
            redirect(url: prod.rlsurl)
        }
        else {
            Product p = prod
            [rlist: rlist, product:p]
        }
    }

}
