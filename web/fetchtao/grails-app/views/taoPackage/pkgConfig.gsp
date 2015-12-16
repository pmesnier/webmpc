<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="taoDownload" />
        <g:set var="entityName" value="${message(code: 'taoPackage.baseVersion()', default: 'OCI TAO')}" />
        <g:set var="lastPatch" value="${taoPackage.lastPatch}" />
        <title>OCI TAO Downloader</title>
    </head>
    <body>
        <a href="#list-taoPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="download" action="download"><g:message code="default.download.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="pickOptions" class="content scaffold-edit" role="main">
            <h1><g:message code="taoPackage.choosOpts.label" args="[lastPatch]" /></h1>
            <!-- form resource="${this.taoPackage}" method="PUT" -->
               <g:set var="lastpatch" value="${this.taoPackage?.lastPatch}" defaultvalue="10" />
               <g:set var="download" value="Composed download archive name" />
                  <fieldset class="form">
                        <h2>Patch Level</h2>
                        <g:radioGroup name="patchLevel"
                                      values="['p0', 'pn', 'pj', 'ps']"
                                      value="pn"
                                      labels="['base (full source tree)',
                                             'latest, p${lastpatch} (full source tree)',
                                             'jumbo patch, sum of changes from base to latest',
                                             'any intermediate patch, select below']"
                                      />
                        <g:field type="number" name="changesLevel" min="1" max="${lastpatch}" />
                        <br>
                        <h2>Archive contents</h2>
                        <g:radio name="content" value="src" checked="checked" /> Source only<br>
                        <g:radio name="content" value="proj"/> Project files only<br>
                        <g:radio name="content" value="both"/> Source + Project files
                        <br>
                        <h2>File format</h2>
                        <g:radio name="format" value="tgz" checked="checked" /> .tar.gz <br>
                        <g:radio name="format" value="zip"/> .zip </br>

                        <h2>File To Download "${download}"</h2>

                  </fieldset>
                  <fieldset class="buttons">
                       <input class="save" type="submit" value="${message(code: 'default.button.download.label', default: 'Download')}" />
                  </fieldset>
            <!-- /form -->
        </div>
    </body>
</html>
