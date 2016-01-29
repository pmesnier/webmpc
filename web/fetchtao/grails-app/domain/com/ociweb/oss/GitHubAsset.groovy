package com.ociweb.oss

import java.text.DecimalFormat

/**
 * Created by phil on 1/20/16.
 */
class GitHubAsset {
    long size
    String created_at
    String browser_download_url

    String getFmtFileSize () {
        def fmt = new DecimalFormat("###.##")
        def klimit=1024
        if (size > (klimit * klimit * klimit)) {
            fmt.format(size / (klimit * klimit * klimit)) + "G"
        }
        else if (size > (klimit * klimit)) {
            fmt.format(size / (klimit * klimit)) + "M"
        }
        else if (size > (klimit)) {
            fmt.format (size / (klimit)) + "K"
        }
        else {
            Integer (size).toString()
        }
    }

}
