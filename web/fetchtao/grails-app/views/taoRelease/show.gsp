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
        </div>
    </body>
</html>
