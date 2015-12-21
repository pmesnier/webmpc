import com.ociweb.fetchdds.DdsRelease
import com.ociweb.fetchtao.TaoProduct
import com.ociweb.fetchtao.TaoRelease

class BootStrap {

    def init = { servletContext ->
       def ossRelease = []

        ossRelease << [major: 1, minor: 2, lastPatch: 12]
        ossRelease << [major: 1, minor: 3, lastPatch: 18]
        ossRelease << [major: 1, minor: 4, lastPatch: 26]
        ossRelease << [major: 1, minor: 5, lastPatch: 22]
        ossRelease << [major: 1, minor: 6, lastPatch: 15]
        ossRelease << [major: 2, minor: 0, lastPatch: 7]
        ossRelease << [major: 2, minor: 2, lastPatch: 8]

        def ossProduct = []
        ossProduct << [name: "OCI TAO", rootServerName: "download.ociweb.com"]

        ossProduct.each { proddef ->
            def product = new TaoProduct(proddef)
            ossRelease.each { rlsdef ->
                def rls = new TaoRelease (rlsdef)
                rls.initPackages()
                product.addToReleases (rls)
            }
            product.save(failOnError: true)
        }

        if (DdsRelease.count() == 0)
        {
            new DdsRelease(baseVersion: "3.7", lastPatch: 0).save()
        }
    }
    def destroy = {
    }
}
