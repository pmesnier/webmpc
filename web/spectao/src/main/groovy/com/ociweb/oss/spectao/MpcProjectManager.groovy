package com.ociweb.oss.spectao

import groovy.json.JsonSlurper
import org.hibernate.jdbc.Work

class MpcProjectManager {
    static MpcMapper mapper = new MpcMapper()

    static void initAll(def initresource) {
        mapper.initAll(initresource)
    }

    static def categories () {
        def catlist = []
        mapper.categories.each { cat ->
            def subsets = []
            cat.group.each { mgrp ->
                mgrp.subset.each { sub ->
                    if (sub.displayOnTop) {
                        subsets.add([mpcSub: sub])
                    }
                }
            }

            if (subsets.size() > 0) {
                catlist.add([name: cat.name, subList: subsets])
            }
        }
        catlist
    }

    static void loadFeatures (Workspace wsp) {
        if (wsp.features == null) {
            wsp.features = []
        }

        MpcFeature.list().each { feat ->
            Feature f = new Feature ([mpcFeature: feat,
                                      isComment: feat.defState == "comment",
                                      enabled: feat.defState == "1",
            byUser: false])
            wsp.addToFeatures(f);
            f.save(failOnError:true)
        }
    }

    static void enableAfeature (Workspace wsp, String name, boolean enable) {
        Feature f = wsp.features.find { it.mpcFeature.name == name}
        if (f == null) {
            print "enable feature ${name} is null"
        }
        else {
            if (!f.isComment) {
                f.enabled = enable;
                f.byUser = true;
                wsp.projects.each { proj ->
                    if (proj.mpc.projectRequires.contains (name)) {
                        if (enable) {
                            proj.disabledBy.remove(name)
                        }
                        else {
                            proj.disabledBy.add (name)
                        }
                    }
                    else if (proj.mpc.projectAvoids.contains (name)) {
                        if (enable) {
                            proj.disabledBy.remove(name)
                        }
                        else {
                            proj.disabledBy.add (name)
                        }
                    }
                }
            }
        }
    }

    static void enableFeatures (Workspace wsp, def fvalue, enable) {
        if (fvalue instanceof String) {
            enableAfeature(wsp, fvalue, enable)
        }
        else {
            fvalue.each {
                enableAfeature(wsp, it, enable)
            }
        }
    }

    static int findImplied (Workspace wsp, MpcProject mpc, List<String> implied) {
        int added = 0
        mpc.units?.each { unit ->
            unit.after.each { after ->
                MpcProject prec = wsp.product.rawProjects.get(after)?:wsp.product.rawUnits.get(after)?.owner
                String pName = prec?.name
                if (pName && !implied.contains(pName)) {
                    implied.add(pName)
                    added++
                    int subs = findImplied (wsp, prec, implied)
                    added += subs
                }
            }
        }
        added
    }

    static def addImplied(Workspace wsp, Project desiredProj) {
        List<String> implied = new ArrayList<>()
        findImplied(wsp, desiredProj.mpc, implied)
        implied.each { pName ->
            if (pName != desiredProj.mpc.name) {

                // get the project element, increment the required count save the project
            }
        }
        // get the poject element for the desired project, increment the desired count and save
        // return
    }

//        project.mpc.units?.each { unit ->
//            unit.after.each { after ->
//                MpcProject prec = wsp.product.rawProjects.get(after)?:wsp.product.rawUnits.get(after)?.owner
//                String pName = prec.name
//                Project depProj = wsp.projects.find { it.mpc.name == pName }
//                if (depProj == null) {
//                    depProj = new Project([mpc: prec, desired: 0, required: 0, afterProj: [], neededBy: []])
//                    wsp.addToProjects(depProj)
//                    depProj.save(failOnError: true)
//                    addImplied(wsp, depProj)
//                }
//                depProj.addToNeededBy(project);
//                project.addToAfterProj(depProj);
//                depProj.required++
//            }
//        }
//    }

    static def removeImplied (Workspace wsp, Project project) {
        project.mpc.units?.each { unit ->
            unit.after.each { after ->
                MpcProject prec = wsp.product.rawProjects.get(after)?:wsp.product.rawUnits.get(after)?.owner
                String pName = prec.name
                Project depProj = wsp.projects.find { it.mpc.name == pName }
                if (depProj == null) {
                    println "removeImplied unable to find for project for ${pName}"
                }
                depProj.required--
                if (depProj.required <= 0) {
                    if (depProj.required < 0) {
                        println "removeImplied, required count underflow, project ${pName} desired = ${depProj.desired}, required = ${depProj.required}"
                    }
                    wsp.projects.remove(depProj)
                    wsp.impliedProject.remove(pName)
                }
            }
        }
    }

    static def refreshProjectNameLists (Workspace wsp) {
        wsp.impliedProject.clear()
        wsp.desiredProject.clear()
        wsp.projects.each { proj ->
            if (proj.desired)
                wsp.addToDesiredProject (proj.mpc.name)
            else
                wsp.addToImpliedProject (proj.mpc.name)
        }
    }


}
