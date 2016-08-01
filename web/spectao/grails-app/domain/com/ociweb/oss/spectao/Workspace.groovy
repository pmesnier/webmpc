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

    private  def getFeatureSets (boolean wantEnabled) {
        def result = []
        String title = "General features"
        def subset = [:]
        def items = []
        features.each {
            if (it.isComment) {
                subset = [title: title, items: items]
                result.add (subset)
                title = it.mpcFeature.name
                items = []
            }
            else if (it.enabled == wantEnabled) {
                items.add([name: it.mpcFeature.name, checked: false])
            }
        }
        subset = [title: title, items: items]
        result.add (subset)

        result
    }

    def getDisabledFeatures () {
        getFeatureSets (false)
    }

    def getEnabledFeatures () {
        getFeatureSets (true)
    }

    def getWsuser () {
        List<String> coll = new ArrayList<String>()
        desiredProject.each { des ->
            String spec = des
            def disabled = projects.find {it.mpc.name == des}?.disabledBy
            if (disabled) {
                spec += " -disabled by features "
                disabled.eachWithIndex { dis, i -> spec += "${dis}${i < disabled.size() -1 ? ',' : ' '}" }
            }
            coll.add(spec)
        }
        coll
    }

    def getWsimplied () {
        List<String> coll = new ArrayList<String>()
        impliedProject.each { des ->
            String spec = des
            def disabled = projects.find {it.mpc.name == des}?.disabledBy
            if (disabled) {
                spec += " -disabled by features "
                disabled.eachWithIndex { dis, i -> spec += "${dis}${i < disabled.size() -1 ? ',' : ' '}" }
            }
            coll.add(spec)
        }
        coll
    }

 }
