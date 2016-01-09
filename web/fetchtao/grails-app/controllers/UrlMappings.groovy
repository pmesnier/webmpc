class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'Product', action:'index')
        "/taoRelease/populatePatchLevel/$id/$foo?" (controller:'TaoRelease', action:'populatePatchLevel')
        "/taoRelease/populateContent/$id/$patchLevel?" (controller:'TaoRelease', action:'populateContent')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
