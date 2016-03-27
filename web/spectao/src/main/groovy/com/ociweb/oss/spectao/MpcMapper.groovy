package com.ociweb.oss.spectao

import groovy.json.JsonSlurper


class MpcMapper {
    def jsonSlurper = new JsonSlurper()
    def categories = []
    MpcProduct currentProduct
    MpcCategory currentCategory
    MpcGroup currentGroup
    MpcSubset currentSubset

    void initAll(def initresource) {
        def mpcprojects = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.sources))

        currentProduct = new MpcProduct (initresource.product)
        currentProduct.rawUnits = [:]
        currentProduct.rawProjects = [:]

        println "core size = ${mpcprojects.core.size()}"
        println "tests size = ${mpcprojects.tests.size()}"
        println "examples size = ${mpcprojects.examples.size()}"

        mpcprojects.each { cat ->
            println "category ${cat.key} has ${cat.value.size()} elements"
            addCategory(cat.key)
            cat.value.each { gconf ->
                MpcGroup group = addGroup(gconf.key)
                gconf.value.each { ssconf ->
                    MpcSubset subset = addSubset(ssconf)
                    ssconf.fileRoot.each { directory ->
                        def dir = getClass().getClassLoader().getResource(directory)
                        def params = [:]
                        params << [groupName: group.name, subName: subset.alias]
                        if (dir.toString().endsWith(".json"))
                            addProjectDef(params, dir)
                        else
                            loadProjects(params, dir)
                    }
                }
            }
        }

        def feat = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.features))?.features
        feat.each {
            it << [needsOn : [], needsOff : []]
            MpcFeature mf = new MpcFeature (it)
            mf.save()
        }
        currentProduct.availableBuildTypes = jsonSlurper.parse(getClass().getClassLoader().getResource(initresource.types))?.buildType
        currentProduct.save ()

        println "found ${MpcFeature.list().size()} features and ${currentProduct.availableBuildTypes.size()} build types"
        resolveDependencies()
    }

    void addProjectDef(def params, def res) {
        def projdef = jsonSlurper.parse(res).project
        if (projdef) {
            def inherit = projdef.inheritance
            projdef << [mpcpath: inherit ? inherit.get(projdef.name) : "undef"]
            projdef << params
            addProjectUnit(projdef)
        }
    }

    void loadProjects(def params, def dir) {
        File dirFile = null;

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

        dirFile?.listFiles().each { entry ->
            if (entry.getName() != "." && entry.getName() != "..") {
                if (entry.isDirectory()) {
                    loadProjects(params, entry);
                } else if (entry.getName().endsWith(".json")) {
                    addProjectDef(params, entry)
                }
            }
        }
    }


    def addCategory(String name) {
        currentCategory = new MpcCategory (name:name)
        currentCategory.group = []
        categories.add ( currentCategory)
        currentCategory.save (failOnError: true)
        currentCategory
    }

    def addGroup (String name) {
        currentGroup = new MpcGroup (name: name)
        currentGroup.subset = []
        currentCategory.addToGroup (currentGroup)
        currentGroup.save(failOnError: true)
        currentGroup
    }

    def addSubset ( def params) {
        currentSubset = new MpcSubset(params)
        currentSubset.mpcProjects = []
        currentGroup.addToSubset (currentSubset)
        currentSubset.save(failOnError: true)
        currentSubset
    }

    def addProjectUnit (def config ) {
        MpcUnit punit = new MpcUnit (config)
        MpcProject container = currentSubset.mpcProjects.find { it.mpcpath == config.mpcpath }
        if (!container) {
            String mpath = config.mpcpath
            int slash = mpath.lastIndexOf('/')
            int ext = mpath.lastIndexOf('.')
            String name
            if (ext == -1) {
                if (slash == mpath.length() - 1) {
                    ext = slash
                    slash = mpath.lastIndexOf('/', slash - 1)
                } else {
                    ext = mpath.length()
                }
            }
            while (slash > -1) {
                name = mpath.substring(slash + 1, ext)
                if (!name)
                    println "${mpath} gave an empty name"
                else if (!currentProduct.rawProjects.containsKey(name))
                    break

                slash = mpath.lastIndexOf('/', slash - 1)
            }
            if (!name)
            {
                println "${mpath} bad using ${config.name}"

                name = config.name
            }

            container = new MpcProject([name: name, mpcpath: mpath, projectRequires: [], projectAvoids: []])
            MpcProject dup = currentProduct.rawProjects.get(name)
            if (dup) {
                println "${name} is a duplicate key,\n   mpath = ${mpath}\n   dup = ${dup.mpcpath}"
            }
            currentProduct.rawProjects.put(name, container)
            currentSubset.addToMpcProjects(container)
            container.save(failOnError: true)
        }
        container.addToUnits (punit)
        punit.requires?.each {
            if (!container.projectRequires.contains (it)) {
                container.projectRequires.add (it)
            }
        }

        punit.avoids?.each {
            if (!container.projectAvoids.contains (it)) {
                container.projectAvoids.add (it)
            }
        }

        currentProduct.rawUnits.put(config.name, punit)
        punit.save(failOnError: true)
        punit
    }

    void resolveDependencies () {
        println "rawProjects map has ${currentProduct.rawProjects.size()} entries"
        println "rawUnits map has ${currentProduct.rawUnits.size()} entries"
        long coreTotal = 0
        MpcCategory coreCat = categories.find { if (it.name == "core") it}
        coreCat.group.each { grp ->
            coreTotal += grp.subset.size()
        }
        println "core group has ${coreTotal}"
        currentProduct.rawProjects.values().each { mproj ->
            mproj.precedents = []
            mproj.units.each { unit ->
                unit.after.each { proj ->
                    if (proj == mproj.name) {
                        println "project ${proj} includes itself as a precedent"
                    }
                    else if (!mproj.precedents.find {it.name == proj}) {
                        MpcUnit prec = currentProduct.rawUnits.get(proj)
                        if (!prec) {
                            println "project ${mproj.name} after proj ${proj} not found"
                        } else {
                            mproj.precedents.add(prec)
                        }
                    }
                }
                unit.requires?.each { urec ->
                    MpcFeature mf = MpcFeature.list().find {it.name == urec}
                    if (mf == null) {
                        println "looking for feature ${urec} on behalf of ${mproj.name} unit ${unit.name} found nothing"
                        mf = new MpcFeature ([name: urec, defState:"0", needsOn: [], needsOff: []])
                        mf.save()
                    }
                    if (!mf.needsOn.contains (mproj.name))
                        mf?.needsOn.add(mproj.name)
                }
                unit.avoids?.each { uavo ->
                    MpcFeature mf = MpcFeature.list().find {it.name == uavo}
                    if (mf == null) {
                        println "looking for feature ${uavo} to avoid on behalf of ${mproj.name} unit ${unit.name} found nothing"
                        mf = new MpcFeature ([name: urec, defState:"0", needsOn: [], needsOff: []])
                        mf.save()
                    }
                    else if (!mf?.needsOff.contains (mproj.name))
                        mf?.needsOff.add(mproj.name)
                }
            }
        }
    }
}
