package com.ociweb.oss.spectao

import groovy.json.JsonSlurper

class MpcProjectManager {
    MpcMapper mapper = new MpcMapper()
    def initialSelected = []
    def jsonSlurper = new JsonSlurper()

    void initAll(def initresource) {
        def subres = getClass().getClassLoader().getResource(initresource.projectRoot)
        def mpcprojects = jsonSlurper.parse(subres)
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
        initialSelected = initresource.initialSelection.collect {
            mapper.findProject(it)
        }
        if (initialSelected.size() == 0)
            initialSelected = mapper.rawProjects.values()
        println "initial selection has = ${initialSelected.size()}"
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

    def loadChecklist (MpcSubset sub, Workspace wsp, def checklist) {
        sub.mpcProjects.each { mpc ->
            Project proj = projectPrecedent(mpc, wsp, false, false)
            println "loadChecklist adding ${mpc.name} desired = ${proj.desired} required = ${proj.required}"
            checklist.add (proj)
        }
        println "loadChecklist returns\n"
    }

    def projectPrecedent (MpcProject mpc, Workspace wsp, boolean deep, boolean implied) {
        Project prec = wsp.projects.get(mpc.name)
        if (!prec) {
            prec = new Project([mpc: mpc, afterProj: [], neededBy: []])
            wsp.projects.put(mpc.name, prec)
            prec.workspace = wsp
            prec.save(failOnError: true)
            if (deep) {
                mpc.precedents.each {
                    Project sub = projectPrecedent(it, wsp, deep, true)
                    sub.addToNeededBy (prec)
                    prec.addToAfterProj (sub)
                }
            }
        }
        if (deep) {
            if (implied)
                prec.required++
            else
                prec.desired++
        }
        prec
    }

    def resolvePrecedents (Workspace wsp) {
        wsp.projects.values().each { proj ->
            proj.mpc?.precedents?.each { submpc ->
                Project prec = projectPrecedent(proj, submpc.owner, wsp)
                if (!proj?.mpc) {
                    print "proj or mpc is null"
                } else if (!prec?.mpc) {
                    print "prec or mpc is null"
                } else if (proj.mpc.name == prec.mpc.name) {
                    println "project ${proj.mpc.name} requires itself to be built"
                } else {
                    if (!proj.afterProj.find {it.mpc.name == prec.mpc.name}) {
                        proj.afterProj.add(prec)
                        prec.neededBy.add(proj)
                    }
                }
            }
            if ((i + 1) % 500 == 0)
                println("resolvePrecedents completed ${i + 1} iterations")
        }
    }

 }
