package com.ociweb.oss.spectao


class Project {
    static belongsTo = [workspace : Workspace]

    List<Project> afterProj
    List<Project> neededBy

    MpcProject mpc
    int desired
    int required

    static constraints = {
        afterProj nullable:true
        neededBy nullable:true
        mpc nullable:true
    }

}
