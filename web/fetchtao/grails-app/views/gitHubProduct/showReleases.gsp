<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'name', default: 'Product')}" />
        <title>${gitHubProduct.name} Downloader</title>
    </head>
    <body>
        <div class="releases" role="main">
            <p> ${gitHubProduct.descstr} </p>
            <g:form name="gitHubProductDownloadForm" url="[controller:'GitHubProduct', action:'downloadRelease']">
                <fieldset class="form">
                    <g:hiddenField name="id" value="${gitHubProduct.id}" />
                    <p>Select a release  <g:select name="release" from="${rlist}" optionValue="name" /></p>
                    <P>Select a format   <g:select name="bundle" from="['tar.gz','zip']" value="tar.gz" /></P>
               <p>  <g:submitButton name="Download It!" /> </p>
               </fieldset>


            </g:form>
        </div>
    </body>
</html>
