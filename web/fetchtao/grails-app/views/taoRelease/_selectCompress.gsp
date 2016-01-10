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
