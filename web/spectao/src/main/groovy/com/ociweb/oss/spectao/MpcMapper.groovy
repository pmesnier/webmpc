package com.ociweb.oss.spectao


class MpcMapper {
    def rawProjects = [:]
    def rawUnits = [:]
    def categories = []
    MpcCategory currentCategory
    MpcGroup currentGroup
    MpcSubset currentSubset

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
                if (name == null || name.length() == 0)
                    println "${mpath} gave an empty name"
                else if (!rawProjects.containsKey(name))
                    break

                slash = mpath.lastIndexOf('/', slash - 1)
            }
            if (name == null || name.length() == 0)
            {
                println "${mpath} bad using ${config.name}"

                name = config.name
            }

            container = new MpcProject([name: name, mpcpath: mpath])
            MpcProject dup = rawProjects.get(name)
            if (dup) {
                println "${name} is a duplicate key,\n   mpath = ${mpath}\n   dup = ${dup.mpcpath}"
            }
            rawProjects.put(name, container)
            currentSubset.addToMpcProjects(container)
            container.save(failOnError: true)
        }
        container.addToUnits (punit)
        rawUnits.put(config.name, punit)
        punit.save(failOnError: true)
        punit
    }

    MpcProject findProject (String name) {
        MpcProject p = rawProjects.get(name)
        if (!p) {
            p = rawUnits.get(name)?.owner
        }
        p
    }

    void resolveDependencies () {
        println "rawProjects map has ${rawProjects.size()} entries"
        println "rawUnits map has ${rawUnits.size()} entries"
        long coreTotal = 0
        MpcCategory coreCat = categories.find { if (it.name == "core") it}
        coreCat.group.each { grp ->
            coreTotal += grp.subset.size()
        }
        println "core group has ${coreTotal}"
        rawProjects.values().each { mproj ->
            mproj.precedents = []
            mproj.units.each { unit ->
                unit.after.each { proj ->
                    if (proj == mproj.name) {
                        println "project ${proj} includes itself as a precedent"
                    }
                    else if (!mproj.precedents.find {it.name == proj}) {
                        MpcUnit prec = rawUnits.get(proj)
                        if (!prec) {
                            println "project ${mproj.name} after proj ${proj} not found"
                        } else {
                            mproj.precedents.add(prec)
                        }
                    }
                }
            }
        }
    }
}
