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

    TaoRelease rls

    def setup() {
        rls = new TaoRelease (["rlsVersion": "2.2a",
                               "lastPatch": 8, "patchsrc": true,
                               "basePath": "http://download.ociweb.com/",
                               "patchesPath": "http://download.ociweb.com/TAO-2.2a_patches"])
        TaoLegacyService.initPackages (rls, "tao22aFiles.json")
    }

    def cleanup() {
        rls = null
    }

    void "defkey creation"() {

        when:
        def key = TaoLegacyService.defKey()

        then:
        key == 1043
    }

    void "has 2.2a"() {

        when:
        def ver = rls.rlsVersion

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
        def plist = TaoLegacyService.patchlevelFor(rls)

        then:
        plist.size() == 6

    }

    void "getting content"() {
        when:
        def lev = TaoLegacyService.patchList.find {it.value == TaoLegacyService.LEVEL_PATCH}.name
        def plist = TaoLegacyService.contentFor(rls, [patchLevel:lev, changesLevel:"8"])

        then:
        plist.size() == 3

    }

    void "getting compression1"() {
        when:
        def lev = TaoLegacyService.patchList.find {it.value == TaoLegacyService.BASE_RELEASE}.name
        def con = TaoLegacyService.contentList.find {it.value == TaoLegacyService.SOURCE_ONLY}.name
        def plist = TaoLegacyService.compressFor(rls, [patchLevel:lev, changesLevel:"0", content:con])

        then:
        plist.size() == 3

    }
    void "getting compression2"() {
        when:
        def lev = TaoLegacyService.patchList.find {it.value == TaoLegacyService.JUMBO_PATCH}.name
        def con = TaoLegacyService.contentList.find {it.value == TaoLegacyService.SOURCE_AND_PROJECT}.name
        def plist = TaoLegacyService.compressFor(rls, [patchLevel:lev, changesLevel:"0", content:con])

        then:
        plist.size() == 2

    }

}
