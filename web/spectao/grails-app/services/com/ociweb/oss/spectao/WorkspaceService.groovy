package com.ociweb.oss.spectao

import groovy.json.JsonSlurper

class WorkspaceService {
    def mpcProjectManager = new MpcProjectManager()
    MpcMapper mapper
    def catlist

    void init (String bootref) {
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(bootref)
        def initdef = jsonSlurper.parse(resource)
        mpcProjectManager.initAll(initdef)
        mapper = mpcProjectManager.mapper
    }

    String wsSpec (Workspace wsp) {
        String spec = ""
        String dlist = ""
        String rlist = ""
        wsp.projects.values().each {
            if (it.desired > 0 && it.required == 0) {
                dlist += "${it.mpc.name}\n"
            }
            else if (it.required > 0) {
                rlist += "${it.mpc.name}\n"
            }

        }
        spec = "Projects You Chose:\n${dlist}\nAdditional Required Projects\n${rlist}"
    }

    def createWorkspace () {
        Workspace wsp = new Workspace([name    : "unnamed_${Workspace.list().size()}",
                                       projects: [:]])
        wsp.save(failOnError : true)
        println "createWorkspace saving new workspace"
        wsp.save ()

        wsp.save(failOnError: true)
        println "createWorkspace returning"
        wsp
    }

    def categories () {
        if (!catlist ) {
            catlist = mpcProjectManager.categories()
        }
        catlist
    }

    def initPicker (MpcSubset sub, int wid) {
        def checklist = []
        Workspace wsp = Workspace.get(wid)
        if (!wsp ){
            println "did not find workspace for wid = ${wid}"
        }
        mpcProjectManager.loadChecklist (sub, wsp, checklist)
        wsp.save()
        println "returning from initpicker "
        [setname: sub?.alias, sid: sub?.id, checks: checklist, wid: wsp.id]
    }

    def postPick (def checks, Workspace wsp) {
        def onlist = []
        MpcSubset subset = null
        checks.each { entry ->
            if (!entry.key.startsWith('_')) {
                MpcProject mproj = mapper.findProject(entry.key)
                if (!subset) {
                    subset = mproj?.subset
                }
                if (mproj) {
                    onlist.add (entry.key)
                    Project prj = mpcProjectManager.projectPrecedent(mproj,wsp,true,false)

//                    wsp.projects.get (mproj.name)
//                    prj.with {
//                        println "postPick, name = ${entry.key}, desired = ${desired}"
//                        if (desired == 0) {
//                            desired++
//                            afterProj.each { rproj ->
//                                rproj.required++
//                            }
//                        }
//                    }
                }
            }
            else {
                if (!subset) {
                    MpcProject mproj = mapper.findProject(entry.key.substring(1))
                    subset = mproj?.subset
                }
            }
        }

        def checklist = []
        subset?.mpcProjects.each { mpc ->
            Project prj = wsp.projects.get (mpc.name) //find {it.mpc.name == mpc.name }
            if (!onlist.contains(mpc.name)) {
                 if (prj.desired > 0) {
                    prj.desired--
                    prj.afterProj.each { rproj ->
                        if (rpoj.required > 0) {
                            rproj.required--
                        }
                        else {
                            println "project ${mpc.name} afterproj ${rproj.mpc.name} required underflow"
                        }
                    }
                }
            }
            checklist.add (prj)
        }

        [setname: subset?.alias, sid: subset?.id, checks: checklist, wid: wsp.id]
    }

    def getWorkspace (int wid) {
        Workspace wsp =  Workspace.get (wid)
        if (!wsp ){
            println "did not find workspace for wid = ${wid}"
        }

    }


    def existingWorkspace (String name) {
        Workspace.find (name)
    }
}
