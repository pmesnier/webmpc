package com.ociweb.oss

import groovy.json.JsonSlurper

/**
 * Created by phil on 1/7/16.
 */
class TaoLegacyService {

    static def loader = null
    static def jsonSlurper = new JsonSlurper()

    static int CONTENT_MASK =7
    static int SOURCE_ONLY = 1
    static int PROJECT_ONLY = 2
    static int SOURCE_AND_PROJECT = 3
    static int DOXYGEN = 4

    static int PATCH_MASK = 504
    static int BASE_RELEASE = 8
    static int FULL_LATEST_RELEASE = 16
    static int JUMBO_PATCH = 32
    static int LEVEL_PATCH = 64
    static int BASE_PLUS_CIAO = 128
    static int DOXYGEN_GENERATED = 256

    static int COMPRESS_MASK = 7168
    static int TGZ = 1024
    static int ZIP = 2048
    static int BZ2 = 4096

    static int LEVEL_SHIFT = 65536

    static patchList = [[name:"Base release, unpatched, full source", value: BASE_RELEASE],
                        [name:"Latest patch, full source", value: FULL_LATEST_RELEASE],
                        [name:"Jumbo patch, all patched files from base to latest", value: JUMBO_PATCH],
                        [name:"Changed files only, patch level ", value: LEVEL_PATCH],
                        [name:"Base release with CIAO, full source", value: BASE_PLUS_CIAO],
                        [name:"Doxygen generated documentation", value: DOXYGEN_GENERATED]]

    static contentList = [[name:"Source files only", value: SOURCE_ONLY],
                          [name:"Project files only", value: PROJECT_ONLY],
                          [name:"Source + Project files", value: SOURCE_AND_PROJECT],
                          [name:"Doxygen files", value: DOXYGEN]]

    static compressList = [[name:"tar.gz", value: TGZ],
                           [name:"zip", value: ZIP],
                           [name:"tar.bzip2", value: BZ2]]


    //------------------------------------ TaoLegacyPackage functions ---------------------------------------
    static int defKey() {
        return SOURCE_AND_PROJECT + FULL_LATEST_RELEASE + TGZ
    }

    static int genKey(String name, int patch) {
        int content = SOURCE_AND_PROJECT
        if (name.contains("NO_makefiles") || name.contains ("-nomake")) {
            content = SOURCE_ONLY
        } else if (name.contains("_project")) {
            content = PROJECT_ONLY
        } else if (name.contains("_dox")) {
            content = DOXYGEN | DOXYGEN_GENERATED
        }

        if (name.contains ("with_latest_patches")) {
            content |= FULL_LATEST_RELEASE
        } else if (name.contains ("jumbo")) {
            content |= JUMBO_PATCH
        } else if (name.contains ("+CIAO")) {
            content |= BASE_PLUS_CIAO
        } else if (patch > 0) {
            content += patch * LEVEL_SHIFT
            String pnum = "a_p" + patch
            if (name.contains (pnum)) {
                content |= LEVEL_PATCH
            }
        } else {
            content |= BASE_RELEASE
        }

        if (name.endsWith("gz")) {
            content |= TGZ
        } else if (name.endsWith("zip")) {
            content |= ZIP
        } else if (name.endsWith("bz2")) {
            content |= BZ2
        }

        return content
    }


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
        rls.lastTarget = defKey ()
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

    static initFromJson (TaoRelease rls, String packageInit) {
        if (loader == null)
            loader = rls.getClass().getClassLoader()
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
                def key = genKey(fp, patch) as String
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
            key = genKey(it.file, 0) as String
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
                    key = genKey(it.file, i) as String
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
            rls.lastTarget = defKey()

        int plval = params.patchLevel ? patchList.find { pl ->
                pl.name.equals(params.patchLevel)
            }.value : (rls.lastTarget & PATCH_MASK)

        int clval = params.changesLevel ? contentList.find { cl ->
            cl.name.equals(params.content)
        }.value : (rls.lastTarget & CONTENT_MASK)

        int cmpval = params.compress ? compressList.find {cmp ->
            cmp.name.equals(params.compress)
        }.value : (rls.lastTarget & COMPRESS_MASK)

        int pnum = params.changesLevel ? params.changesLevel.toInteger() : (rls.lastTarget / LEVEL_SHIFT)
        rls.lastTarget = plval + clval + cmpval + (plval == LEVEL_PATCH ? pnum * LEVEL_SHIFT : 0)

        println "target using patchLevel = " + plval +
                " changesLevel = " + pnum +
                " content = " + clval +
                " compress = " + cmpval
        " getting legacy [" + rls.lastTarget + "]"

        String key = rls.lastTarget as String
        TaoLegacyPackage tlp = rls.legacy.get(key)
        return tlp ? tlp : rls.defaultPackage
    }

    static def patchlevelFor (TaoRelease rel) {
        if (rel == null)
            return [name:"unknown release requested", value: 0]

        def names = []
        patchList.each {pl ->
            if (pl.value != LEVEL_PATCH && rel.legacy.find { leg ->
                (leg.key as int & pl.value) == pl.value }) {
                names << pl
            }
        }
        if (rel.lastPatch > 0) {
            String base = patchList.find {it.value as int == LEVEL_PATCH}.name
            (1..rel.lastPatch).each {
                String n = base + " " + it
                int v = LEVEL_PATCH + it * LEVEL_SHIFT
                names << [name: n, value: v]
            }
        }
        return names
    }

    static def contentFor (TaoRelease rel, def params)
    {
        int plval = params.patchLevel

        def names = []
        contentList.each {cl ->
            int testval = plval + cl.value
            if (rel.legacy.find { leg -> (leg.key as int & testval) == testval } ) {
                names << cl.name
            }

        }
        return names
    }

    static def compressFor (TaoRelease rel, def params)
    {
        int plval = patchList.find { pl ->
            pl.name.equals(params.patchLevel)
        }.value

        int clval = contentList.find { cl ->
            cl.name.equals(params.content)
        }.value

        int pnum = params.changesLevel.toInteger()
        int fixedtest = plval + clval + (plval == LEVEL_PATCH ? pnum * LEVEL_SHIFT : 0)

        def names = []
        compressList.each {cm ->
            int testval = fixedtest + cm.value
            if (rel.legacy.find { leg -> (leg.key as int & testval) == testval } ) {
                names << cm.name
            }
        }
        return names
    }

}
