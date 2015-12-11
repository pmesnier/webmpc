import com.ociweb.fetchtao.TaoPackage

class BootStrap {

    def init = { servletContext ->
        new TaoPackage(baseVersion: "1.4a", patches: 26, repo: "cvs").save()
        new TaoPackage(baseVersion: "1.5a", patches: 22, repo: "svn").save()
        new TaoPackage(baseVersion: "1.6a", patches: 15, repo: "svn").save()
        new TaoPackage(baseVersion: "2.0a", patches: 7, repo: "svn").save()
        new TaoPackage(baseVersion: "2.2a", patches: 7, repo: "svn").save()
     // new TaoPackage(baseVersion: "2.3a", patches: 0, repo: "git").save()
    }
    def destroy = {
    }
}
