package com.ociweb.oss.spectao

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED

@Transactional(readOnly = false)
class WorkspaceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    //static standardScaffolding = true
    def workspaceService

    def create () {
        println "starting redirect call params = ${params}"
        def wsp = workspaceService.createWorkspace(params.wsName)
        redirect (controller: "workspace", action: "edit", resource : wsp)
    }

    def edit (Workspace wsp) {
        println "edit, params = ${params} wsp = ${wsp}"
        if (!wsp)
            wsp = Workspace.get(id)
        respond wsp,
                model: [categoryList: workspaceService.categories(),
                        wsp: wsp,
                        product: [name: "TAO"]]

    }
    def index() {
        [categoryList: workspaceService.categories(), product: [name: "TAO"]]
    }

    def update(Workspace wsp) {
        workspaceService.postPick (params, wsp)
        def model = [wsp: wsp]
        render template: "projectPicker", model : model
    }

    def updateProject (Workspace wsp) {
        workspaceService.postPick (params, wsp)
        def model = [categoryList: workspaceService.categories(),
                     wsp: wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

     def enable (Workspace wsp) {
         if (params.mode == "dis")
             workspaceService.enableFeatures (wsp, params.enab, false)
         else
             workspaceService.enableFeatures (wsp, params.dis, true)
         def model = [categoryList: workspaceService.categories(),
                      wsp: wsp, product: [name: "TAO"]]
         render template: "editMainView", model : model
     }

    def resolve (Workspace wsp) {
        println "resolve called, params = ${params}"
        def model = workspaceService.resolve (wsp)
        render template: "workspaceView", model : [wsp : wsp]
    }

    def next (Workspace wsp) {
        println "next called, params = ${params}"
        def model = workspaceService.resolve (wsp)
        render template: "workspaceView", model : [wsp : wsp]
    }

    def previous (Workspace wsp) {
        println "previous called, params = ${params}"
        def model = workspaceService.resolve (wsp)
        render template: "workspaceView", model :[wsp : wsp]
    }

    def discard (Workspace wsp) {
        println "discard called, params = ${params}"
        def model = workspaceService.resolve (wsp)
        render template: "workspaceView", model : [wsp : wsp]
    }

    def showProjectPicker(MpcSubset sub, int wid ) {
        Workspace wsp = Workspace.get (wid)
        wsp.currentSubset = sub
        workspaceService.initPicker (wsp)
        def model = [wsp: wsp]
        render template: "projectPicker", model : model
    }

    def show (Workspace wsp) {
        respond wsp, model: [categoryList: workspaceService.categories(), product: [name: "TAO"]]
    }

    @Transactional
    def save(Workspace wsp) {
        println "save workspace, ${wsp}"
        if (wsp == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (wsp.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond wsp.errors, view:'create'
            return
        }

        wsp.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'wsp.lable', default: 'Workspace'), wsp.name])
                redirect wsp
            }
            '*' { respond wsp, [status: CREATED] }
        }
    }


}
