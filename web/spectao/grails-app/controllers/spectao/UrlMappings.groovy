package spectao

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/workspace/showProjectPicker/$id/$wid" (controller:'Workspace', action:'showProjectPicker')
        "/workspace/updateProject/$id" (controller:'Workspace', action:'updateProject')
        "/workspace/disable/$id" (controller:'Workspace', action:'disable')
        "/workspace/enable/$id" (controller:'Workspace', action:'enable')
        "/workspace/unchoose/$id" (controller:'Workspace', action:'unchoose')
        "/workspace/trigger/$id" (controller:'Workspace', action:'trigger')
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
