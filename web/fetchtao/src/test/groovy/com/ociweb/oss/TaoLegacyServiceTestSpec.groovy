package com.ociweb.oss

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(OciService)
@TestMixin(GrailsUnitTestMixin)
class TaoLegacyServiceTestSpec extends Specification {

    OciRelease rls22a
    OciRelease rls12a

    def ociService

    def setup() {
        rls22a = new OciRelease (["rlsVersion" : "2.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-2.2a_patches"])
        ociService.initAssets (rls22a, "tao22aFiles.json")
        rls12a = new OciRelease (["rlsVersion" : "1.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-1.2a_patches"])
        ociService.initAssets (rls12a, "tao12aFiles.json")
    }

    def cleanup() {
        rls22a = null
    }

    void "has 2.2a"() {

        when:
        def ver = rls22a.rlsVersion

        then:
        ver.equals ("2.2a")
    }

    void "verify genkey"() {
        when:
        String key = ociService.genTaoKey("TAO-2.2a/ACE+TAO-2.2a_with_latest_patches_NO_makefiles.tar.gz", 0)

        then:
        key == "SRC.LTST.TGZ"
    }

    void "getting patchlevel"() {
        when:
        def plist = ociService.patchlevelFor(rls22a)

        then:
        plist.size() == 13

    }

    void "getting content"() {
        when:
        int pnum = 8
        def lev = OciPatchBase.LEVL
        //def plist = ociService.contentFor(rls22a, [patchLevel:lev])

        then:
        false //  plist.size() == 3
    }

    void "getting content 12a"() {
        when:
        int pnum = 8
        def lev = OciPatchBase.LEVL
        //def plist = ociService.contentFor(rls12a, [patchLevel:lev])

        then:
        false // plist.size() == 1
    }

    void "find doxy file"() {
        when:
        def lev = OciPatchBase.DGEN
        def con = OciContent.DOX
        def cmp = OciCompress.TGZ
        String filename = "TAO-2.2a/ACE+TAO-2.2a_dox.tar.gz"
        String key = "Tao.C:${con}.P:${lev}.L:0.Z:${cmp}"

        println "key = " + key
        def pkg = rls22a.legacy.get (key)

        then:
        pkg.filePath == filename
    }

    void "find release notes"() {
        when:
        def rnote1 = rls12a.relNotesPath
        def rnote2 = rls22a.relNotesPath

        then:
        rnote1 && rnote2
    }

}
