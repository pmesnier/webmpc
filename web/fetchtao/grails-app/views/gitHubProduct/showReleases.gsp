<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="gitHubCommon" />
        <g:set var="entityName" value="${message(code: 'name', default: 'Product')}" />
        <title>${gitHubProduct.name} Downloader</title>
    </head>
    <body>
        <a href="#list-taoRelease" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/product/index')}"><g:message code="default.home.label"/></a></li>
                <li><g:link controller="GitHubProduct" action="showSource" id="${gitHubProduct.id}"> Source </g:link></li>
                <li><g:link controller="GitHubProduct" action="showLicense" id="${gitHubProduct.id}"> License </g:link></li>
                <li><g:link controller="GitHubProduct" action="showDocs" id="${gitHubProduct.id}"> Documentation </g:link></li>
                <li><g:link controller="GitHubProduct" action="showFAQ" id="${gitHubProduct.id}"> FAQ </g:link></li>
            </ul>
        </div>
        <div class="releases" role="main">
            <p> ${gitHubProduct.descstr} </p>
            <g:form name="gitHubProductDownloadForm" url="[controller:'GitHubProduct', action:'downloadRelease']">
                <fieldset class="form">
                    <g:hiddenField name="id" value="${gitHubProduct.id}" />
                    <p>Select a release  <g:select name="release" from="${rlist}" optionValue="name" /></p>
                    <P>Select a format   <g:select name="bundle" from="['tar.gz','zip']" value="tar.gz" /></P>
               <p>  <g:submitButton name="Download It!" resource="${release}"/> </p>
               </fieldset>


            </g:form>
        </div>
    </body>
</html>
