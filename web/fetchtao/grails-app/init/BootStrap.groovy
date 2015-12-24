
import com.ociweb.fetchdds.DdsRelease
import com.ociweb.fetchtao.TaoProduct
import com.ociweb.fetchtao.TaoRelease
import com.ociweb.oss.Product
import groovy.json.JsonSlurper


class BootStrap {

    def init = { servletContext ->

        if (Product.list().size == 0)
        {
            def jsonSlurper = new JsonSlurper()
            def resource = getClass().getClassLoader().getResource("products.json")
            def products = jsonSlurper.parse(resource)
            products.Product.each{
                def prod = it.name.contains ("TAO") ? new TaoProduct (it) : new Product (it)
                prod.initRelease(it)
                prod.save(failOnError: true)
                println "product " + it.name + " saved"
            }

        }
    }

    def destroy = {
    }
}
