package com.ociweb.oss

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(TaoLegacyService)
@TestMixin(GrailsUnitTestMixin)
class TaoLegacyServiceTestSpec extends Specification {

    TaoRelease rls22a
    TaoRelease rls12a

    def setup() {
        rls22a = new TaoRelease (["rlsVersion" : "2.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-2.2a_patches"])
        TaoLegacyService.initPackages (rls22a, "tao22aFiles.json")
        rls12a = new TaoRelease (["rlsVersion" : "1.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-1.2a_patches"])
        TaoLegacyService.initPackages (rls12a, "tao12aFiles.json")
    }

    def cleanup() {
        rls22a = null
    }

    void "defkey creation"() {

        when:
        def key = TaoLegacyService.defKey()

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
        int key = TaoLegacyService.genKey("TAO-2.2a/ACE+TAO-2.2a_with_latest_patches_NO_makefiles.tar.gz", 0)

        then:
        key == TaoLegacyService.FULL_LATEST_RELEASE + TaoLegacyService.SOURCE_ONLY + TaoLegacyService.TGZ
    }

    void "getting patchlevel"() {
        when:
        def plist = TaoLegacyService.patchlevelFor(rls22a)

        then:
        plist.size() == 13

    }

    void "getting content"() {
        when:
        int pnum = 8
        def lev = TaoLegacyService.LEVEL_PATCH + pnum * TaoLegacyService.LEVEL_SHIFT
        def plist = TaoLegacyService.contentFor(rls22a, [patchLevel:lev])

        then:
        plist.size() == 3
    }

    void "getting content 12a"() {
        when:
        int pnum = 8
        def lev = TaoLegacyService.LEVEL_PATCH + pnum * TaoLegacyService.LEVEL_SHIFT
        def plist = TaoLegacyService.contentFor(rls12a, [patchLevel:lev])

        then:
        plist.size() == 1
    }

    void "getting compression1"() {
        when:
        def lev = TaoLegacyService.BASE_RELEASE
        def con = TaoLegacyService.SOURCE_ONLY
        def plist = TaoLegacyService.compressFor(rls22a, [patchLevel:lev, content:con])

        then:
        plist.size() == 3

    }
    void "getting compression2"() {
        when:
        def lev = TaoLegacyService.JUMBO_PATCH
        def con = TaoLegacyService.SOURCE_AND_PROJECT
        def plist = TaoLegacyService.compressFor(rls22a, [patchLevel:lev, content:con])

        then:
        plist.size() == 2

    }

    void "find doxy file"() {
        when:
        def lev = TaoLegacyService.DOXYGEN
        def con = TaoLegacyService.DOXYGEN_GENERATED
        def cmp = TaoLegacyService.TGZ
        String filename = "TAO-2.2a/ACE+TAO-2.2a_dox.tar.gz"
        def key = TaoLegacyService.genKey(filename, 0)
        println "key = " + key
        TaoLegacyPackage pkg = TaoLegacyService.target(rls22a, [patchLevel: lev, content: con, compress: cmp ])

        then:
        pkg.targetName.equals(filename)

    }

}
