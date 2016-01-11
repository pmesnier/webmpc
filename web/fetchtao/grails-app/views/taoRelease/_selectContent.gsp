<div id="selectContent" class="content scaffold-edit" role="main">
    <h1>Select the Contents</h1>
    <p>
    <g:if test="${contList.size() == 1}">
    <g:select name="content"
              from="${contList}"
              optionValue="name"
              optionKey="value"
              onChange="${remoteFunction (controller: 'TaoRelease',
                                        action: 'populateCompress',
                                        params: '\'id=\'+ escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                                        update: 'selectCompress'
                                      )}"/>
    </g:if>
    <g:else>
    <g:select name="content"
              from="${contList}"
              optionValue="name"
              optionKey="value"
              noSelection="['':'-Select Content-']"
              onChange="${remoteFunction (controller: 'TaoRelease',
                                        action: 'populateCompress',
                                        params: '\'id=\'+ escape(rlsVersion.value) + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(this.value)',
                                        update: 'selectCompress'
                                      )}"/>
    </g:else>
    </p>
</div>
