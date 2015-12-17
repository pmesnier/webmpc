import com.ociweb.fetchtao.DdsPackage
import com.ociweb.fetchtao.TaoPackage
import com.ociweb.fetchtao.TaoPackageController
import com.ociweb.fetchtao.TaoReleases

class BootStrap {

    def init = { servletContext ->
       def ossProduct = []
        /*
        ossProduct << [major: 1, minor: 2, lastPatch: 12]
        ossProduct << [major: 1, minor: 3, lastPatch: 18]
        ossProduct << [major: 1, minor: 4, lastPatch: 26]
        ossProduct << [major: 1, minor: 5, lastPatch: 22]
        ossProduct << [major: 1, minor: 6, lastPatch: 15]
        ossProduct << [major: 2, minor: 0, lastPatch: 7]
        ossProduct << [major: 2, minor: 2, lastPatch: 7]
        // ossProduct << [major: 2, minor: 3, lastPatch: 0]

        ossProduct.each { taodef ->
            new TaoPackage(taodef).save()
        }
*/

        new TaoPackage (major: 1, minor: 2, lastPatch: 12).save()
        new TaoPackage (major: 1, minor: 3, lastPatch: 18).save()
        new TaoPackage (major: 1, minor: 4, lastPatch: 26).save()
        new TaoPackage (major: 1, minor: 5, lastPatch: 22).save()
        new TaoPackage (major: 1, minor: 6, lastPatch: 15).save()
        new TaoPackage (major: 2, minor: 0, lastPatch: 7).save()
        new TaoPackage (major: 2, minor: 2, lastPatch: 7).save()
        // new TaoPackage (major: 2, minor: 3, lastPatch: 0).save()


        ossProduct.clear()
        ossProduct << [name: "OCI TAO", rootServerName: "download.ociweb.com"]

        ossProduct.each { proddef ->
            new TaoReleases(proddef).save()
        }

        if (DdsPackage.count() == 0)
        {
            new DdsPackage(baseVersion: "3.7", lastPatch: 0).save()
        }
    }
    def destroy = {
    }
}
