package com.ociweb.oss

import java.text.DecimalFormat

/**
 * Created by phil on 1/20/16.
 */
class GitHubAsset {
    long size
    String created_at
    String browser_download_url

    static constraints = {
        created_at nullable : true
        browser_download_url nullable : true

    }
}
