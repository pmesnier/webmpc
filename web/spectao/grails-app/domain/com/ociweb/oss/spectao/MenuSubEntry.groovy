package com.ociweb.oss.spectao

/**
 * Created by phil on 3/29/16.
 */
class MenuSubEntry {
    String label
    MenuProjects projects
    static constraints = {
        label nullable: true
        projects nullable: true
    }
}
