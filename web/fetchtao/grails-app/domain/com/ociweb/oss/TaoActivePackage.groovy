package com.ociweb.oss

/**
 * Created by phil on 1/14/16.
 */
class TaoActivePackage {
    static belongsTo = [release: TaoRelease]
    TaoRelease release

    def attributes = [:]
    def components = []
    def platforms = []

    String cache
    String workspace
    String source
    String vcs
    String repourl
    def env

    static constraints = {
        cache nullable:true
        workspace nullable:true
        source nullable:true
        vcs nullable:true
        repourl nullable:true
        env nullable:true

    }

}
