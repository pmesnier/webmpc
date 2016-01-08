<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'taoRelease.label', default: 'OCI TAO')}" />
        <title>OCI TAO Downloader</title>
    </head>
    <body>
        <div id="list-taoRelease" class="content scaffold-edit" role="main">
        </div>
                <div class="content scaffold-edit" id="choose-version" >
                    <h1><g:message code="taoRelease.select.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <p><g:select name="rlsVersion"
                          from="${taoReleaseList}"
                          optionValue="rlsVersion"/></p>
                </div>

                <div id="choose-collection" class="content scaffold-edit" role="main">
                    <g:set var="lastpatch" value="${this.taoRelease?.lastPatch}" defaultvalue="10" />
                    <h1>Select the Patch Level Package</h1>
                    <p><g:select name="patchLevel"
                           from="${plList}"
                           noSelection="patch level selector"/> </p>
                    <p>Changed files from patch
                       <g:field type="number" name="changesLevel"
                          value="${lastpatch}" min="1" max="${lastpatch}" default="${lastpatch}" /></p>
                </div>

                <div id="choose-content" class="content scaffold-edit" role="main">
                    <h1>Select the Contents</h1>
                    <p><g:select name="content"
                           from="['content tbd']"
                           noSelection="content selector"/></p>
                </div>

                <div id="choose-compress" class="content scaffold-edit" role="main">
                    <h1>Select the Archive Compression Model</h1>
                    <p><g:select name="compress"
                           from="['compression tbd']"
                           noSelection="compression selector" /></p>
                </div>

        <h1>File To Download</h1>
        <div id="download_link">
        Link to download file is here
        </div>
    </body>
</html>
