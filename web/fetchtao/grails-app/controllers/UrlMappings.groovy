class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'Product', action:'index')
        "/products/download/$pname" (controller:'Product', action:'showName')
        "/taoRelease/updateTaoSelector/$id/$patchLevel?/$content?/$compress?" (controller:'TaoRelease', action:'updateTaoSelector')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
