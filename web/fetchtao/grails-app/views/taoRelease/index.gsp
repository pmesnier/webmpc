<!DOCTYPE html>
<html>
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="prodCommon" />
        <g:set var="entityName" value="${message(code: 'taoRelease.label', default: 'OCI TAO')}" />
        <title>OCI TAO Downloader</title>
    </head>
    <body>
        <div id="list-taoRelease" class="content scaffold-edit" role="main">
        </div>
        <div class="content scaffold-edit" id="choose-version" >
            <h1><g:message code="taoRelease.select.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <p><g:select name="rlsVersion"
                  from="${taoReleaseList}"
                  optionValue="rlsVersion"
                  optionKey="id"
                  valueMessagePrefix="OCI Tao Version "
                  noSelection="['':'-Select Release-']"
                  onChange="${remoteFunction (controller: 'TaoRelease',
                                              action: 'populatePatchLevel',
                                              params: '\'id=\' + escape(this.value)',
                                              update: 'selectPatchLevel'
                                            )}"/></p>
        </div>

        <div id="selectPatchLevel" class="content scaffold-edit" role="main">
            <h1>Select the Patch Level Package</h1>
            <p><g:select name="patchLevel"
                    enabled="false"
                   from="${plList}"
                   optionValue="name"
                   optionKey="value"
                   noSelection="['':'-Select Package-']"
                   onChange="${remoteFunction (controller: 'TaoRelease',
                                             action: 'populateContent',
                                             params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(this.value)',
                                             update: 'selectContent'
                                           )}"/></p>
        </div>

        <div id="selectContent" class="content scaffold-edit" role="main">
            <h1>Select the Contents</h1>
            <p><g:select name="content"
                   from="${conList}"
                   optionValue="name"
                   optionKey="value"
                   noSelection="['':'-Select Content-']"
                   onChange="${remoteFunction (controller: 'TaoRelease',
                      action: 'populateCompress',
                      params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                      update: 'selectCompress'
                    )} "/>
            </p>
        </div>

        <div id="selectCompress" class="content scaffold-edit" role="main">
            <h1>Select the Archive Format</h1>
            <p><g:select name="compress"
                   from="${cmpList}"
                   optionValue="name"
                   optionKey="value"
                   noSelection="['':'-Select Compression-']"
                   onChange="${remoteFunction (controller: 'TaoRelease',
                      action: 'taoDownloadLink',
                      params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(content.value) + \'&compress=\' + escape(this.value)',
                      update: 'download_link'
                    )}" />
            </p>
        </div>

        <h1>File To Download</h1>
        <div id="download_link">
        Link to download file is here
        </div>
    </body>
</html>
