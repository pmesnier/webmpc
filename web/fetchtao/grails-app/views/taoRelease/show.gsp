<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="taoDownload" />
        <g:set var="entityName" value="${message(code: 'taoRelease.baseVersion()', default: 'OCI TAO')}" />
        <g:set var="lastPatch" value="${taoRelease.lastPatch}" />
        <title>OCI TAO ${taoRelease.baseVersion()} Downloader</title>
    </head>
    <body>
        <a href="#list-taoRelease" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            </ul>
        </div>
        <div id="pickOptions" class="content scaffold-edit" role="main">
            <h1><g:message code="default.chooseOpts.label" args="[entityName]" /> ${taoRelease.baseVersion()} </h1>
            <g:formRemote name="taoDownloadForm" url="[controller:'TaoRelease', action:'taoDownloadLink']" update="download_link">
                <g:set var="lastpatch" value="${this.taoRelease?.lastPatch}" defaultvalue="10" />
                <fieldset class="form">
                    <g:hiddenField name="id" value="${taoRelease.id}" />
                    <p>Patch Level</p>
                    <p><g:radio name="patchLevel"  value="8" /> Base (full source tree) </p>
                    <p><g:radio name="patchLevel"  value="16" checked="checked" /> Latest, p${lastPatch} (full source tree) </p>
                    <p><g:radio name="patchLevel"  value="32" /> Jumbo patch, combining all incremental patches from base to latest </p>
                    <p><g:radio name="patchLevel"  value="64" /> Changed files from patch
                        <g:field type="number" name="changesLevel" value="${lastpatch}" min="1" max="${lastpatch}" default="${lastpatch}" />
                    </p>
                    <br>
                    <p>Archive contents</p>
                    <p><g:radio name="content" value="1" checked="checked" /> Source only </p>
                    <p><g:radio name="content" value="2"/> Project files only</p>
                    <p><g:radio name="content" value="3"/> Source + Project files</p>
                    <br>
                    <p>File format</p>
                    <p><g:radio name="compress" value="512" checked="checked" /> .tar.gz </p>
                    <p><g:radio name="compress" value="1024" /> .zip </p>
                    <br>

                </fieldset>
                <g:submitButton name="Get Download Info" resource="${taoRelease}"/>
            </g:formRemote>

            <h1>File To Download</h1>
            <div id="download_link">
            </div>
        </div>
    </body>
</html>