package com.ociweb.fetchtao

/**
 * Created by phil on 12/12/15.
 */

import grails.rest.Resource;

@Resource(uri="/downloaddds")
public class DdsPackage {
    String baseVersion
    int lastPatch
}

