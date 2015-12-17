<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="taoDownload" />
        <g:set var="entityName" value="${message(code: 'taoPackage.label', default: 'OCI TAO')}" />
        <title>OCI TAO Downloader</title>
    </head>
    <body>
        <a href="#list-taoPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            </ul>
        </div>
        <div id="list-taoPackage" class="content scaffold-edit" role="main">
            <h1><g:message code="taoPackage.select.label" args="[entityName]" /></h1>
            <p> There are ${pkgCount} releases </p>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <ul>
                <g:each in="${taoPackageList}">
                <li> <g:link action="show" id="${it.id}" >OCI TAO version ${it.baseVersion()} has ${it.lastPatch} patches.</g:link></li>
                </g:each>
            </ul>
        </div>

    </body>
</html>
