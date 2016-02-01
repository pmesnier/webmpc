package com.ociweb.oss

import java.text.DecimalFormat

/**
 * Created by phil on 2/1/16.
 */
class Formatter {
    public String fmtFileSize (long fileSize) {
        def fmt = new DecimalFormat("###.##")
        def klimit=1024
        if (fileSize > (klimit * klimit * klimit)) {
            fmt.format(fileSize / (klimit * klimit * klimit)) + "G"
        }
        else if (fileSize > (klimit * klimit)) {
            fmt.format(fileSize / (klimit * klimit)) + "M"
        }
        else if (fileSize > (klimit)) {
            fmt.format (fileSize / (klimit)) + "K"
        }
        else {
            fileSize as String
        }
    }

}
