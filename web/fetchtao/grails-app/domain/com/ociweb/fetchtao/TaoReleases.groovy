package com.ociweb.fetchtao

/**
 * Created by phil on 12/15/15.
 */
class TaoReleases {
    def String name
    def String rootServerName

 //   static hasmany = [releases: TaoPackage]

    def String rootName () {
        "http://" + rootServerName
    }
}
