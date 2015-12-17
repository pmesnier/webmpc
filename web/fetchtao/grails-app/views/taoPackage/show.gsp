<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="taoDownload" />
        <g:set var="entityName" value="${message(code: 'taoPackage.baseVersion()', default: 'OCI TAO')}" />
        <g:set var="lastPatch" value="${taoPackage.lastPatch}" />
        <title>OCI TAO ${taoPackage.baseVersion()} Downloader</title>
    </head>
    <body>
        <a href="#list-taoPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            </ul>
        </div>
        <div id="pickOptions" class="content scaffold-edit" role="main">
            <h1><g:message code="default.chooseOpts.label" args="[entityName]" /> ${taoPackage.baseVersion()} </h1>
            <form resource="${this.taoPackage}" method="PUT" >
                <g:set var="lastpatch" value="${this.taoPackage?.lastPatch}" defaultvalue="10" />
                <fieldset class="form">
                    <p>Patch Level</p>
                    <p><g:radio name="patchLevel"  value="p0" /> Base (full source tree) </p>
                    <p><g:radio name="patchLevel"  value="pn" checked="checked" /> Latest, p${lastPatch} (full source tree) </p>
                    <p><g:radio name="patchLevel"  value="pj" /> Jumbo patch, combining all incremental patches from base to latest </p>
                    <p><g:radio name="patchLevel"  value="ps" /> Changed files from patch
                        <g:field type="number" name="changesLevel" value="${lastpatch}" min="1" max="${lastpatch}" default="${lastpatch}" />
                    </p>
                    <br>
                    <p>Archive contents</p>
                    <p><g:radio name="content" value="src" checked="checked" /> Source only </p>
                    <p><g:radio name="content" value="proj"/> Project files only</p>
                    <p><g:radio name="content" value="both"/> Source + Project files</p>
                    <br>
                    <p>File format</p>
                    <p><g:radio name="compress" value="tar.gz" checked="checked" /> .tar.gz </p>
                    <p><g:radio name="compress" value="zip" /> .zip </p>
                    <br>

                </fieldset>
                <h1>File To Download</h1>
                <dif 
                <fieldset class="form">
                    <g:set var="download" value="${taoPackage.target ("ps", 5, "src", "tar.gz") }" />
                    <p><a href="${download}">${download}</a></p>
                </fieldset>
                <!--
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.download.label', args: [entityName], default: 'Download [entityName]')}" />
                </fieldset>
                -->
            </form>
        </div>
    </body>
</html>
