package spectao

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/workspace/showPicker/$id/$wid" (controller:'Workspace', action:'showPicker')
        "/workspace/showPick2/$id" (controller:'Workspace', action:'showPick2')
      //  "/workspace/updateOneProject/$wid?/$proj?/$sel?" (controller:'Workspace', action:'updateOneProject')
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
