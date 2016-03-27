package com.ociweb.oss

import groovy.json.JsonSlurper
import groovy.util.XmlSlurper

class OciService {
    def formatter = new Formatter()

    def loader = null
    def jsonSlurper = new JsonSlurper()
    def xmlSlurper = new XmlSlurper()


    //------------------------------------ OciProduct specific functions --------------------------------------

    void initProduct (OciProduct prod, def params) {
        String resourceInfo = params.ociReleaseInit
        if (resourceInfo) {
            if (loader == null)
                loader = prod.getClass().getClassLoader()
            def resource = loader.getResource(resourceInfo)
            def prodInfo = jsonSlurper.parse(resource)

            prodInfo.releases.each { rlsdef ->
                def rls = new OciRelease (rlsdef)
                //productService.initRelease (prod, rls)
                prod.addToReleases (rls)
                if (rlsdef.assetInit) {
                    resource = loader.getResource(rlsdef.assetInit)
                    initAssets (prod.name, rls, resource)
                }
                if (rlsdef.testRlsNote) {
                    resource = loader.getResource(rlsdef.testRlsNote)
                    if (resource)
                        loadReleaseNote (rls, resource)
                }
            }
        }
    }

    //------------------------------------ OciRelease specific functions --------------------------------------

    int compareRelease (Release member, Release test) {
        if (member instanceof OciRelease && test instanceof OciRelease) {
            if (member.major != test.major) {
                return member.major > test.major ? 1 : -1
            }
            else if (member.minor != test.minor) {
                return member.minor > test.minor ? 1 : -1
            }
            else if (member.micro != test.micro) {
                return member.micro > test.micro ? 1 : -1
            }
            else if (member.ext != test.ext) {
                return member.ext > test.ext ? 1 : -1
            }
            else return 0

        }
        return -1
    }


    void initAssets(String prodName, OciRelease rls, def resource) {
        def pkgs = jsonSlurper.parse(resource)
        if (pkgs == null)
            return

        int i = 0
        pkgs.legacy.each { leg ->
            String fp = leg.filePath
            if (fp.contains("readme.txt")) {
                rls.readmePath = fp
            } else if (fp.contains("OCIReleaseNotes.html") || fp.contains("releasenotes")) {
                rls.relNotesPath = fp
            } else {
                int patch = 0
                int pno = fp.indexOf("a_p", fp.indexOf('/'))
                if (pno == -1)
                    pno = fp.indexOf("b_p", fp.indexOf('/'))
                if (pno > -1) {
                    int p2 = fp.indexOf("_", pno + 3)
                    if (p2 == -1)
                        p2 = fp.indexOf(".", pno + 3)
                    try {
                        patch = fp.substring(pno + 3, p2).toInteger()
                    }
                    catch (NumberFormatException nfe)
                    {
                        // no worries
                    }
                }
                leg.patchLevel = patch
                OciAsset tlp = new OciAsset(leg)

                addAsset(prodName, rls, tlp, patch)
            }
        }
    }

    //------------------------------------ OciAsset functions ---------------------------------------
    String defKey() {
        "Tao.C:${OciContent.SNP}.P:${OciPatchBase.LTST}.L:0.Z:${OciCompress.TGZ}"
    }

    OciContent identifyContent (String progName, String name)
    {
        OciContent cnt
        if (progName.equals ("Jboss")) {
            if (name.contains("-src")) {
                cnt = OciContent.JSRC
            }
            else {
                cnt = OciContent.JBLD
            }
            return cnt
        }

        cnt = OciContent.SNP
        if (name.contains("NO_makefiles") || name.contains ("-nomake")) {
            cnt = OciContent.SRC
        } else if (name.contains("_project")) {
            cnt = OciContent.PRJ
        } else if (name.contains("_dox")) {
            cnt = OciContent.DOX
        }
        return cnt
    }

    OciCompress identifyCompress (String progName, String name) {
        OciCompress z = OciCompress.TGZ
        if (name.endsWith("zip")) {
            z = OciCompress.ZIP
        } else if (name.endsWith("bz2")) {
            z = OciCompress.BZ2
        }
        return z
    }

