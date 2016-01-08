<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'taoRelease.rlsVersion', default: 'OCI TAO')}" />
        <g:set var="lastPatch" value="${taoRelease.lastPatch}" />
        <title>OCI TAO ${taoRelease.rlsVersion} Downloader</title>
    </head>
    <body>
        <div id="pickOptions" class="content scaffold-edit" role="main">
            <h1><g:message code="default.chooseOpts.label" args="[entityName]" /> ${taoRelease.rlsVersion} </h1>

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
                    <p><g:radio name="compress" value="256" checked="checked" /> .tar.gz </p>
                    <p><g:radio name="compress" value="512" /> .zip </p>
                    <br>
                </fieldset>
                <g:submitButton name="Get Download Info"/>

            </g:formRemote>

            <h1>File To Download</h1>
            <div id="download_link">
            Link to download file is here
            </div>
        </div>
    </body>
</html>
