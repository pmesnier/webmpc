package com.ociweb.fetchtao

/**
 * Created by phil on 12/11/15.
 */
import grails.transaction.Transactional

@Transactional(readOnly = false)
class TaoReleaseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true
    def productNameProperty

    def index() {
        respond TaoRelease.list(), model: [pkgCount: TaoRelease.count()]
    }

    def show (TaoRelease rls)
    {
        respond rls;
    }

    def save () {
        render "save called"
    }

    def taoDownloadLink (TaoRelease pkg)
    {
        def one = params.patchLevel
        def two = params.changesLevel
        def three = params.content
        def four = params.compress

        render template:'downloadLinkTao', model: [urlstr: pkg.target(one, two.toInteger(), three, four)]
    }

}
