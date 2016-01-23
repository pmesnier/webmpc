package com.ociweb.oss

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(OciAssetService)
@TestMixin(GrailsUnitTestMixin)
class TaoLegacyServiceTestSpec extends Specification {

    OciRelease rls22a
    OciRelease rls12a

    def setup() {
        rls22a = new OciRelease (["rlsVersion" : "2.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-2.2a_patches"])
        OciAssetService.initAssets (rls22a, "tao22aFiles.json")
        rls12a = new OciRelease (["rlsVersion" : "1.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-1.2a_patches"])
        OciAssetService.initAssets (rls12a, "tao12aFiles.json")
    }

    def cleanup() {
        rls22a = null
    }

    void "defkey creation"() {

        when:
        def key = OciAssetService.defKey()

        then:
        key == 1044
    }

    void "has 2.2a"() {

        when:
        def ver = rls22a.rlsVersion

        then:
        ver.equals ("2.2a")
    }

    void "verify genkey"() {
        when:
        int key = OciAssetService.genKey("TAO-2.2a/ACE+TAO-2.2a_with_latest_patches_NO_makefiles.tar.gz", 0)

        then:
        key == OciAssetService.FULL_LATEST_RELEASE + OciAssetService.SOURCE_ONLY + OciAssetService.TGZ
    }

    void "getting patchlevel"() {
        when:
        def plist = OciAssetService.patchlevelFor(rls22a)

        then:
        plist.size() == 13

    }

    void "getting content"() {
        when:
        int pnum = 8
        def lev = OciAssetService.LEVEL_PATCH + pnum * OciAssetService.LEVEL_SHIFT
        def plist = OciAssetService.contentFor(rls22a, [patchLevel:lev])

        then:
        plist.size() == 3
    }

    void "getting content 12a"() {
        when:
        int pnum = 8
        def lev = OciAssetService.LEVEL_PATCH + pnum * OciAssetService.LEVEL_SHIFT
        def plist = OciAssetService.contentFor(rls12a, [patchLevel:lev])

        then:
        plist.size() == 1
    }

    void "getting compression1"() {
        when:
        def lev = OciAssetService.BASE_RELEASE
        def con = OciAssetService.SOURCE_ONLY
        def plist = OciAssetService.compressFor(rls22a, [patchLevel:lev, content:con])

        then:
        plist.size() == 3

    }
    void "getting compression2"() {
        when:
        def lev = OciAssetService.JUMBO_PATCH
        def con = OciAssetService.SOURCE_AND_PROJECT
        def plist = OciAssetService.compressFor(rls22a, [patchLevel:lev, content:con])

        then:
        plist.size() == 2

    }

    void "find doxy file"() {
        when:
        def lev = OciAssetService.DOXYGEN
        def con = OciAssetService.DOXYGEN_GENERATED
        def cmp = OciAssetService.TGZ
        String filename = "TAO-2.2a/ACE+TAO-2.2a_dox.tar.gz"
        def key = OciAssetService.genKey(filename, 0)
        println "key = " + key
        OciAsset pkg = OciAssetService.target(rls22a, [patchLevel: lev, content: con, compress: cmp ])

        then:
        pkg.targetName.equals(filename)

    }

}
