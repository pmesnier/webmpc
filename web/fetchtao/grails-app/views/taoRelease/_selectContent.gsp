<div id="selectContent" class="content scaffold-edit" role="main">
        <h1>Select the Contents</h1>
Content selector updated, id = ${id}
        <p><g:select name="content"
              from="${contList}"
              optionValue="name"
              optionKey="value"
              noSelection="['':'-Select Content Fool!-']"
              onChange="${remoteFunction (controller: 'TaoRelease',
                                        action: 'populateCompress',
                                        params: '\'id=\'+ escape($rid)+ \'&content=\' + escape(this.value)',
                                        update: 'selectCompress'
                                      )}"/></p>
</div>
