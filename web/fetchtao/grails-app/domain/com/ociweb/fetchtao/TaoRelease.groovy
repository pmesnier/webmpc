package com.ociweb.fetchtao;

import com.ociweb.oss.Release

/**
 * Created by phil on 12/9/15.
 */

//@Resource(uri="/downloadtao")
public class TaoRelease extends Release {

    static hasMany = [legacy:TaoLegacyPackage]
    static defaultPackage = new TaoLegacyPackage ([targetName: "Desired package not available",
                                                   md5sum    :"",
                                                   patchLevel:0])

    Map legacy = [:]

    String rlsVersion
    String basePath
    String patchesPath
//    URL    readme
//    URL    releaseNotes

    int lastPatch
    boolean patchsrc
    int lastTarget

    static initMd5sums (String urlStr) {
        URL md5file = new URL (urlStr)
        def md5sums = []
        md5file.readLines().each ({line ->
            def words = line.split("  ")
            String sum = words[0]
            String filename = words[-1]
            md5sums << [file: filename, sum: sum]
        })
        return md5sums
    }

    def initFiles (String urlStr) {
        URL directory = new URL (urlStr)
        def files = []
        directory .readLines().each ({line ->
            def words = line.split (" ")

        })
    }
    def initPackages () {
        println "Init packages called for rls = " + rlsVersion
        def baseSums = []
        try {
            baseSums = initMd5sums(basePath + "/TAO-" + rlsVersion + ".md5")
            String baseRoot = "ACE+TAO-" + rlsVersion
            baseSums << [file: baseRoot + ".tar.gz", sum : "not available"]
            baseSums << [file: baseRoot + ".zip", sum : "not available"]
            baseSums << [file: baseRoot + "-nomake.tar.gz", sum : "not available"]
            baseSums << [file: baseRoot + "-nomake.zip", sum : "not available"]
        }
        catch (FileNotFoundException ex) {
            try {
                baseSums = initMd5sums(basePath + "/ACE+TAO-" + rlsVersion + ".md5")
            } catch (ex2) {

            }
        }

        String key
        baseSums.each({
            key = TaoLegacyPackage.genKey(it.file, 0) as String
            legacy.put  key, new TaoLegacyPackage( [targetName: basePath + "/" + it.file,
                                                    md5sum:it.sum, patchLevel:0,
            timestamp:it.time, size: it.fsize] )
        })

        for (int i = 1; i <= lastPatch; i++) {
            try {
                baseSums = initMd5sums(patchesPath + "/patch" + i + ".md5")
                if (i == lastPatch) {
                    String jumboRoot = "TAO-" + rlsVersion + "_jumbo_patch"
                    baseSums << [file: jumboRoot + ".tar.gz", sum : "not available"]
                    baseSums << [file: jumboRoot + ".zip", sum : "not available"]
                    baseSums << [file: jumboRoot + "_NO_Makefiles.tar.gz", sum : "not available"]
                    baseSums << [file: jumboRoot + "_NO_Makefiles.zip", sum : "not available"]
                }
                baseSums.each({
                    key = TaoLegacyPackage.genKey(it.file, i) as String
                    def target = (it.file.contains ("latest") ? basePath : patchesPath) + "/" + it.file
                    legacy.put key , new TaoLegacyPackage([targetName: target, md5sum:it.sum, patchLevel:i])
                })
            }
            catch (FileNotFoundException ex) {

            }

        }

        lastTarget = TaoLegacyPackage.defKey ()

    }

    TaoLegacyPackage target (def params) {
        if (lastTarget == 0)
            lastTarget = TaoLegacyPackage.defKey()

        int patchLevel = params.patchLevel != null ? params.patchLevel.toInteger() : (lastTarget & TaoLegacyPackage.PATCH_MASK)
        int changesLevel = params.changesLevel != null ? params.changesLevel.toInteger() : (lastTarget >> TaoLegacyPackage.LEVEL_SHIFT)
        int content = params.content != null ? params.content.toInteger() : (lastTarget & TaoLegacyPackage.CONTENT_MASK)
        int compress = params.compress != null ? params.compress.toInteger() : (lastTarget & TaoLegacyPackage.COMPRESS_MASK)
        lastTarget = patchLevel + content + compress

        println "target using patchLevel = " + patchLevel +
                " changesLevel = " + changesLevel +
                " content = " + content +
                " compress = " + compress
                " getting legacy [" + lastTarget + "]"

        if (patchLevel == TaoLegacyPackage.LEVEL_PATCH)
            lastTarget += (changesLevel << TaoLegacyPackage.LEVEL_SHIFT)
        String key = lastTarget as String

        legacy.containsKey(key) ? legacy.get(key) : defaultPackage
    }

    String toString () {
        rlsVersion + " lastTarget = " + lastTarget
    }

}
