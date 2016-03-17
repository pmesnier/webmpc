package com.ociweb.oss.spectao

/**
 * Created by phil on 3/9/16.
 */
class MpcCategory {
    String name
    static hasMany=[group: MpcGroup]
    List group
}
