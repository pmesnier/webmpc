class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

     //   "/downloadtao"(resources:'taoRelease')
        "/downloaddds"(resources:'ddsRelease')
        "/pickTaoRelease"(resources:'taoProduct')
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
