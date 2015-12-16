package com.ociweb.fetchtao

/**
 * Created by phil on 12/15/15.
 */

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TaoReleasesController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static standardScaffolding = true

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TaoReleases.list(params), model:[productName: "Do Wah Diddy"]
    }

}
