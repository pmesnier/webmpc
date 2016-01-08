<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'taoProduct.label', default: 'OCI TAO')}" />
        <title>${productName}</title>
    </head>
    <body>
        <a href="#list-taoProduct" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/product/index')}"><g:message code="default.home.label"/></a></li>
                <!--li><g:link class="next" action="pkgConfig" params="'id=' + ${version?.value}" ><g:message code="default.options.label" args="options" /></g:link></li-->
            </ul>
        </div>
        <div id="list-taoRelease" class="content scaffold-list" role="main">
            <h1><g:message code="taoRelease.select.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:each in="${taoReleasesList}">
            <p> ${it.name} is at ${it.rootServerName} with ${it.releases.count} releases.</p>
            </g:each>
        </div>

    </body>
</html>
