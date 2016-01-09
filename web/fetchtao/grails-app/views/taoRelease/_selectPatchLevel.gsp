<div id="selectPatchLevel" class="content scaffold-edit" role="main">

    <h1>Select the Patch Level Package</h1>
    ReleaseId = ${rid}
    <p><g:select name="patchLevel"
            enabled="true"
           from="${plList}"
           optionValue="name"
           optionKey="value"
           noSelection="['':'-Select Package-']"
           onChange="${remoteFunction (controller: 'TaoRelease',
                                     action: 'populateContent',
                                     params: '\'id=\'+ escape(rid) + \'&patchLevel=\' + escape(this.value)',
                                     update: 'selectContent'
                                   )}"/></p>
</div>
