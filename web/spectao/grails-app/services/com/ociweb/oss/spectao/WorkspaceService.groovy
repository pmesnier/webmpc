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

    def createWorkspace (def wsName) {
        Workspace wsp = new Workspace([name    : (wsName == null) ? "unnamed_${Workspace.list().size()}" : wsName,
                                       required: [:],
                                       desiredProject: [],
                                       impliedProject: [],
                                       features: []])
        mpcProjectManager.loadFeatures(wsp)
        wsp.save(failOnError : true)
        wsp
    }

    def categories () {
        if (!catlist ) {
            catlist = mpcProjectManager.categories()
        }
        catlist
    }

    void initPicker (Workspace wsp) {
        MpcSubset sub = wsp.currentSubset
        if (sub) {
            wsp.checklist = []
            mpcProjectManager.loadChecklist (wsp)
            wsp.save()
        }
    }

    void postPick (def checks, Workspace wsp) {
        MpcSubset sub = wsp.currentSubset
        sub.mpcProjects.each { mpc ->
            if (checks.containsKey (mpc.name)) {
                if (!wsp.desiredProject.contains(mpc.name) ) {
                    wsp.desiredProject.add(mpc.name)
                    println "post pick adding ${mpc.name}"
                }
            }
            else if (checks.containsKey ("_${mpc.name}")) {
                int index = wsp.desiredProject.indexOf(mpc.name)
                if (index > -1) {
                    wsp.desiredProject.removeAt(index)
                    println "post pick removing ${key} at index ${index}"
                }
            }
        }
        mpcProjectManager.resolveImplied(wsp)

        initPicker (wsp)
    }

    void enableFeatures (Workspace wsp, def fvalue, enable) {
        mpcProjectManager.enableFeatures (wsp, fvalue, enable)
        initPicker (wsp)
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
