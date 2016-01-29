class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'Product', action:'index')
        "/ociProduct/updateOciSelector/$id/$patchLevel?/$content?" (controller:'OciProduct', action:'updateOciSelector')
        "/download/$name"(resource:'download')

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
