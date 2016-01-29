package com.ociweb.oss

/**
 * Created by phil on 1/27/16.
 */

enum OciContent {
    SRC,  // source only
    PRJ,  // project only or jboss build
    SNP,  // source and project
    DOX,  // doxygen files
    JSRC, // Jboss source files
    JBLD // Jboss build packages

}

enum OciPatchBase {
    BASE, // BASE_RELEASE = 512
    LTST, // FULL_LATEST_RELEASE = 16
    JMBO, // JUMBO_PATCH = 32
    CIAO, // BASE_PLUS_CIAO = 128
    DGEN, // DOXYGEN_GENERATED = 256
    LEVL, //  LEVEL_PATCH = 64
    JLTST, //Jboss latest
    JLEVL, // Jboss level
    JTEST  // Jboss test package

}

enum OciCompress {
    TGZ,
    ZIP,
    BZ2
}


