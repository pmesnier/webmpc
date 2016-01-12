import com.ociweb.oss.TaoLegacyService
import com.ociweb.oss.GitHubProduct
import com.ociweb.oss.GitHubService
import com.ociweb.oss.Product
import groovy.json.JsonSlurper


class BootStrap {

    def init = { servletContext ->
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource("products.json")
        def products = jsonSlurper.parse(resource)

        GitHubService.initAuthToken(products.gitHubAuthToken)

        if (Product.list().size == 0)
        {
            products.product.each{
                def prod = it.githubowner != null ? new GitHubProduct(it) : new Product (it)
                prod.initRelease(it)
                if (it.name.contains ("TAO")) {
                    TaoLegacyService.initProduct (prod, it.releaseInit)
                }
                prod.save(failOnError: true)
                println "product " + it.name + " saved"
            }
        }
    }

    def destroy = {
    }
}
