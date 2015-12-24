package com.ociweb.fetchtao

import com.ociweb.oss.Product

/**
 * Created by phil on 12/15/15.
 */
class TaoProduct extends Product {

    def initRelease (params) {
        super.initRelease (params)
        def ossRelease = []

        ossRelease << [rlsVersion: "1.2a", lastPatch: 12, patchsrc: false, basePath: "http://download.ociweb.com/TAO-1.2a", patchesPath: "http://download.ociweb.com/TAO-1.2a_patches"]
        ossRelease << [rlsVersion: "1.3a", lastPatch: 18, patchsrc: false, basePath: "http://download.ociweb.com/TAO-1.3a", patchesPath: "http://download.ociweb.com/TAO-1.3a_patches"]
        ossRelease << [rlsVersion: "1.4a", lastPatch: 26, patchsrc: false, basePath: "http://download.ociweb.com/TAO-1.4a", patchesPath: "http://download.ociweb.com/TAO-1.4a_patches"]
        ossRelease << [rlsVersion: "1.5a", lastPatch: 22, patchsrc: false, basePath: "http://download.ociweb.com/TAO-1.5a", patchesPath: "http://download.ociweb.com/TAO-1.5a_patches"]
        ossRelease << [rlsVersion: "1.6a", lastPatch: 15, patchsrc: true, basePath: "http://download.ociweb.com/TAO-1.6a", patchesPath: "http://download.ociweb.com/TAO-1.6a_patches"]
        ossRelease << [rlsVersion: "2.0a", lastPatch: 7, patchsrc: true, basePath: "http://download.ociweb.com/TAO-2.0a", patchesPath: "http://download.ociweb.com/TAO-2.0a_patches"]
        ossRelease << [rlsVersion: "2.2a", lastPatch: 8, patchsrc: true, basePath: "http://download.ociweb.com/TAO-2.2a", patchesPath: "http://download.ociweb.com/TAO-2.2a_patches"]

        ossRelease.each { rlsdef ->
            def rls =  new TaoRelease (rlsdef)
            rls.initPackages()
            addToReleases (rls)
        }

    }


}
