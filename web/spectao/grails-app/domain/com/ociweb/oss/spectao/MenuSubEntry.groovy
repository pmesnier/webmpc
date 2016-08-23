package com.ociweb.oss.spectao

/**
 * Created by phil on 3/29/16.
 */
class MenuSubEntry {
    String label
    static hasMany=[pickList: MenuPickList ]
    List pickList

    static constraints = {
        label nullable: true
        pickList nullable: true
    }
}
