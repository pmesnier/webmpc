package com.ociweb.oss.spectao


class MpcUnit {
    String name
    static belongsTo = [owner: MpcProject]
    String mpcpath
    List<String> after
    List<String> requires
    List<String> avoids

    static constraints = {
        mpcpath nullable:true
        after nullable:true
        requires nullable:true
        avoids nullable:true

    }

}
