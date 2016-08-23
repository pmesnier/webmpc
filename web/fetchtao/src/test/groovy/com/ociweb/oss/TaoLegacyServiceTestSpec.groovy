package com.ociweb.oss

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(OciService)
@TestMixin(GrailsUnitTestMixin)
@Mock([OciSelectorInfo, OciRelease, OciAsset])
class TaoLegacyServiceTestSpec extends Specification {

    OciRelease rls22a
    OciRelease rls12a

    def ociService
    def loader

    def setup() {
        ociService = new OciService()
        loader = ociService.getClass().getClassLoader()
        rls22a = new OciRelease (["rlsVersion" : "2.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-2.2a_patches"])
        def resource = loader.getResource("tao22aFiles.json")
        ociService.initAssets ("Tao", rls22a, resource)
        rls12a = new OciRelease (["rlsVersion" : "1.2a",
                                  "lastPatch"  : 8, "patchsrc": true,
                                  "basePath"   : "http://download.ociweb.com/",
                                  "patchesPath": "http://download.ociweb.com/TAO-1.2a_patches"])
        resource = loader.getResource("tao12aFiles.json")
        ociService.initAssets ("Tao", rls12a, resource)
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
        OciSelectorInfo info = rls22a.plList[0]
        String key = ociService.targetKey(info, 0)

        then:
        key == "C:SRC.P:BASE.L:0"
    }

    void "getting patchlevel"() {
        when:
        def plist = rls22a.plList

        then:
        plist.size() == 13

    }

    void "getting content"() {
        when:
        int pnum = 8
        def lev = OciPatchBase.LEVL
        String key = "P:${lev}.L:${pnum}"

        def plev = rls22a.plList.find() {p ->
            key.equals(ociService.targetKey(p))
        }

        then:
        plev?.contentKind.size() == 3
    }

    void "getting content 12a"() {
        when:
        int pnum = 0
        def lev = OciPatchBase.BASE
        String key = "P:${lev}.L:${pnum}"

        def plev = rls22a.plList.find() {p ->
            key.equals(ociService.targetKey(p))
        }

        then:
        plev?.contentKind.size() == 2
    }

    void "find doxy file"() {
        when:
        def lev = OciPatchBase.DGEN
        def con = OciContent.DOX
        def cmp = OciCompress.TGZ
        String filename = "TAO-2.2a/ACE+TAO-2.2a_dox.tar.gz"
        String testKey = "Tao.C:${con}.P:${lev}.L:0.Z:${cmp}"

        OciAsset pkg = rls22a.legacy.find { it.key.equals(testKey) }

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
