package com.ociweb.oss.spectao


class Workspace {
    static hasMany=[required: Project, features: Feature]
    Map required
    List features
    String name
    MpcSubset currentSubset
    List<String> desiredProject
    List<String> impliedProject
    List checklist

    static transients=["wsuser", "wsimplied", "disabledFeatures", "enabledFeatures", "checklist"]

    static constraints = {
        required nullable:true
        name nullable:true
        features nullable:true
        currentSubset nullable:true
        desiredProject nullable:true
        impliedProject nullable:true
    }

    def getdisabledFeatures () {
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

    def getenabledFeatures () {
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

    String getwsuser () {
        String spec = ""
        desiredProject.each {
            spec += "${it}\n"
        }
        spec
    }
    String getwsimplied () {
        String spec = ""
        impliedProject.each {
            spec += "${it}\n"
        }
        spec
    }

 }
