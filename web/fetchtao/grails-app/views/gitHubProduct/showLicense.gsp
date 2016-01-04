<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="gitHubCommon" />
        <g:set var="entityName" value="${message(code: 'name', default: 'Product')}" />
        <title>${name} Downloader</title>
    </head>
    <body>
        <a href="#list-taoRelease" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/product/index')}"><g:message code="default.home.label"/></a></li>
                <li><g:link controller="GitHubProduct" action="showSource" id="${gitHubProduct.id}"> Source </g:link></li>
                <li><g:link controller="GitHubProduct" action="showReleases" id="${gitHubProduct.id}"> Releases </g:link></li>
                <li><g:link controller="GitHubProduct" action="showDocs" id="${gitHubProduct.id}"> Documentation </g:link></li>
                <li><g:link controller="GitHubProduct" action="showFAQ" id="${gitHubProduct.id}"> FAQ </g:link></li>
            </ul>
        </div>
        <div class="text" role="main">
        <pre>
        ${license}
        </pre>
        </div>
    </body>
</html>
