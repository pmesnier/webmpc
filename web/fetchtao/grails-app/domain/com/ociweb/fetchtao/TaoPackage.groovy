package com.ociweb.fetchtao;

/**
 * Created by phil on 12/9/15.
 */
import grails.rest.Resource;

@Resource(uri="/download")
public class TaoPackage {
    String baseVersion
    int patches
    String repo
}
