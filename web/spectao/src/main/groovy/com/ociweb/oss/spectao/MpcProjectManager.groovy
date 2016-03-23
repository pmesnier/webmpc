package com.ociweb.oss.spectao

import groovy.json.JsonSlurper

class MpcProjectManager {
    MpcMapper mapper = new MpcMapper()
    def mfeatures = []
    def buildTypes = []
    def jsonSlurper = new JsonSlurper()

    void initAll(def initresource) {
        def mpcprojects = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.sources))
        println "core size = ${mpcprojects.core.size()}"
        println "tests size = ${mpcprojects.tests.size()}"
        println "examples size = ${mpcprojects.examples.size()}"

        mpcprojects.each { cat ->
            println "category ${cat.key} has ${cat.value.size()} elements"
            mapper.addCategory(cat.key)
            cat.value.each { gconf ->
                MpcGroup group = mapper.addGroup(gconf.key)
                gconf.value.each { ssconf ->
                    MpcSubset subset = mapper.addSubset(ssconf)
                    ssconf.fileRoot.each { directory ->
                        def dir = getClass().getClassLoader().getResource(directory)
                        def params = [:]
                        params << [groupName: group.name, subName: subset.alias]
                        if (dir.toString().endsWith(".json"))
                            addToMapper(params, dir)
                        else
                            loadProjects(params, dir)
                    }
                }
            }
        }
        mapper.resolveDependencies()
        def feat = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.features))?.features
        feat.each {
            MpcFeature mf = new MpcFeature (it)
            mf.save()
            mfeatures.add(mf)
        }
        buildTypes = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.types))?.buildTypes
        println "found ${mfeatures.size()} features and ${buildTypes.size()} build types"
    }

    void addToMapper(def params, def res) {
        def projdef = jsonSlurper.parse(res).project
        if (projdef) {
            def inherit = projdef.inheritance
            projdef << [mpcpath: inherit ? inherit.get(projdef.name) : "undef"]
            projdef << params
            mapper.addProjectUnit(projdef)
        }

    }

    void loadProjects(def params, def dir) {
        File dirFile;

        if (dir instanceof URL) {
            def uri = dir.toURI()
            dirFile = new File(uri)
        } else if (dir instanceof URI) {
            dirFile = new File(dir)
        } else if (dir instanceof File) {
            dirFile = dir;
        } else if (dir instanceof String) {
            dirFile = new File(dir)
        }

        dirFile.listFiles().each { entry ->
            if (entry.getName() != "." && entry.getName() != "..") {
                if (entry.isDirectory()) {
                    loadProjects(params, entry);
                } else if (entry.getName().endsWith(".json")) {
                    addToMapper(params, entry)
                }
            }
        }
    }

    def categories () {
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

    void loadFeatures (Workspace wsp) {
        if (wsp.features == null) {
            wsp.features = []
        }

        mfeatures.eachWithIndex { feat, i ->
            Feature f = new Feature ([mpcFeature: feat, isComment: feat.defState == "comment", enabled: feat.defState == "1"])
            wsp.addToFeatures(f);
            if (i > 80) {
                println "saving feature ${i} for ${feat.name}"
            }
            f.save(failOnError:true)
        }
    }

    void loadChecklist (Workspace wsp) {
        wsp.currentSubset.mpcProjects.each { mpc ->
            boolean disable = false
            mpc.units.each { unit ->
                unit.requires?.each {
                    if (wsp.disabledFeatures.contains(it)) {
                        disable = true
                    }
                    if (disable)
                        return
                }
                if (!disable) {
                    unit.avoids?.each {
                        if (wsp.enabledFeatures.contains(it)) {
                            disable = true
                        }
                        if (disable)
                            return
                    }
                }
                if (disable) {
                    Project proj = wsp.desiredProject.find {it == mpc.name}
                    if (proj) {
                        proj.desired --;
                        proj.afterProj.each { after ->
                            after.required --;
                        }
                        proj.neededBy.each { need ->

                        }

                    }
                    return
                }
            }
            int selected = (wsp.desiredProject.contains(mpc.name) || wsp.impliedProject.contains(mpc.name)) ? 1 : 0
            wsp.checklist.add ([name: mpc.name, checked: selected, disabled:disable])
        }
    }

    boolean cancelDisabledProject (Workspace wsp, MpcProject proj, MpcFeature feat) {
        boolean lostPrec = false
        proj.precedents.each { prec ->
            lostPrec = cancelDisabledProject(wsp, prec, feat)
        }
        if (!lostPrec) {
            proj.units.each { unit ->
                lostPrec |= unit.requires.contains (feat.name)
            }
        }
        if (lostPrec) {
            wsp.desiredProject.remove (proj.name)
            wsp.required.remove (proj.name)
        }
        lostPrec

    }

    void enableAfeature (Workspace wsp, String name, boolean enable) {
        Feature f = wsp.features.find { it.mpcFeature.name == name}
        if (f == null) {
            print "enable feature ${name} is null"
        }
        else {
            if (!f.isComment) {
                if (!enable) {
                    wsp.desiredProject.each {
                        MpcProject proj = mapper.findProject (it)
                        cancelDisabledProject(wsp, proj, f.mpcFeature)
                    }
                }
                f.enabled = enable;
            }
        }
    }

    void enableFeatures (Workspace wsp, def fvalue, enable) {
        if (fvalue instanceof String) {
            enableAfeature(wsp, fvalue, enable)
        }
        else {
            fvalue.each {
                enableAfeature(wsp, it, enable)
            }
        }
    }


    def projectPrecedent (MpcProject mpc, Workspace wsp) {
        println "projectPrecident mpc = ${mpc.name}, deep = ${deep}, ${implied}"
        Project prec = wsp.required.get(mpc.name)
        if (!prec) {
            prec = new Project([mpc: mpc, afterProj: [], neededBy: []])
            wsp.required.put(mpc.name, prec)
            prec.workspace = wsp
            prec.save(failOnError: true)
        } else {
            println "projectPrecident mpc = ${mpc.name}, deep = ${deep}, ${implied}"
        }
        if (deep && prec.afterProj.size() < mpc.precedents?.size()) {
            println "projectPrecident going deep on ${mpc.name}"
            mpc.precedents?.each {
                Project sub = projectPrecedent(it.owner, wsp, deep, true)
                sub.addToNeededBy(prec)
                prec.addToAfterProj(sub)
            }
        }

        if (implied)
            prec.required++
        else
            prec.desired++

        prec
    }

    def resolveImplied_i (Workspace wsp, MpcProject mpcProject) {
        mpcProject.precedents?.each { prec ->
            String pName = prec.owner.name
            if (!wsp.desiredProject.contains(pName) &&
                    !wsp.impliedProject.contains(pName)) {
                 wsp.impliedProject.add (pName)
                MpcProject proj = mapper.findProject(pName)
                resolveImplied_i (wsp, proj)
            }
        }
    }

    def resolveImplied (Workspace wsp) {
        wsp.impliedProject.clear()
        wsp.desiredProject.each { mpcName ->
            MpcProject mpcProject = mapper.findProject(mpcName)
            if (mpcProject == null) {
                println "No project for ${mpcName}"
            }
            else {
                resolveImplied_i(wsp, mpcProject)
            }
        }
    }


}
