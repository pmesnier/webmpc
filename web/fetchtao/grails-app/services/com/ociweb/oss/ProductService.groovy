package com.ociweb.oss

import java.text.DecimalFormat

/**
 * Created by phil on 1/13/16.
 */
class ProductService {
    def gitHubService
    def ociService

    Map licenseCache = [:]

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

    void initProduct (def prod, def params) {
        if (prod instanceof OciProduct) {
            ociService.initProduct (prod, params)
        }

    }

    void initRelease (def prod, def release) {
        release.orderName = prod.orderName
    }

    String getLicenseText (Product prod) {
        if (prod instanceof GitHubProduct) {
            return gitHubService.fetchLicense (this, prod)
        }
        else {
            return ociService.fetchLicense (this, prod)
        }
    }

}
