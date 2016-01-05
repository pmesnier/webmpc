package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 12/9/15.
 */

//@Resource(uri="/downloadtao")
public class TaoRelease extends Release {

    static hasMany = [legacy:TaoLegacyPackage]
    static defaultPackage = new TaoLegacyPackage ([targetName: "Desired package not available",
                                                   md5sum    :"",
                                                   patchLevel:0,
                                                   filesize: 0,
                                                   timestamp:"no date"])

    Map legacy = [:]

    String rlsVersion
    String basePath
    String patchesPath
    String packageInit
    String readmePath
    String patchReadmePath
    String relNotesPath

    int lastPatch
    boolean patchsrc
    int lastTarget

    static constraints = {
        packageInit nullable:true
        readmePath nullable:true
        patchReadmePath nullable:true
        relNotesPath nullable:true
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

    def initFromJson () {
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(packageInit)
        def pkgs = jsonSlurper.parse(resource)
        int i = 0
        pkgs.taoLegacy.each { leg ->
            String fp = leg.filePath
            if (fp.contains("readme.txt")) {
                readmePath = fp
            } else if (fp.contains("OCIReleaseNotest.html") || fp.contains("releasenotes")) {
                relNotesPath = fp
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
                def found = legacy.get(key)
                if (found) {
                    println "collision at key = " + key + " fp = " + fp + " found = " + found.targetName
                }
                legacy.put key, tlp
            }
        }

        println "Loaded " + legacy.size() + " elements from " + packageInit
    }

    def initPackages () {
        println "Init packages called for rls = " + rlsVersion
        lastTarget = TaoLegacyPackage.defKey ()
        if (packageInit) {
            try {
                initFromJson()
                return
            }
            catch (Exception ex) {
                println "caught " + ex + " trying to parse json, reverting to download "
            }
        }

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
        TaoLegacyPackage tlp = legacy.get(key)
        return tlp ? tlp : defaultPackage
    }

    String toString () {
        rlsVersion + " lastTarget = " + lastTarget
    }

}