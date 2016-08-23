package com.ociweb.oss.spectao

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import org.springframework.transaction.annotation.Propagation

@Transactional (propagation = Propagation.REQUIRED)
class WorkspaceService {

    static String[] editPage = ["projects", "features", "workspace"]

    @Transactional (propagation = Propagation.SUPPORTS)
    void init (String bootref) {
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(bootref)
        def initdef = jsonSlurper.parse(resource)
        MpcProjectManager.initAll(initdef)
        println "MpcProjectManager.initAll complete"
    }

    def createWorkspace (def wsName) {
        Workspace wsp = new Workspace([name    : wsName?: "workspace_${Workspace.list().size()}",
                                       projects : [],
                                       desiredProject: [],
                                       impliedProject: [],
                                       features: []])
        MpcProjectManager.loadFeatures(wsp)
//        MenuSubEntry sub = MenuSubEntry.get (1)
//        wsp.currentSubset = sub;
        wsp.product = MpcProjectManager.mapper.currentProduct

        wsp.save(failOnError : true)
        println "CreateWorkspace wsp saved, has ${wsp.product.rawProjects.size()} raw projects"
        wsp
    }

    @Transactional (propagation = Propagation.SUPPORTS)
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

    @Transactional (propagation = Propagation.SUPPORTS)
    void addAProject (String pName,  Workspace wsp) {
        MpcProject mpc = wsp.product.rawProjects.get(pName)
        if (mpc == null) {
            println "addAProject cannot find mpc project for ${pName}"
        }
        if (mpc) {
            println "addAProject found mpc for ${pName}"
            Project proj = wsp.projects?.find() { pName == it.mpc.name}
            if (proj != null) {
                println "addAProject wsp already had a project, desired count = ${proj.desired}"
            }
            else {
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

                proj = new Project([mpc: mpc, desired: 1, required: 0, afterProj: [], neededBy: []])
                wsp.addToProjects(proj)
            }
            proj.save (failOnError: true)
            println "addAProject adding ${mpc.name}"
            MpcProjectManager.addImplied(wsp, proj)
        }
    }


    @Transactional (propagation = Propagation.SUPPORTS)
    void postPick (def params, Workspace wsp) {
        boolean adding = true;
        String option = params.get("checked");
        if (option == null) {
            option = params.get("unchecked");
            adding = false;
        }
        println "postPick, option = ${option} and adding = ${adding}"

        if (option != null) {
            int pivot = option.indexOf(':')
            String listname = option.substring(0, pivot - 1)
            String pname = option.substring(pivot + 1)
            if (adding)
                addAProject(pname, wsp)
            else
                removeAProject(pname, wsp)
        }
        MpcProjectManager.refreshProjectNameLists(wsp)
    }

    static def getDefPage () {
        return [prev: "", curr: editPage[0], next: editPage[1]]
    }

    static def getNextPage (def from) {
        String prev = ""
        String curr = ""
        String next = ""
        for (int i = 0; i < editPage.size() ; i++) {
            if (from == editPage[i]) {
                prev = editPage[i];
                if (i < editPage.size() - 1) {
                    curr = editPage[i + 1]
                }
                if (i < editPage.size() - 2) {
                    next = editPage[i + 2]
                }
                break
            }
        }
        return [prev: prev, curr: curr, next: next]
    }

    static def getPrevPage (def from) {
        String prev = ""
        String curr = ""
        String next = ""
        for (int i = editPage.size()-1; i > -1 ; i--) {
            if (from == editPage[i]) {
                next = editPage[i];
                if (i > 0) {
                    curr = editPage[i - 1]
                }
                if (i > 1) {
                    prev = editPage[i - 2]
                }
                break
            }
        }
        return [prev: prev, curr: curr, next: next]
    }

    @Override
    int hashCode() {
        return super.hashCode()
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
