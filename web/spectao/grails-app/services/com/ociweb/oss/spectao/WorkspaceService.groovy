package com.ociweb.oss.spectao

import groovy.json.JsonSlurper

class WorkspaceService {

    void init (String bootref) {
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(bootref)
        def initdef = jsonSlurper.parse(resource)
        MpcProjectManager.initAll(initdef)
    }

    def createWorkspace (def wsName) {
        Workspace wsp = new Workspace([name    : wsName?: "workspace_${Workspace.list().size()}",
                                       projects : [],
                                       desiredProject: [],
                                       impliedProject: [],
                                       features: []])
        MpcProjectManager.loadFeatures(wsp)
        MpcSubset sub = MpcSubset.get (7)
        wsp.currentSubset = sub;
        wsp.product = MpcProjectManager.mapper.currentProduct

        wsp.save(failOnError : true)
        wsp
    }

    void removeAProject (String name, Workspace wsp) {
        Project proj = wsp.projects.find {it.mpc.name == name}
        if (proj) {
            if (proj.desired > 0) {
                proj.desired --
                if (proj.desired == 0) {
                    MpcProjectManager.removeImplied (wsp, proj)
                }
            }
            println "removing ${name}"
            wsp.projects.remove(proj)
            wsp.desiredProject.remove (name)
        }

    }

    void removeProjects (def toRemove, Workspace wsp) {
        if (toRemove instanceof String ) {
            removeAProject (toRemove, wsp)
        }
        toRemove.each { name ->
            removeAProject (name, wsp)
        }
    }

    void postPick (def checks, Workspace wsp) {
        MpcSubset sub = wsp.currentSubset
        sub.mpcProjects.each { mpc ->
            if (checks.containsKey (mpc.name)) {
                Project proj = wsp.projects.find {it.mpc.name == mpc.name}
                if (proj == null) {

                    mpc.projectRequires?.each { ureq ->
                        Feature f = wsp.features.find { ureq == it.mpcFeature.name }
                        if (f && !f.isComment && !f.enabled) {
                            if (f.byUser) {
                                println "overriding user setting feature ${ureq} disabled"
                            }
                            f.enabled = true

                        }
                    }
                    mpc.projectAvoids?.each { uavo ->
                        Feature f = wsp.features.find { uavo == it.mpcFeature.name }
                        if (f && !f.isComment && f.enabled) {
                            if (f.byUser) {
                                println "overriding user setting feature ${uavo} disabled"
                            }
                            f.enabled = false
                        }

                    }

                    proj = new Project ([mpc: mpc, desired: 1, required: 0, afterProj: [], neededBy: []])
                    wsp.addToProjects (proj)
                    proj.save (failOnError: true)
                    println "post pick adding ${mpc.name}"
                    MpcProjectManager.addImplied(wsp, proj)
                }
            }
            else if (checks.containsKey ("_${mpc.name}")) {
                removeAProject(mpc.name, wsp)
            }
        }
        MpcProjectManager.refreshProjectNameLists(wsp)
    }


    void enableFeatures (Workspace wsp, def fvalue, enable) {
        MpcProjectManager.enableFeatures (wsp, fvalue, enable)
    }

    void setBuildType (def bt, def arch, Workspace wsp) {
        wsp.buildType = []

        if (bt instanceof String) {
            wsp.buildType.add (bt)
        }
        else {
            wsp.buildType = bt
        }
        wsp.archiveType = arch
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

    def buildIt (Workspace wsp) {
        println "Launching task to build workspace\n"
        // Todo: prepare workspace generation area
        // Todo: write custom MWC file
        // Todo: trigger mwc.pl on file with approrpiate arguments
        // Todo: gather console output
        // Todo: package using selected archive format
        // Todo: stage for pickup
    }
}
