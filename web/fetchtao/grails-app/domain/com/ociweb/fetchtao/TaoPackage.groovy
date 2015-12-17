package com.ociweb.fetchtao;

/**
 * Created by phil on 12/9/15.
 */
import grails.rest.Resource
import org.apache.tools.ant.taskdefs.ExecuteOn

//@Resource(uri="/downloadtao")
public class TaoPackage {
  //  static belongsTo = [product: TaoReleases]

    def int major
    def int minor
    def int lastPatch

    def String baseVersion () {
        return Integer.toString(major) + "." + Integer.toString(minor) + "a"
    }

    def String rootPath (boolean patch) {
        String path = "http://download.ociweb.com/TAO-" + baseVersion ()
        path += (patch) ? "_patches" + "/TAO-" : "/ACE+TAO-"
        return path + baseVersion()
    }

    def boolean sourceOnlyAvailable (boolean base) {
        return base ? (major > 1 && minor > 0) : (major > 1 || minor > 5)
    }

    def String fullArchive (boolean base, String srcProj) {
        String content = ""
        if (srcProj.equals("src") && sourceOnlyAvailable (base)) {
            content = base ? "-nomake" : "_with_latest_patches_NO_makefiles"
        } else if (!base) {
            content = "_with_latest_patches"
        }
        rootPath(false) + content

    }

    def String changedFiles (int level, String srcProj) {
        String content
        if (level == -1) {
            content = "_jumbo_patch"
            if (srcProj.equals("src") && sourceOnlyAvailable(false))
                content += "NO_makefiles"
        }
        else {
            content = "_p" + level
            if (srcProj.equals("both") || (srcProj.equals("src") && !sourceOnlyAvailable(false))) {
                content += "_patched_files"
            }
            else if (srcProj.equals("src")) {
                content += "_patched_files_NO_makefiles"
            }
            else {
                content += "_project_files"
            }
        }

        rootPath(true) + content
    }

    def String target (String patchLevel, int changesLevel, String contents, String compress) {
        boolean patch = (patchLevel.equals("pj") || patchLevel.equals("ps"))
        String t = patch ? changedFiles (patchLevel.equals("pj") ? -1 : changesLevel, contents)
        : fullArchive (patchLevel.equals("p0"), contents)

        t + "." + compress
    }

    def String toString () {
        baseVersion()
    }

}
