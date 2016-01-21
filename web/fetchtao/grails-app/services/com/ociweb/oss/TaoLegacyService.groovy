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
    static int SOURCE_AND_PROJECT = 4
    static int DOXYGEN = 8

    static int PATCH_MASK = 1008
    static int BASE_RELEASE = 512
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

    static defaultPackage = new TaoLegacyPackage ([targetName: "Desired package not available",
                                                   md5sum    :"",
                                                   patchLevel:0,
                                                   filesize: 0,
                                                   timestamp:"no date"])

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
            content = DOXYGEN_GENERATED
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
        } else if (name.contains("_dox")) {
            content |= DOXYGEN
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

    static initProduct (Product prod, def params) {
        String resourceInfo = params.legacyInit
        if (resourceInfo) {
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

    }

    //------------------------------------ TaoRelease specific functions --------------------------------------

    static initPackages (TaoRelease rls, String packageInit) {
        rls.lastTarget = defKey ()
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
                tlp.release = rls
                rls.legacy.put key, tlp
            }
        }

    }

    static TaoLegacyPackage target (TaoRelease rls, def params) {
        if (rls.lastTarget == 0)
            rls.lastTarget = defKey()

        int plval = rls.lastTarget & PATCH_MASK
        if (params.patchLevel) {
            plval = params.patchLevel as int
        }
        else if (plval == LEVEL_PATCH) {
            int pnum = rls.lastTarget / LEVEL_SHIFT
            plval += pnum * LEVEL_SHIFT
        }
        int clval = rls.lastTarget & CONTENT_MASK
        if (params.content) {
            clval = params.content as int
        }

        int cmpval = rls.lastTarget & COMPRESS_MASK
        if (params.compress) {
            cmpval = params.compress as int
        }

        rls.lastTarget = plval + clval + cmpval

        println "target using patchLevel = " + plval +
                " content = " + clval +
                " compress = " + cmpval +
        " getting legacy [" + rls.lastTarget + "]"

        String key = rls.lastTarget as String
        TaoLegacyPackage tlp = rls.legacy.get(key)
        return tlp ? tlp : defaultPackage
    }

    static def patchlevelFor (TaoRelease rel) {
        if (rel == null)
            return [name:"unknown release requested", value: 0]

        def names = []
        patchList.each {pl ->
            if (pl.value != LEVEL_PATCH && rel.legacy.find { leg -> (leg.key as int & pl.value) == pl.value }) {
                names << pl
            }
        }

        String base = patchList.find {it.value as int == LEVEL_PATCH}.name
        int low = 0
        int high = 0
        rel.legacy.findAll {
            ((it.key as int & LEVEL_PATCH) == LEVEL_PATCH)
        }.each { leg ->
            int pnum = (leg.key as int) / LEVEL_SHIFT
            if (pnum) {
                if (low == 0 || pnum < low)
                    low = pnum
                if (high == 0 || pnum > high)
                    high = pnum
            }
        }
        (low..high).each {
            String n = base + " " + it
            int v = LEVEL_PATCH + it * LEVEL_SHIFT
            names << [name: n, value: v]
        }

        return names
    }

    static def contentFor (TaoRelease rel, def params)
    {
        int plval = params.patchLevel as int

        def names = []
        contentList.each {cl ->
            int testval = plval + cl.value
            if (rel.legacy.find { leg -> (leg.key as int & testval) == testval } ) {
                names << cl
            }

        }
        return names
    }

    static def compressFor (TaoRelease rel, def params)
    {
        int plval = 0
        int clval = 0
        try {
            plval = params.patchLevel as int
            clval = params.content as int
        }
        catch (NumberFormatException nfe) {
            return []
        }

        def names = []
        compressList.each {cm ->
            int testval = plval + clval + cm.value
            if (rel.legacy.find { leg -> (leg.key as int & testval) == testval } ) {
                names << cm
            }
        }
        return names
    }

}
