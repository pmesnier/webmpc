<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="ddsDownload" />
        <g:set var="entityName" value="${message(code: 'ddsPackage.label', default: 'Open DDS')}" />
        <title>OCI TAO</title>
    </head>
    <body>
        <a href="#list-taoPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <!-- li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li-->
            </ul>
        </div>
        <div id="list-taoPackage" class="content scaffold-list" role="main">
            <h1><g:message code="taoPackage.select.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            ya ya ya
            <g:select id="baseVersion"
                      name="taoPackage.baseVersion"
                      objectKey="id"
                      from="${taoPackageList }"
                       />

            selected = ${version}
            <div class="pagination">
                <g:paginate total="${taoPackageCount ?: 0}" />
            </div>
        </div>
    </body>
</html>
