package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/12/16.
 */
class TaoActiveService {

    static def loader = null
    static def jsonSlurper = new JsonSlurper()

    String ACE_ROOT = "/tao_builds/phil/ocitao/tao22a/build/native/OCI"
    String TAO_ROOT
    String MWC_CMD

    static def run_shell_command (String cmd) {
        cmd.execute().text
    }


    static invokeMPC (TaoRelease rel, def params) {
        String cmd = ""
    }

    static initProduct (Product prod, def params) {
        String resourceInfo = params.activeInit
        if (resourceInfo) {
            if (loader == null)
                loader = prod.getClass().getClassLoader()
            def resource = loader.getResource(resourceInfo)
            def taoConfig = jsonSlurper.parse(resource)

            taoConfig.taoLegacy.each { rlsdef ->
                def rls =  new TaoRelease (rlsdef)
                initPackages (rls, rlsdef.packageInit)
                prod.addToReleases (rls)
            }

        }
    }

}
