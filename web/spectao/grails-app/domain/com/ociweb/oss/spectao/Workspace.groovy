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

    static transients=[ "productName", "wsimplied", "disabledFeatures", "enabledFeatures"]

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

    def getCheckedProjects (int pl) {
        def checks = []
        MenuPickList plist = currentSubset?.pickList?.get(pl)
        if (plist) {
            plist.options?.each () { pname ->
                Project prj = projects.find {
                    it?.mpc.name == pname
                }
                int des = prj?.desired ?: 0
                int req = prj?.required ?: 0
                checks.add ([name: pname, desired: des, required:req, checked: des > 0])
            }
        }
        checks
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
