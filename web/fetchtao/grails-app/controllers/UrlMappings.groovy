class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

     //   "/downloadtao"(resources:'taoPackage')
        "/downloaddds"(resources:'ddsPackage')
        "/pickTaoRelease"(resources:'taoReleases')
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
