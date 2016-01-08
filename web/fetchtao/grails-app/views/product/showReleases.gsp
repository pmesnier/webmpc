<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'name', default: 'Product')}" />
        <title>${name} Downloader</title>
    </head>
    <body>

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
