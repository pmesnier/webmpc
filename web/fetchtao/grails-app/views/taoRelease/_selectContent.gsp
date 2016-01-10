<div id="selectContent" class="content scaffold-edit" role="main">
        <h1>Select the Contents</h1>
        <p><g:select name="content"
              from="${contList}"
              optionValue="name"
              optionKey="value"
              noSelection="['':'-Select Content-']"
              onChange="${remoteFunction (controller: 'TaoRelease',
                                        action: 'populateCompress',
                                        params: '\'id=\'+ escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                                        update: 'selectCompress'
                                      )}"/></p>
</div>
