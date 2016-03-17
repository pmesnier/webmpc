package com.ociweb.oss.spectao

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.json.JsonSlurper
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class MpcProjectManagerSpec extends Specification {
    MpcProjectManager mpcProjectManager

    def setup() {
        mpcProjectManager = new MpcProjectManager()
        String bootref = "MPC_projects/tao22a.json"
        def jsonSlurper = new JsonSlurper()
        def resource = getClass().getClassLoader().getResource(bootref)
        def initdef = jsonSlurper.parse(resource)

        mpcProjectManager.initAll(initdef)

    }

    def cleanup() {
    }

    void "test load"() {
        expect:"fix me"
        true == false
    }
}
