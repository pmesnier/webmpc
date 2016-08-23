package com.ociweb.oss.spectao

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED

@Transactional(readOnly = false)
class WorkspaceController {
    static editTemplate = "editMainView"

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def workspaceService

    def create () {
        println "starting redirect call params = ${params}"
        session.setAttribute ("editPage", WorkspaceService.getDefPage())
        def wsp = workspaceService.createWorkspace(params.wsName)
        redirect (controller: "workspace", action: "edit", resource : wsp)
    }

    def edit (Workspace wsp) {
        def pages = session.editPage
        println "edit, params = ${params} wsp = ${wsp} pages = ${pages}"

        if (!wsp)
            wsp = Workspace.get(id)
        respond wsp,
                model: [categoryList: MpcProjectManager.categories(),
                        templateName: editTemplate,
                        wsp: wsp,
                        product: [name: "TAO"]]
    }

    def index() {
        [categoryList: MpcProjectManager.categories(), product: [name: "TAO"]]
    }

    def update(Workspace wsp) {
        workspaceService.postPick (params, wsp)
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp: wsp,
                     product: [name: "TAO"]]
        render template: editTemplate, model : model
    }

    def prevPressed (int wid) {
        def page = WorkspaceService.getPrevPage (session.editPage.curr);
        session.editPage = page;
        Workspace wsp = Workspace.get(wid);
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp: wsp,
                     product: [name: "TAO"]]
        render template: editTemplate, model: model
    }

    def nextPressed (int wid) {
        def page = WorkspaceService.getNextPage (session.editPage.curr);
        session.editPage = page;
        Workspace wsp = Workspace.get(wid);

        def dlist = wsp.getDisabledFeatures();

        println "next pressed page = ${page} wsp = ${wsp}"
        def disList = wsp.disabledFeatures
        def enList = wsp.enabledFeatures
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp: wsp,
                     disabled: disList,
                     enabled: enList,
                     product: [name: "TAO"]]
                render template: editTemplate, model: model
    }

    def updateProject (int id) {
        Workspace wsp = Workspace.get(params.remove("wid").toInteger())
        params.remove ("controller");
        params.remove ("format");
        params.remove ("action");
        println "calling postpick with ${params}"
        workspaceService.postPick (params, wsp)
        def model = [wsp: wsp]
        render template: "projectPicker", model: model
    }

    def showProjectPicker(MenuSubEntry sub, int wid ) {
        Workspace wsp = Workspace.get (wid)
        wsp.currentSubset = sub
        def model = [wsp: wsp]
        render template: "projectPicker", model : model
    }

    def enable(Workspace wsp) {
        println "enable pressed"
        workspaceService.enableFeatures(wsp, params.dis, true)
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp         : wsp, product: [name: "TAO"]]
        render template: editTemplate, model: model
    }

    def disable(Workspace wsp) {
        println "disable pressed"
        workspaceService.enableFeatures(wsp, params.enab, false)
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp         : wsp, product: [name: "TAO"]]
        render template: editTemplate, model: model
    }

    def unchoose(Workspace wsp) {
        workspaceService.removeProjects (params.userPicked, wsp)
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp: wsp, product: [name: "TAO"]]
        render template: editTemplate, model: model
    }

    def trigger(Workspace wsp) {
        workspaceService.setBuildType (params.output, params.archive, wsp)
        if (wsp.buildType?.size() > 0) {
            workspaceService.buildIt (wsp)
        } else {
            //Todo: display a popup window that alerts if buildtype list is empty
        }
        // or shows how to retrieve built package when ready
        def model = [categoryList: MpcProjectManager.categories(),
                     templateName: editTemplate,
                     wsp: wsp, product: [name: "TAO"]]
        render template: editTemplate, model: model
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
