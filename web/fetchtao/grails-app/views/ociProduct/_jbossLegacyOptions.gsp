<div id="jbossLegacyOptions" class="content scaffold-edit" role="main">
Select package details for JBoss
    <div id="selectPatchLevel" class="content scaffold-edit" role="main">
        <h3>Select the Patch Level Package</h3>
        <p>
        <g:if test="${plList.size() > 1}" >
            <g:select name="patchLevel" from="${plList}" optionValue="name"  optionKey="value"
                value="${plsel ? plsel :''}"
                noSelection="['':'-Select Package-']"
                onChange="${remoteFunction (controller: 'OciProduct',
                                            action: 'updateJbossSelector',
                                            params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(this.value) + \'&content=\' + escape(content.value) + \'&compress=\' + escape(compress.value)',
                                            update: 'jbossLegacyOptions'
                                           )}"/>
        </g:if>
        <g:else>
            <g:select name="patchLevel" from="${plList}" optionValue="name"  optionKey="value"/>
        </g:else>
        </p>
    </div>

    <div id="selectContent" class="content scaffold-edit" role="main">
        <h3>Select the Contents</h3>
        <p>
        <g:if test="${conList.size() > 1}" >
            <g:select name="content" from="${conList}" optionValue="name" optionKey="value"
                value="${consel ? consel :''}"
                noSelection="['':'-Select Content-']"
                onChange="${remoteFunction (controller: 'OciProduct',
                                            action: 'updateJbossSelector',
                                            params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value) + \'&compress=\' + escape(compress.value)',
                                            update: 'jbossLegacyOptions'
                                           )} "/>
        </g:if>
        <g:else>
            <g:select name="content" from="${conList}" optionValue="name"  optionKey="value"/>
        </g:else>
        </p>
    </div>

    <div id="selectCompress" class="content scaffold-edit" role="main">
        <h3>Select the Archive Format</h3>
        <p>
        <g:if test="${cmpList.size() > 1}" >
            <g:select name="compress" from="${cmpList}" optionValue="name" optionKey="value"
                value="${cmpsel ? cmpsel : ''}"
                noSelection="['':'-Select Compression-']"
                onChange="${remoteFunction (controller: 'OciProduct',
                                            action: 'updateJbossSelector',
                                            params: '\'id=\' + escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(content.value) + \'&compress=\' + escape(this.value)',
                                            update: 'jbossLegacyOptions'
                                           )}" />
        </g:if>
        <g:else>
            <g:select name="compress" from="${cmpList}" optionValue="name"  optionKey="value"/>
        </g:else>
        </p>
    </div>

    <div id="download_link">
        <h3>File To Download</h3>
        <g:if test="${basePath != null}" >
            <p> Click on the link to begin your download </p>
            <p><a href="${basePath}${pkg.targetName}">${pkg.targetName}</a></p>
            <p>md5sum ${pkg.md5sum} </p>
            <p>file size =
            <g:if test="${pkg.filesize > 1048576}" > <g:formatNumber number="${pkg.filesize / 1048576}" format="###.##M" /> </g:if>
            <g:elseif test="${pkg.filesize > 1024}" > <g:formatNumber number="${pkg.filesize / 1024}" format="###.##K" /> </g:elseif>
            <g:else> <g:formatNumber number="${pkg.filesize}" format="######b" /> </g:else>
            File posting date: ${pkg.timestamp} </p>
        </g:if >
        <g:else>
            <p> File details will appear when all options are selcted above </p>
        </g:else>
    </div>
</div>
