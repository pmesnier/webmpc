package com.ociweb.fetchtao;

import com.ociweb.oss.Release

/**
 * Created by phil on 12/9/15.
 */

//@Resource(uri="/downloadtao")
public class TaoRelease extends Release {

    static hostName = "download.ociweb.com"

    int major
    int minor
    int lastPatch
    def firstRelease
    def latestPatch

    static Collection<Properties> initMd5sums (String urlStr) {
        URL md5file = new URL (urlStr)
        def md5sums = []
        md5file.readLines().each ({line ->
            def words = line.split("  ")
            String sum = words[0]
            String filename = words[words.length-1]
            md5sums << [file: filename, sum: sum]
        })
        return md5sums
    }

    def initPackages () {
        /*
        String path = this.rootPath(false, false)
        def baseSums = []
        try {
            baseSums = initMd5sums(path + "/TAO-" + baseVersion() + ".md5")
        }
        catch (FileNotFoundException ex) {
            try {
                baseSums = initMd5sums(path + "/ACE+TAO-" + baseVersion() + ".md5")
            } catch (ex2) {

            }
        }

        def pkgs = []
        baseSums.each({
            pkgs << [targetName: path + "/" + it.file,
                     md5sum:it.sum,
                     patchLevel:0,
                     id: TaoLegacyPackage.genId(it.file, 0)]
        })

        for (int i = 1; i <= lastPatch; i++) {
            path = this.rootPath(true, false)
            try {
                baseSums.clear()
                baseSums = initMd5sums(path + "/patch" + i + ".md5")
                baseSums.each({
                    pkgs << [targetName: path + "/" + it.file,
                             md5sum:it.sum,
                             patchLevel:i,
                             id: TaoLegacyPackage.genId(it.file, i)]
                })
            }
            catch (FileNotFoundException ex) {

            }
        }

        pkgs.each ({ pkgdef ->
            def tp = new TaoLegacyPackage (pkgdef)
            this.addToPackages (tp)
        })
        */
    }

    String baseVersion () {
        return Integer.toString(major) + "." + Integer.toString(minor) + "a"
    }

    String rootPath (boolean patch, boolean prefixFile) {
        String path = "http://" + hostName + "/TAO-" + baseVersion ()
        if (patch) {
            path += "_patches"
        }
        if (prefixFile) {
            path += (patch) ? "/TAO-" : "/ACE+TAO-"
            path += baseVersion ()
        }
        return path
    }

    boolean sourceOnlyAvailable (boolean base) {
        return base ? (major > 1 && minor > 0) : (major > 1 || minor > 5)
    }

    String fullArchive (boolean base, int srcProj) {
        String content = ""
        if (srcProj == TaoLegacyPackage.SOURCE_ONLY && sourceOnlyAvailable (base)) {
            content = base ? "-nomake" : "_with_latest_patches_NO_makefiles"
        } else if (!base) {
            content = "_with_latest_patches"
        }
        rootPath(false, true) + content

    }

    String changedFiles (int level, int srcProj) {
        String content
        if (level == -1) {
            content = "_jumbo_patch"
            if (srcProj == TaoLegacyPackage.SOURCE_ONLY && sourceOnlyAvailable(false))
                content += "NO_makefiles"
        }
        else {
            content = "_p" + level
            if (srcProj == TaoLegacyPackage.SOURCE_AND_PROJECT ||
                    (srcProj == TaoLegacyPackage.PROJECT_ONLY && !sourceOnlyAvailable(false))) {
                content += "_patched_files"
            }
            else if (srcProj == TaoLegacyPackage.SOURCE_ONLY) {
                content += "_patched_files_NO_makefiles"
            }
            else {
                content += "_project_files"
            }
        }

        rootPath(true, true) + content
    }

    TaoLegacyPackage target (def p) {
        int patchLevel = p.patchLevel.toInteger()
        int changesLevel = p.changesLevel.toInteger()
        int content = p.content.toInteger()
        int compress = p.compress.toInteger()

        boolean jumbo = patchLevel == TaoLegacyPackage.JUMBO_PATCH
        boolean patch = ( jumbo || patchLevel == TaoLegacyPackage.LEVEL_PATCH)
        String t = patch ? changedFiles (jumbo ? -1 : changesLevel, content)
        : fullArchive (patchLevel == TaoLegacyPackage.BASE_RELEASE, content)
        String compressStr = compress == TaoLegacyPackage.TGZ ? ".tar.gz" : compress == TaoLegacyPackage.ZIP ? ".zip" : ".tar.bz2"
        String tpath = new String (t + "." + compressStr)
        return new TaoLegacyPackage ([targetName: tpath,
                                      md5sum    :"Under Construction",
                                      patchLevel:changesLevel,
                                      id        : TaoLegacyPackage.genId(tpath,changesLevel)])
    }

    String toString () {
        baseVersion()
    }

}
