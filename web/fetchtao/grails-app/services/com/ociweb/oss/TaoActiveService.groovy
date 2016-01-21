package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/12/16.
 */
class TaoActiveService {

    static def loader = null
    static def jsonSlurper = new JsonSlurper()

    static collectionList = []
    static taoFeatureLibList = []
    static orbServiceClientList = []
    static orbServiceServerList = []

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
                    rls = new TaoRelease (rlsdef)
                initPackages (rls, rlsdef.packageInit)
                if (addit)
                    prod.addToReleases (rls)
            }

        }
    }

    static String genkey (TaoActivePackage pkg) {
        long svcKey = 0
        pkg.components.each{ cmp ->
            services += taoComponentFindName(cmp).value
        }
    }

    static void initPackages (def rls, def params) {
        String key = "dummy"
        def tap = new TaoActivePackage(params)
        tap.release = rls
        tap.env = params.env
        rls.active.put (key, tap)
    }

}
