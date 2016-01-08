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
            <h1><g:message code="taoRelease.select.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <p>OCI TAO Version <g:select name="rlsVersion"
             from="${taoReleaseList}"
              optionValue="rlsVersion"/></p>
        </div>

        <div id="list-taoRelease" class="content scaffold-edit" role="main">
            <ul>
                <g:each in="${taoReleaseList}">
                <li> <g:link action="show" id="${it.id}" >OCI TAO version ${it.rlsVersion} has ${it.lastPatch} patches.</g:link></li>
                </g:each>
            </ul>
        </div>

    </body>
</html>
