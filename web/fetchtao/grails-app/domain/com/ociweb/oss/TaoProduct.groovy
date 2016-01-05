package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 12/15/15.
 */
class TaoProduct extends Product {

    def initRelease (params) {
        super.initRelease (params)

        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(params.releaseInit)
        def taoConfig = jsonSlurper.parse(resource)

        taoConfig.taoLegacy.each { rlsdef ->
            def rls =  new TaoRelease (rlsdef)
            rls.initPackages()
            addToReleases (rls)
        }

    }

}
