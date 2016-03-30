package com.ociweb.oss.spectao

/**
 * Created by phil on 3/26/16.
 */
class MpcProduct {
    String name
    String revision
    static hasMany = [rawProjects : MpcProject, rawUnits : MpcUnit]
    Map rawProjects;
    Map rawUnits;

    List<String> availableBuildTypes
    List<String> availableArchiveTypes
    List<MenuEntry> menu

    static constraints = {
        rawProjects nullable:true
        rawUnits nullable:true
        availableBuildTypes nullable:true
        availableArchiveTypes nullable:true
        menu nullable:true
    }
}
