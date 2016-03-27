package com.ociweb.oss.spectao

class MpcFeature {
    String name
    String defState // "1" "0" "comment"
    List<String> needsOn
    List<String> needsOff

    static constraints = {
        needsOn nullable: true
        needsOff nullable: true
    }
}
