package com.ociweb.oss.spectao


class Workspace {
    String name
    static hasMany=[projects: Project]
    Map projects

    static constraints = {
        projects nullable:true
        name nullable:true
    }

 }
