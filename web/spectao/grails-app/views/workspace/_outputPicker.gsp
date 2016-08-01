
<div id="outputPicker" class="content scaffold-create" role="main">
     <g:form controller="workspace" action="disable" id="${wid}" >
        <h2>Output Options</h2>
        <table pad="20px">
        <tr><th>Select Project Type</th><th></th><th>Select Archive Format</th></tr>
        <tr><td>
        <g:select name="output" from="${wsp.product.availableBuildTypes}" multiple="true" height="10" />
        </td><td>&nbsp;<td>
        <g:radioGroup name="archive"
            values="${wsp.product.availableArchiveTypes}"
            labels="${wsp.product.availableArchiveTypes}"
            value= "tar.gz">
            <p>${it.radio} ${it.label}</p>
        </g:radioGroup>
        </td></tr></table><p>
        <g:submitToRemote value="BUILD IT" action="trigger" update="edit-main" controller="workspace" id="${wid}" />
        </p>
    </g:form>
</div>
