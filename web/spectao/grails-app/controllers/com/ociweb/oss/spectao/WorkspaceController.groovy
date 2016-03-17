package com.ociweb.oss.spectao

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED

@Transactional(readOnly = false)
class WorkspaceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    //static standardScaffolding = true
    def workspaceService

    def create () {
        def wsp = workspaceService.createWorkspace()
        println "starting redirect call"
        redirect (controller: "workspace", action: "edit", resource : wsp)

        println "end of create operation"
    }

    def edit (Workspace wsp) {
        println "edit, params = ${params} wsp = ${wsp}"
        if (!wsp)
            wsp = Workspace.get(id)
        respond wsp,
                model: [categoryList: workspaceService.categories(),
                        wsspec: workspaceService.wsSpec(wsp),
                        wsp: wsp,
                        product: [name: "TAO"]]

    }
    def index() {
        [categoryList: workspaceService.categories(), product: [name: "TAO"]]
    }

    def update(Workspace wsp) {
        println "in update, id = ${params}"
        def model = workspaceService.postPick (params, wsp)
        //render template: "workspaceView", model : [wsp : wsp]
        render template: "projectPicker", model : model
    }

    def showPick2 (Workspace wsp) {
        println "showPick2 called, params = ${params}"
//        redirect (controller: "workspace", action: "edit", resource : wsp)
        def model = workspaceService.postPick (params, wsp)
        render template: "workspaceView", model : [wsp : wsp, wsspec: workspaceService.wsSpec(wsp)]
    }

//    def updateOneProject (Workspace wsp, String proj, boolean sel) {
//        println "updateOneProject wid = ${wsp?.name} proj = ${proj}, sel = ${sel}"
//        MpcSubset sub = workspaceService.mapper.findProject(proj)?.subset
//        def checklist = workspaceService.updatePickList (sub, proj, sel == "true ", wsp)
//        render template: "projectPicker", model : [setname: "${sub.alias} after update" , sid: sub.id, checks: checklist, wid: wid]
//    }

    def showPicker (MpcSubset sub, int wid ) {
        println "showPick2 called, params = ${params}"
        def model = workspaceService.initPicker (sub, wid)
        render template: "projectPicker", model : model //[setname: sub.alias, sid: sub.id, checks: checklist, wid: wid]
    }

    def show (Workspace wsp) {
        println "show called, wsp = ${wsp}"
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
