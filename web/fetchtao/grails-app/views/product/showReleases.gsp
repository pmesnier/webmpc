<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'name', default: 'Product')}" />
        <title>${name} Downloader</title>
    </head>
    <body>
        <a href="#list-taoRelease" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/product/index')}"><g:message code="default.home.label"/></a></li>
                <li><g:link controller="Product" action="showSource" id="${product.id}"> Source </g:link></li>
                <li><g:link controller="Product" action="showLicense" id="${product.id}"> License </g:link></li>
                <li><g:link controller="Product" action="showDocs" id="${product.id}"> Documentation </g:link></li>
                <li><g:link controller="Product" action="showFAQ" id="${product.id}"> FAQ </g:link></li>
            </ul>
        </div>
        <div class="releases" role="main">
            <p> ${product.descstr} </p>
            <p> ${product.name} </p>
            <g:formRemote name="productDownloadForm" url="[controller:'Product', action:'downloadRelease']">
                <fieldset class="form">
                    <p>Select a release  <g:select name="release" from="${rlist}" optionValue="name" /></p>
                    <P>Select a format   <g:select name="format" from="['tar.gz','zip']" /></P>
               </fieldset>
               <p>  <g:submitButton name="Download It!" resource="${product}"/> </p>

            </g:formRemote>
        </div>
    </body>
</html>
