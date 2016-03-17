package com.ociweb.oss.spectao

/**
 * Created by phil on 3/9/16.
 */
class MpcProject {
    static belongsTo = [subset : MpcSubset]
    static hasMany = [units : MpcUnit]
    List units

    String name
    String mpcpath
    List<MpcUnit> precedents
    static transients = ["precedents"]
    static constraints = {
        //precedents nullable:true
     }

    String getLongName() {
        "${subset.group.name}/${subset.alias}/${name}"
    }
}
