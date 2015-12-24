package com.ociweb.fetchtao

/**
 * Created by phil on 12/19/15.
 */

class TaoLegacyPackage {

    static int CONTENT_MASK =7
    static int SOURCE_ONLY = 1
    static int PROJECT_ONLY = 2
    static int SOURCE_AND_PROJECT = 3
    static int UNDEF_CONTENT = 4

    static int PATCH_MASK = 248
    static int BASE_RELEASE = 8
    static int FULL_LATEST_RELEASE = 16
    static int JUMBO_PATCH = 32
    static int LEVEL_PATCH = 64
    static int UNDEF_PATCH = 128

    static int COMPRESS_MASK = 1792
    static int TGZ = 256
    static int ZIP = 512
    static int BZ2 = 1024

    static int LEVEL_SHIFT = 16
    String targetName
    String md5sum
    int content

    static int defKey() {
        return SOURCE_AND_PROJECT + FULL_LATEST_RELEASE + TGZ
    }

    static int genKey(String name, int patch) {
        int content = SOURCE_AND_PROJECT
        if (name.contains("NO_makefiles") || name.contains ("-nomake")) {
            content = SOURCE_ONLY
        } else if (name.contains("_project")) {
            content = PROJECT_ONLY
        }

        if (name.contains ("with_latest_patches")) {
            content |= FULL_LATEST_RELEASE
        } else if (name.contains ("jumbo")) {
            content |= JUMBO_PATCH
        } else if (patch > 0) {
            content += patch << LEVEL_SHIFT
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

    static int genKey(def p) {
        int pl = p.patchLevel.toInteger ()
        int key = p.content.toInteger () + pl + p.compress.toInteger()
        if (pl == LEVEL_PATCH) {
            key += p.changesLevel.toInteger () << LEVEL_SHIFT
        }
        return key
    }

 }
