class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'Product', action:'index')
        "/taoRelease/populatePatchLevel/$id" (controller:'TaoRelease', action:'populatePatchLevel')
        "/taoRelease/populateContent/$id/$patchLevel" (controller:'TaoRelease', action:'populateContent')
        "/taoRelease/populateCompress/$id/$patchLevel/$content" (controller:'TaoRelease', action:'populateCompress')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
