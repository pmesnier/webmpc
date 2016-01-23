package com.ociweb.oss

/**
 * Created by phil on 1/14/16.
 */
class MpcCommandLine {
    static belongsTo = [release: OciRelease]
    OciRelease release

    def attributes = [:]
    def components = []
    def platforms = []


}
