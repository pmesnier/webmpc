package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/12/16.
 */
class webMpcService {

    static def loader = null
    static def jsonSlurper = new JsonSlurper()

    static collectionList = []
    static taoFeatureLibList = []
    static orbServiceClientList = []
    static orbServiceServerList = []

    def run_shell_command (String cmd) {
        cmd.execute().text
    }


    void invokeMPC (OciRelease rel, def params) {
        String cmd = ""
    }

    void initProduct (Product prod, def params) {
        String resourceInfo = params.activeInit
        if (resourceInfo) {
            if (loader == null)
                loader = prod.getClass().getClassLoader()
            def resource = loader.getResource(resourceInfo)
            def taoConfig = jsonSlurper.parse(resource)

            collectionList = taoConfig.collectionList;
            taoFeatureLibList = taoConfig.taoFeatureLibList;
            orbServiceClientList = taoConfig.orbServiceClientList;
            orbServiceServerList = taoConfig.orbServiceServerList;

            taoConfig.taoActive.each { rlsdef ->
                def rls =  prod.releases.find {
                    it.rlsVersion.equals(rlsdef.rlsVersion)
                }
                boolean addit = rls == null
                if (addit)
                    rls = new OciRelease (rlsdef)
                initPackages (rls, rlsdef.packageInit)
                if (addit)
                    prod.addToReleases (rls)
            }

        }
    }


}
