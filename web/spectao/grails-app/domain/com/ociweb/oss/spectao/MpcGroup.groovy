package com.ociweb.oss.spectao

class MpcGroup {
    static belongsTo = [category: MpcCategory]
    static hasMany = [subset: MpcSubset]
    List subset

    String name
}
