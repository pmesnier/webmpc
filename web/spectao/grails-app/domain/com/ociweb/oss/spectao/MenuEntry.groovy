package com.ociweb.oss.spectao

/**
 * Created by phil on 3/29/16.
 */
class MenuEntry {
    String label
    List subMenu
    static hasMany = [subMenu: MenuSubEntry]
    static constraints = {
        label nullable: true
        subMenu nullable: true
    }
}
