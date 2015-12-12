import com.ociweb.fetchtao.DdsPackage
import com.ociweb.fetchtao.TaoPackage

class BootStrap {

    def init = { servletContext ->
        new TaoPackage(baseVersion: "1.4a", lastPatch: 26).save()
        new TaoPackage(baseVersion: "1.5a", lastPatch: 22).save()
        new TaoPackage(baseVersion: "1.6a", lastPatch: 15).save()
        new TaoPackage(baseVersion: "2.0a", lastPatch: 7).save()
        new TaoPackage(baseVersion: "2.2a", lastPatch: 7).save()
     // new TaoPackage(baseVersion: "2.3a", lastPatch: 0).save()
    }
    def destroy = {
    }
}
