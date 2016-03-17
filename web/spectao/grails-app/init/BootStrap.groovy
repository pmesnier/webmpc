class BootStrap {
    def workspaceService

    def init = { servletContext ->
        workspaceService.init("MPC_projects/tao22a.json")
        println "BootStrap.init is done"
    }

    def destroy = {
    }
}
