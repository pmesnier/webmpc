package com.ociweb.oss.spectao

/**
 * Created by phil on 3/7/16.
 */
class MpcSubset {
    static belongsTo = [group: MpcGroup]
    static hasMany = [mpcProjects : MpcProject]
    List mpcProjects

    boolean displayOnTop
    String alias
    //def fileRoot
}