    OciPatchBase identifyPatchBase (String progName, String name, int patch) {
        OciPatchBase pb = OciPatchBase.BASE
        if (progName.equals ("Jboss")) {
            if (name.contains ("test")) {
                return OciPatchBase.JTEST
            }
            else if (name.startsWith("previous-releases/")) {
                return OciPatchBase.JLEVL
            }
            return OciPatchBase.JLTST
        }

        if (name.contains("with_latest_patches")) {
            pb = OciPatchBase.LTST
        } else if (name.contains("jumbo")) {
            pb = OciPatchBase.JMBO
        } else if (name.contains("+CIAO")) {
            pb = OciPatchBase.CIAO
        } else if (patch > 0) {
            String pnum = "a_p" + patch
            if (name.contains(pnum)) {
                pb = OciPatchBase.LEVL
            }
        } else if (name.contains("_dox")) {
            pb = OciPatchBase.DGEN
        }
        return pb
    }

    void addAsset (String progName, OciRelease rls, OciAsset tlp, int patch) {
        String name = tlp.filePath
        OciContent cnt = identifyContent (progName, name)
        OciCompress z = identifyCompress (progName, name)
        OciPatchBase pb = identifyPatchBase (progName, name, patch)
        int testNum = -1
        if (pb == OciPatchBase.JTEST) {
            testNum = 0
            int tpos = name.indexOf("test") + "test".length()
            int dash = name.indexOf('-',tpos)
            int dot = name.indexOf ('.', tpos)
            if (dash > 0 && dash < dot) dot = dash
            if (dot > 0 && dot > tpos) {
                testNum = name.substring(tpos,dot) as int
            }
            tlp.key = "${progName}.C:${cnt}.P:${pb}.T:${testNum}.L:${patch}.Z:${z}"
        }
        else {
            tlp.key = "${progName}.C:${cnt}.P:${pb}.L:${patch}.Z:${z}"
        }
        def found = rls.legacy?.find{it.key.equals(tlp.key)}
        if (found) {
            println "collision at key = " + tlp.key + " fp = " + tlp.filePath + " found = " + found.filePath
        }
        def plent = rls.plList?.find {
            it.patchKind == pb && it.patchNum == patch && it.testNum == testNum
        }
        if (plent) {
            if (!plent.contentKind?.contains (cnt))
                plent.addToContentKind (cnt)
        }
        else {
            plent = new OciSelectorInfo ([patchKind: pb, patchNum: patch, testNum: testNum])
            plent.addToContentKind (cnt)
            rls.addToPlList (plent)
        }
        rls.addToLegacy (tlp)
    }


    def target (OciRelease rls, def params) {
        List result = []

        String keyBase = "${rls.product.name}.${params.content}.${params.patchLevel}"

        OciCompress.each {cm ->
            String testKey = keyBase + ".Z:${cm}"
            def oa = rls.legacy.find { it.key.equals(testKey) }
            if (oa) {
                result.add([targetName: "${rls.basePath}/${oa.filePath}",
                            displayName: oa.shortUrlName,
                            fileSize: formatter.fmtFileSize (oa.fileSize),
                            md5sum: oa.md5,
                            timeStamp: oa.fileDate ])
            }
        }
        return result

    }

    String targetKey(OciSelectorInfo p) {
        String tno = p.testNum > -1 ? ".T:${p.testNum}" : ""
        return "P:${p.patchKind}${tno}.L:${p.patchNum}"
    }

    String targetKey(OciSelectorInfo p, int ckNdx) {
        return  "C:${p.contentKind[ckNdx]}.${targetKey(p)}"
    }

    void loadReleaseNote (OciRelease rls, def resource) {
        String key = "OCIRelease-${rls.rlsVersion}-RelNote"
        def parser=new XmlSlurper()
        parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      //  def url = new URL (resource as String)
        def con = resource.openConnection()
        con.connect()
        def page = parser.parse(con.content)
        ContentCache.content.$key = page.body
    }
    String cached (OciRelease rls, String what, String subpath) {
        String key = "${rls}.${what}"
        def url = new URL ("${rls.basePath}/${subpath}")
        HttpURLConnection con = (HttpURLConnection) url.openConnection()
//        if (ContentCache.lastModified.$key) {
//           con.addRequestProperty("If-Modified-Since", ContentCache.lastModified.$key)
//        }
        con.connect()
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            def parser=new XmlSlurper()
            parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            def page = parser.parse(con.getContent())
            page.each {
                println it.key
            }
            ContentCache.content.$key = page.body
            ContentCache.lastModified.$key = con.getHeaderField("Last-Modified")
        }

        ContentCache.content.$key

    }

}
