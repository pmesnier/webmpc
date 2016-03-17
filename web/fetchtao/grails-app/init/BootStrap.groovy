import com.ociweb.oss.ProductService
import groovy.json.JsonSlurper


class BootStrap {
    def productService

    def init = { servletContext ->
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource("products.json")
        def products = jsonSlurper.parse(resource)
        productService.initAll (products)
    }

    def destroy = {
    }
}
