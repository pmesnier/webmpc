package com.ociweb.oss.spectao

import groovy.json.JsonSlurper


class MpcMapper {
    def jsonSlurper
    def categories
    MpcProduct currentProduct
    MpcCategory currentCategory
    MpcGroup currentGroup
    MpcSubset currentSubset
    String rootPath
    ClassLoader loader

    MpcMapper () {
        jsonSlurper = new JsonSlurper()
        categories = []
        currentProduct = null
        currentCategory = null
        currentGroup = null
        currentSubset = null
        rootPath = null
        loader = getClass().getClassLoader()
    }

    def getConfigFromResource(String path) {
        def resource = loader.getResource(path) ?: loader.getResource("${rootPath}/${path}")
        def config = [:]
        if (resource)
            config = jsonSlurper.parse(resource)
    }

    void initAll(def initresource) {
        rootPath = initresource.rootDirectory
        currentProduct = initProduct(initresource)
        currentProduct.rawUnits = [:]
        currentProduct.rawProjects = [:]
        def mpcprojects = getConfigFromResource(initresource.sources)

        if (mpcprojects.size == 0) {
            println "unable to load config from ${initresource.sources}"
            return
        }

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
                        def dir = loader.getResource(directory)
                        def params = [:]
                        params << [groupName: group.name, subName: subset.alias]
                        if (dir.toString().endsWith(".json"))
                            addProjectDef(params, dir)
                        else
                            loadProjects(params, dir,[])
                    }
                }
            }
        }

        def feat = getConfigFromResource (initresource.features)?.features
        feat.each {
            it << [needsOn : [], needsOff : []]
            MpcFeature mf = new MpcFeature (it)
            mf.save()
        }
        currentProduct.availableBuildTypes = getConfigFromResource(initresource.types)?.buildType
        currentProduct.menu.each { entry ->
            entry.subMenu.each { sub ->
                sub.save (failOnError: true)
            }
            entry.save (failOnError: true)
        }
        currentProduct.save (failOnError: true)

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

    void loadProjects(def params, def dir, def exclude) {
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
            String name = entry.getName()
            if (name != "." && name != ".." && !exclude.contains(name)) {
                if (entry.isDirectory()) {
                    loadProjects(params, entry, []);
                } else if (name.endsWith(".json")) {
                    addProjectDef(params, entry)
                }
            }
        }
    }

    def initProduct (def config) {
        MpcProduct product = new MpcProduct (config)
       config.menu_source.each { entryConf ->
            MenuEntry me = new MenuEntry (label : entryConf.label)
            entryConf.subMenu.each { sub ->
                MenuSubEntry ms = new MenuSubEntry(label: sub.label)
                ms.projects = new MenuProjects(server: sub.projects.server?:[], client: sub.projects.client?:[]);
                ms.projects.save(failOnError: true)
                ms.save(failOnError: true)
                me.addToSubMenu(ms)
            }
           me.save (failOnError: true)
           product.addToMenu (me)
        }
        product.save (failOnError: true)
        product
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

//        File prjdump = new File ("rawProj.txt")
//        prjdump.write "Raw project list with ${currentProduct.rawProjects.size()} entries:\n"
//        currentProduct.rawProjects.each { n, v ->
//            prjdump.append "name: ${n} path: ${v.mpcpath}\n"
//        }

        long coreTotal = 0
        MpcCategory coreCat = categories.find { if (it.name == "core") it}
        coreCat.group.each { grp ->
            coreTotal += grp.subset.size()
        }
        println "core group has ${coreTotal}"
        currentProduct.rawProjects.values().each { mproj ->
            mproj.precedents = []
            mproj.units.each { unit ->
                unit.after?.each { proj ->
                    if (proj != mproj.name && !mproj.precedents.find {it.name == proj}) {
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
                        println "adding implied feature ${urec}"
                        mf = new MpcFeature ([name: urec, defState:"1", needsOn: [], needsOff: []])
                        mf.save()
                    }
                    if (!mf.needsOn.contains (mproj.name))
                        mf?.needsOn.add(mproj.name)
                }
                unit.avoids?.each { uavo ->
                    MpcFeature mf = MpcFeature.list().find {it.name == uavo}
                    if (mf == null) {
                        println "adding implied feature ${uavo}"
                        mf = new MpcFeature ([name: uavo, defState:"1", needsOn: [], needsOff: []])
                        mf.save()
                    }
                    else if (!mf?.needsOff.contains (mproj.name))
                        mf?.needsOff.add(mproj.name)
                }
            }
        }
    }
}
