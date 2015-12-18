import com.ociweb.fetchdds.DdsRelease
import com.ociweb.fetchtao.TaoProduct
import com.ociweb.fetchtao.TaoRelease

class BootStrap {

    def init = { servletContext ->
       def ossPackage = []

        ossPackage << [major: 1, minor: 2, lastPatch: 12]
        ossPackage << [major: 1, minor: 3, lastPatch: 18]
        ossPackage << [major: 1, minor: 4, lastPatch: 26]
        ossPackage << [major: 1, minor: 5, lastPatch: 22]
        ossPackage << [major: 1, minor: 6, lastPatch: 15]
        ossPackage << [major: 2, minor: 0, lastPatch: 7]
        ossPackage << [major: 2, minor: 2, lastPatch: 8]

        def ossProduct = []
        ossProduct << [name: "OCI TAO", rootServerName: "download.ociweb.com"]

        ossProduct.each { proddef ->
            def release = new TaoProduct(proddef)
            ossPackage.each { pkgdef ->
                def pkg = new TaoRelease (pkgdef)
                release.addToReleases (pkg)
            }
            release.save(failOnError: true)
        }

        if (DdsRelease.count() == 0)
        {
            new DdsRelease(baseVersion: "3.7", lastPatch: 0).save()
        }
    }
    def destroy = {
    }
}
