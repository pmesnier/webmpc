package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/7/16.
 */
class TaoLegacyService {

    static def loader = null
    static def jsonSlurper = new JsonSlurper()

    //------------------------------------ TaoProduct specific functions --------------------------------------

    static initProduct (Product prod, String resourceInfo) {

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

    //------------------------------------ TaoRelease specific functions --------------------------------------

    static initPackages (TaoRelease rls, String packageInit) {
        println "Init packages called for rls = " + rls.rlsVersion
        rls.lastTarget = TaoLegacyPackage.defKey ()
        try {
            if (initFromJson(rls, packageInit)) {
                return
            }
        }
        catch (Exception ex) {
            println "caught " + ex + " trying to parse json, reverting to download "
        }
        initFromDownload (rls)
    }

    static hasbz2 (TaoRelease rls, int patchType) {
        boolean result = false

        result = rls.legacy.find { pkg ->
            if ((pkg.key & TaoLegacyService.BZ2) != 0)
                return true
        }

        return result
    }

    static initFromJson (TaoRelease rls, String packageInit) {
        def resource = loader.getResource(packageInit)
        def pkgs = jsonSlurper.parse(resource)
        int i = 0
        pkgs.taoLegacy.each { leg ->
            String fp = leg.filePath
            if (fp.contains("readme.txt")) {
                rls.readmePath = fp
            } else if (fp.contains("OCIReleaseNotest.html") || fp.contains("releasenotes")) {
                rls.relNotesPath = fp
            } else {
                int patch = 0
                int pno = fp.indexOf("a_p", fp.indexOf('/'))
                if (pno > -1) {
                    int p2 = fp.indexOf("_", pno + 3)
                    if (p2 == -1)
                        p2 = fp.indexOf(".", pno + 3)
                    patch = fp.substring(pno + 3, p2).toInteger()
                }
                TaoLegacyPackage tlp = new TaoLegacyPackage([targetName: fp,
                                                             md5sum    : leg.md5,
                                                             patchLevel: patch,
                                                             timestamp : leg.fileDate,
                                                             filesize  : leg.fileSize])
                def key = TaoLegacyPackage.genKey(fp, patch) as String
                def found = rls.legacy.get(key)
                if (found) {
                    println "collision at key = " + key + " fp = " + fp + " found = " + found.targetName
                }
                rls.legacy.put key, tlp
            }
        }

    }

    static initMd5sums (String urlStr) {
        URL md5file = new URL (urlStr)
        def md5sums = []
        try {
            md5file.readLines().each ({line ->
                def words = line.split("  ")
                String sum = words[0]
                String filename = words[-1]
                md5sums << [file: filename, sum: sum]
            })
        } catch (SocketException ex) {
            println "Caught " + ex + " reading " + urlStr
        }
        return md5sums
    }

    static initFromDownload (TaoRelease rls) {
        def baseSums = []
        try {
            baseSums = initMd5sums(rls.basePath + "/TAO-" + rls.rlsVersion + ".md5")
            String baseRoot = "ACE+TAO-" + rls.rlsVersion
            baseSums << [file: baseRoot + ".tar.gz", sum : "not available"]
            baseSums << [file: baseRoot + ".zip", sum : "not available"]
            baseSums << [file: baseRoot + "-nomake.tar.gz", sum : "not available"]
            baseSums << [file: baseRoot + "-nomake.zip", sum : "not available"]
        }
        catch (FileNotFoundException ex) {
            try {
                baseSums = initMd5sums(rls.basePath + "/ACE+TAO-" + rls.rlsVersion + ".md5")
            } catch (ex2) {

            }
        }

        String key
        baseSums.each({
            key = TaoLegacyPackage.genKey(it.file, 0) as String
            rls.legacy.put  key, new TaoLegacyPackage( [targetName: rls.basePath + "/" + it.file,
                                                    md5sum:it.sum, patchLevel:0,
                                                    timestamp:it.time, size: it.fsize] )
        })

        for (int i = 1; i <= rls.lastPatch; i++) {
            try {
                baseSums = initMd5sums(rls.patchesPath + "/patch" + i + ".md5")
                if (i == rls.lastPatch) {
                    String jumboRoot = "TAO-" + rls.rlsVersion + "_jumbo_patch"
                    baseSums << [file: jumboRoot + ".tar.gz", sum : "not available"]
                    baseSums << [file: jumboRoot + ".zip", sum : "not available"]
                    baseSums << [file: jumboRoot + "_NO_Makefiles.tar.gz", sum : "not available"]
                    baseSums << [file: jumboRoot + "_NO_Makefiles.zip", sum : "not available"]
                }
                baseSums.each({
                    key = TaoLegacyPackage.genKey(it.file, i) as String
                    def target = (it.file.contains ("latest") ? rls.basePath : rls.patchesPath) + "/" + it.file
                    rls.legacy.put key , new TaoLegacyPackage([targetName: target, md5sum:it.sum, patchLevel:i])
                })
            }
            catch (FileNotFoundException ex) {

            }

        }

    }

    static TaoLegacyPackage target (TaoRelease rls, def params) {
        if (rls.lastTarget == 0)
            rls.lastTarget = TaoLegacyPackage.defKey()

        int patchLevel = params.patchLevel != null ? params.patchLevel.toInteger() : (rls.lastTarget & TaoLegacyPackage.PATCH_MASK)
        int changesLevel = params.changesLevel != null ? params.changesLevel.toInteger() : (rls.lastTarget >> TaoLegacyPackage.LEVEL_SHIFT)
        int content = params.content != null ? params.content.toInteger() : (rls.lastTarget & TaoLegacyPackage.CONTENT_MASK)
        int compress = params.compress != null ? params.compress.toInteger() : (rls.lastTarget & TaoLegacyPackage.COMPRESS_MASK)
        rls.lastTarget = patchLevel + content + compress

        println "target using patchLevel = " + patchLevel +
                " changesLevel = " + changesLevel +
                " content = " + content +
                " compress = " + compress
        " getting legacy [" + rls.lastTarget + "]"

        if (patchLevel == TaoLegacyPackage.LEVEL_PATCH)
            rls.lastTarget += (changesLevel << TaoLegacyPackage.LEVEL_SHIFT)
        String key = rls.lastTarget as String
        TaoLegacyPackage tlp = rls.legacy.get(key)
        return tlp ? tlp : rls.defaultPackage
    }


}
