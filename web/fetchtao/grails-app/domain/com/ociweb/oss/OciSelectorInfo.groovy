package com.ociweb.oss

/**
 * Created by phil on 1/27/16.
 */
class OciSelectorInfo implements Comparable<OciSelectorInfo> {
    static hasMany = [contentKind : OciContent]
    List contentKind

    OciPatchBase patchKind
    int patchNum
    int testNum


    @Override
    int compareTo(OciSelectorInfo o) {
        if (patchKind != o.patchKind) {
            return patchKind < o.patchKind ? -1 : 1
        }
        if (patchKind == OciPatchBase.JTEST) {
            if (testNum != o.testNum) {
                return testNum < o.testNum ? -1 : 1
            }
        }
        return patchNum == o.patchNum ? 0 :
             (patchNum > o.patchNum ? -1 : 1) //reverse numeric order
    }
}
