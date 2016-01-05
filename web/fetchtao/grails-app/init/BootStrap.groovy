
import com.ociweb.fetchtao.TaoProduct
import com.ociweb.fetchtao.TaoRelease
import com.ociweb.oss.GitHubAPI
import com.ociweb.oss.GitHubProduct
import com.ociweb.oss.Product
import groovy.json.JsonSlurper


class BootStrap {

    def init = { servletContext ->
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource("products.json")
        def products = jsonSlurper.parse(resource)

        GitHubAPI.initAuthToken(products.gitHubAuthTokenFile)

        if (Product.list().size == 0)
        {
            products.product.each{
                def prod = it.name.contains ("TAO") ? new TaoProduct (it) :
                        it.githubowner != null ? new GitHubProduct(it) : new Product (it)
                prod.initRelease(it)
                prod.save(failOnError: true)
                println "product " + it.name + " saved"
            }
        }
        println "product list now has " + Product.list().size
    }

    def destroy = {
    }
}
