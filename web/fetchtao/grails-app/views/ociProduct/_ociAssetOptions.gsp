<div id="ociAssetOptions" class="content scaffold-edit" role="main">

    <h3>Select the Patch Level Package</h3>
    <g:if test="${plSelector.size() > 1}" >
        <g:select name="patchLevel" from="${plSelector}"
            optionValue="name"
            optionKey="value"
            value="${plsel ? plsel : ''}"
            noSelection="['':'-Select Package-']"
            onChange="${remoteFunction (controller: 'OciProduct',
                                        action: "${product.updateAction}",
                                        params: '\'id=\' + escape(releaseSelector.value) + \'&patchLevel=\' + escape(this.value) + \'&content=\' + escape(content.value)',
                                        update: "${product.dynamicDivId}"
                                       )}"/>
    </g:if>
    <g:else>
        <g:select name="patchLevel" from="${plSelector}" optionValue="name"  optionKey="value"/>
    </g:else>
    </p>
    <h3>Select the Contents</h3>
    <p>
    <g:if test="${conList.size() > 1}" >
        <g:select name="content" from="${conList}" optionValue="name" optionKey="value"
            value="${consel ? consel :''}"
            noSelection="['':'-Select Content-']"
            onChange="${remoteFunction (controller: 'OciProduct',
                                        action: "${product.updateAction}",
                                        params: '\'id=\' + escape(releaseSelector.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                                        update: "${product.dynamicDivId}"
                                        )} "/>
    </g:if>
    <g:else>
        <g:select name="content" from="${conList}" optionValue="name"  optionKey="value"/>
    </g:else>
    </p>

    <div id="download_link">
        <h3>Files To Download</h3>
        <table id="downloads">
        <tr><th>Download Link</th><th>File size</th><th>Posting date</th><th>MD5 sum</th></tr>
        <g:each var="p" in="${pkg}">
            <tr><td><a href="${p.targetName}">${p.displayName}</a></td>
                <td>${p.fileSize}</td>
                <td>${p.timeStamp}</td>
                <td>${p.md5sum}</td></tr>
        </g:each>
        </table>
    </div>
</div>
