class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/product/index", controller:"com.ociweb.oss.ProductController")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
