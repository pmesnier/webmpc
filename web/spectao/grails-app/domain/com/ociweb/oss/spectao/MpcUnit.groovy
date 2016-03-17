package com.ociweb.oss.spectao

/**
 * Created by phil on 3/14/16.
 */
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
