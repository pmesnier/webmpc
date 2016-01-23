class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'Product', action:'index')
        "/products/download/$pname" (controller:'Product', action:'showName')
        "/ociProduct/updateTaoSelector/$id/$patchLevel?/$content?/$compress?" (controller:'OciProduct', action:'updateTaoSelector')
        "/ociProduct/updateAceSelector/$id/$patchLevel?/$content?/$compress?" (controller:'OciProduct', action:'updateTaoSelector')
        "/ociProduct/updateJbossSelector/$id/$patchLevel?/$content?/$compress?" (controller:'OciProduct', action:'updateJbossSelector')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
