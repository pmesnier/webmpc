package com.ociweb.fetchtao;

/**
 * Created by phil on 12/9/15.
 */
import grails.rest.Resource;

@Resource(uri="/downloadtao")
public class TaoPackage {
    String baseVersion
    int lastPatch

}
