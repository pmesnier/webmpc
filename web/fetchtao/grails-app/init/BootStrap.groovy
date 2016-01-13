import com.ociweb.oss.ProductService
import groovy.json.JsonSlurper


class BootStrap {

    def init = { servletContext ->
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource("products.json")
        def products = jsonSlurper.parse(resource)

        ProductService.initAll (products)
    }

    def destroy = {
    }
}
