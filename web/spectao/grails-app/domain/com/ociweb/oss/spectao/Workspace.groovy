package com.ociweb.oss.spectao


class Workspace {
    static hasMany=[projects: Project, features: Feature]
//    Map required
    List<Project> projects
    List<Feature> features
    String name
    MpcProduct product
    MpcSubset currentSubset
    List<String> desiredProject
    List<String> impliedProject
    List<String> buildType
    String archiveType

    static transients=[ "productName", "columns", "wsimplied", "disabledFeatures", "enabledFeatures", "checklist"]

    static constraints = {
        projects nullable:true
        name nullable:true
        features nullable:true
        currentSubset nullable:true
        desiredProject nullable:true
        impliedProject nullable:true
        buildType nullable:true
        archiveType nullable:true
    }

    int getColumns () {
        3
    }

    String getProductName () {
        "${product.name ?: "OCITAO"} version ${product.revision ?: "2.2a"}"
    }

    def getChecklist () {
        def checks = null
        if (currentSubset) {
            checks = []
            currentSubset.mpcProjects.each { mpc ->
                 boolean selected = projects.find {it.mpc == mpc} != null
                checks.add ([name: mpc.name, checked: selected])
            }

        }
        return checks
    }

    def getDisabledFeatures () {
        def result = []
        features.each {
            if (it.isComment ) {
                result.add ("- ${it.mpcFeature.name} -")
            }
            else if (!it.enabled) {
                result.add (it.mpcFeature.name)
            }
        }
        result
    }

    def getEnabledFeatures () {
        def result = []
        features.each {
            if (it.isComment ) {
                result.add ("- ${it.mpcFeature.name} -")
            }
            else if (it.enabled) {
                result.add (it.mpcFeature.name)
            }
        }
        result
    }

    String getWsuser () {
        String spec = ""
        desiredProject.each { des ->
            spec += des
            def disabled = projects.find {it.mpc.name == des}?.disabledBy
            if (disabled) {
                spec += " -disabled by features\n  "
                disabled.eachWithIndex { dis, i -> spec += "${dis}${i < disabled.size() -1 ? ',' : ' '}" }
            }
            spec += "\n"
        }
        spec
    }

    String getWsimplied () {
        String spec = ""
        impliedProject.each { des ->
            spec += des
            def disabled = projects.find {it.mpc.name == des}?.disabledBy
            if (disabled) {
                spec += " -disabled by features\n  "
                disabled.eachWithIndex { dis, i -> spec += "${dis}${i < disabled.size() -1 ? ',' : ' '}" }
            }
            spec += "\n"

        }
        spec
    }

 }
