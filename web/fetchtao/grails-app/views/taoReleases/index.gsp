<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="taoDownload" />
        <g:set var="entityName" value="${message(code: 'taoReleases.label', default: 'OCI TAO')}" />
        <title>${productName}</title>
    </head>
    <body>
        <a href="#list-taoReleases" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <!--li><g:link class="next" action="pkgConfig" params="'id=' + ${baseversion?.value}" ><g:message code="default.options.label" args="options" /></g:link></li-->
            </ul>
        </div>
        <div id="list-taoPackage" class="content scaffold-list" role="main">
            <h1><g:message code="taoPackage.select.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:each in="${taoReleasesList}">
            <p> ${it.name} is at ${it.rootServerName} with ${it.releases.count} releases.</p>
            </g:each>
        </div>

    </body>
</html>
