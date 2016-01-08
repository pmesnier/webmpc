<!doctype html>
<html lang="en" class="no-js">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title><g:layoutTitle default="OCI Open Source Software Download portal"/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <asset:stylesheet src="application.css"/>
        <asset:javascript src="application.js"/>

        <g:layoutHead/>
    </head>
    <body>
        <div id="taoLogo" role="banner"><a href="http://ociweb.com"><asset:image src="${product.logo}" alt="OCI TAO"/></a>
        ${product.title}
        <a href="#list-taoRelease" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                 <li><a class="home" href="${createLink(uri: '/product/index')}"><g:message code="default.home.label"/></a></li>
                 <li><g:link controller="Product" action="showSource" id="${product.id}"> Source </g:link></li>
                 <li><g:link controller="Product" action="showReleases" id="${product.id}"> Releases </g:link></li>
                 <li><g:link controller="Product" action="showLicense" id="${product.id}"> License </g:link></li>
                 <li><g:link controller="Product" action="showDocs" id="${product.id}"> Documentation </g:link></li>
                 <li><g:link controller="Product" action="showFAQ" id="${product.id}"> FAQ </g:link></li>
           </ul>
        </div>

        </div>
        <g:layoutBody/>
        <div class="footer" role="contentinfo"></div>
        <!-- div id="spinner" class="spinner" style="display:all;"><g:message code="spinner.alt" default="Loading&hellip;"/></div -->
    </body>
</html>
