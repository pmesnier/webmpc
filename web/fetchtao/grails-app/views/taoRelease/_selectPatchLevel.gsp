<div id="selectPatchLevel">
    <div id="choose-collection" class="content scaffold-edit" role="main">
        <g:set var="lastpatch" value="${this.taoRelease?.lastPatch}" defaultvalue="10" />
        <h1>Select the Patch Level Package</h1>
        <p><g:select name="patchLevel"
               from="${plList}"
               noSelection="patch level selector"/> </p>
        <p>Changed files from patch
           <g:field type="number" name="changesLevel"
              value="${lastpatch}" min="1" max="${lastpatch}" default="${lastpatch}" /></p>
    </div>
</div>
