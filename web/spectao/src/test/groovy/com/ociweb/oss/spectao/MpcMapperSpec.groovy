package com.ociweb.oss.spectao

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock(MpcUnit)
class MpcMapperSpec extends Specification {
    MpcMapper mapper

    def setup () {
        mapper = new MpcMapper ()
    }

    void "test Mapper add a category"() {
        setup:
        mapper.addCategory ("testCore")

        expect:
        mapper.currentCategory.name == "testCore"
    }

    void "test Mapper add a unit" () {
        setup:
        mapper.addCategory ("testCore")
        mapper.addGroup ("testGroup")
        mapper.addSubset ([alias: "a test subset"])
     //   mapper.addProjectUnit ([name:"testUnit", mpcpath:"/path/to/mpc/file/testMpcProject.mpc"])
    }
}
