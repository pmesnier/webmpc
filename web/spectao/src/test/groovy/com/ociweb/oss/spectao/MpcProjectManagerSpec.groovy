package com.ociweb.oss.spectao

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.json.JsonSlurper
import net.sf.ehcache.util.ProductInfo
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

@TestMixin(GrailsUnitTestMixin)
@Mock([Workspace, Feature, Project, MenuProjects, MenuSubEntry, MenuEntry, MpcProduct, MpcSubset, MpcGroup, MpcProject, MpcUnit, MpcCategory, MpcFeature])
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

    void "test pickList"() {
        setup:
        Workspace wsp = new Workspace([name    : "plt",
                                       projects : [],
                                       desiredProject: [],
                                       impliedProject: [],
                                       features: []])
        MpcProjectManager.loadFeatures(wsp)
        wsp.product = MpcProjectManager.mapper.currentProduct
        wsp.currentSubset = wsp.product.menu.get(1).subMenu.get(1)

        expect:
          wsp.currentSubset.pickList != null

    }
}
