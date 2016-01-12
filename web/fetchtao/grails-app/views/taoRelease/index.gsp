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
                                              action: 'updateTaoSelector',
                                              params: '\'id=\' + escape(this.value)',
                                              update: 'taoLegacyOptions'
                                            )}"/></p>
        </div>
        <div id="taoLegacyOptions" class="content scaffold-edit" role="main">

            <div id="selectPatchLevel" class="content scaffold-edit" role="main">
                <h1>Select the Patch Level Package</h1>
                <p>
                <g:if test="${plList.size() > 1}" >
                    <g:select name="patchLevel" from="${plList}" optionValue="name"  optionKey="value"
                        value="${plsel ? plsel :''}"
                        noSelection="['':'-Select Package-']"
                        onChange="${remoteFunction (controller: 'TaoRelease',
                                                    action: 'updateTaoSelector',
                                                    params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(this.value)',
                                                    update: 'taoLegacyOptions'
                                                   )}"/>
                </g:if>
                <g:else>
                    <g:select name="patchLevel" from="${plList}" optionValue="name"  optionKey="value"/>
                </g:else>
                </p>
            </div>

            <div id="selectContent" class="content scaffold-edit" role="main">
                <h1>Select the Contents</h1>
                <p>
                <g:if test="${conList.size() > 1}" >
                    <g:select name="content" from="${conList}" optionValue="name" optionKey="value"
                        value="${consel ? consel :''}"
                        noSelection="['':'-Select Content-']"
                        onChange="${remoteFunction (controller: 'TaoRelease',
                                                    action: 'updateTaoSelector',
                                                    params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                                                    update: 'taoLegacyOptions'
                                                   )} "/>
                </g:if>
                <g:else>
                    <g:select name="content" from="${conList}" optionValue="name"  optionKey="value"/>
                </g:else>
                </p>
            </div>

            <div id="selectCompress" class="content scaffold-edit" role="main">
                <h1>Select the Archive Format</h1>
                <p>
                <g:if test="${cmpList.size() > 1}" >
                    <g:select name="compress" from="${cmpList}" optionValue="name" optionKey="value"
                        val="${cmpsel ? cmpsel : ''}"
                        noSelection="['':'-Select Compression-']"
                        onChange="${remoteFunction (controller: 'TaoRelease',
                                                    action: 'updateTaoSelector',
                                                    params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(content.value) + \'&compress=\' + escape(this.value)',
                                                    update: 'taoLegacyOptions'
                                                   )}" />
                </g:if>
                <g:else>
                    <g:select name="compress" from="${cmpList}" optionValue="name"  optionKey="value"/>
                </g:else>
                </p>
            </div>

            <div id="download_link">
                <h1>File To Download</h1>
                <p> File details will appear when all options are selcted above </p>

            </div>
        </div>

    </body>
</html>
