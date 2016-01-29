package com.ociweb.oss

/**
 * Created by phil on 1/13/16.
 */
class ProductService {
    def gitHubService
    def ociService

    void initAll (def products) {
        if (products.gitHubAuthTokenFile)
            gitHubService.initAuthToken(products.gitHubAuthTokenFile)
        else
            gitHubService.initAuthToken(products.gitHubAuthToken)

        if (Product.list().size == 0)
        {
            products.product.each{ loadProduct(it) }
        }

    }

    void loadProduct (def params) {
        def prod = Product.findByName (params.name);
        if (prod == null) {
            prod = params.githubowner ? new GitHubProduct (params) :
                    params.ociReleaseInit ? new OciProduct (params) : new Product(params)
            initProduct(prod,params)
        }
        else
            prod.refresh (params)
        prod.save(failOnError: true)
        println "product " + params.name + " saved"
    }

    void initProduct (prod, params) {
        if (prod instanceof OciProduct) {
            ociService.initProduct (prod, params)
        }

        if (!(prod.descstr && prod.descstr.length() > 0) && (prod.descref && prod.descref.length() > 0)) {
            prod.descstr = getClass().getClassLoader().getResourceAsStream(prod.descref).text
        }
    }

}
