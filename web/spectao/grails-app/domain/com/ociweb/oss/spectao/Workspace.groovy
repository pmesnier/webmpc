package com.ociweb.oss.spectao


class Workspace {
    static hasMany=[projects: Project, features: Feature]
//    Map required
    Date lastUpdated
    List<Project> projects
    List<Feature> features
    String name
    MpcProduct product
    MenuSubEntry currentSubset
    List<String> desiredProject
    List<String> impliedProject
    List<String> buildType
    String archiveType
    boolean includeSource

    static transients=[ "productName", "wsimplied", "disabledFeatures", "enabledFeatures", "clientChecks", "serverChecks"]

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

    String getProductName () {
        "${product.name ?: "OCITAO"} version ${product.revision ?: "2.2a"}"
    }

    def getClientChecks () {
        def checks = []
        if (currentSubset) {
            currentSubset.projects.client?.each { pname ->
                if (pname.startsWith('%') && pname.endsWith('%')) {
                    checks.add ([name: pname.substring(1,pname.length()-1), isLabel:true, checked: false])
                }
                else {
                    boolean selected = projects.find { it.mpc.name == pname } != null
                    checks.add([name: pname, isLabel: false, checked: selected])
                }
            }

        }
        return checks
    }

    def getServerChecks () {
        def checks = []
        if (currentSubset) {
            currentSubset.projects.server?.each { pname ->
                if (pname.startsWith('%') && pname.endsWith('%')) {
                    checks.add ([name: pname.substring(1,pname.length()-1), isLabel:true, checked: false])
                }
                else {
                    boolean selected = projects.find { it.mpc.name == pname } != null
                    checks.add([name: pname, isLabel: false, checked: selected])
                }
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
