package com.ociweb.oss.spectao

/**
 * Created by phil on 3/29/16.
 */
class MenuProjects {
    List<String> server
    List<String> client
    static constraints = {
        server nullable: true
        client nullable: true
    }
}
