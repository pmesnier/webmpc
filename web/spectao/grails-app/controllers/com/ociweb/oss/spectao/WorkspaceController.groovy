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
                model: [categoryList: MpcProjectManager.categories(),
                        wsp: wsp,
                        product: [name: "TAO"]]

    }
    def index() {
        [categoryList: MpcProjectManager.categories(), product: [name: "TAO"]]
    }

    def update(Workspace wsp) {
        workspaceService.postPick (params, wsp)
        def model = [wsp: wsp]
        render template: "projectPicker", model : model
    }

    def updateProject (Workspace wsp) {
        workspaceService.postPick (params, wsp)
        def model = [categoryList: MpcProjectManager.categories(),
                     wsp: wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

    def enable(Workspace wsp) {
        workspaceService.enableFeatures(wsp, params.dis, true)
        def model = [categoryList: MpcProjectManager.categories(),
                     wsp         : wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

    def disable(Workspace wsp) {
        workspaceService.enableFeatures(wsp, params.enab, false)
        def model = [categoryList: MpcProjectManager.categories(),
                     wsp         : wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

    def unchoose(Workspace wsp) {
        workspaceService.removeProjects (params.userPicked, wsp)
        def model = [categoryList: MpcProjectManager.categories(),
                     wsp: wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

    def trigger(Workspace wsp) {
        workspaceService.setBuildType (params.output, params.archive, wsp)
        if (wsp.buildType.size() > 0)
            workspaceService.buildIt (wsp)
        //Todo: display a popup window that alerts if buildtype list is empty
        // or shows how to retrieve built package when ready
        def model = [categoryList: MpcProjectManager.categories(),
                     wsp: wsp, product: [name: "TAO"]]
        render template: "editMainView", model: model
    }

    def showProjectPicker(MpcSubset sub, int wid ) {
        Workspace wsp = Workspace.get (wid)
        wsp.currentSubset = sub
        def model = [wsp: wsp]
        render template: "projectPicker", model : model
    }

    def show (Workspace wsp) {
        respond wsp, model: [categoryList: MpcProjectManager.categories(), product: [name: "TAO"]]
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
