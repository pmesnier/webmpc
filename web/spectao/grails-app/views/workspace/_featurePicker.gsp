<div id="featurePicker" class="content scaffold-edit" role="main">
        <g:set var="disabledFeat" value="${wsp.disabledFeatures}" />
        <g:set var="enabledFeat" value="${wsp.enabledFeatures}" />
        <g:set var="wid" value="${wsp.id}" />
    <h2>Choose features to enable </h2>
    disabled feature list has ${disabledFeat.size()} enabled feature list has ${enabledFeat.size()}
    <div id = "featureSelectFrame">
    <g:form controller="workspace" id="${wid}">
        <table id="feature-outer">
        <tr><th>Disabled</th><th></th><th>Enabled</th></tr>
        <tr>
        <td><g:select name="dis" from="${disabledFeat}" multiple="true" />
        </td>
        <td><table>
        <tr><td><g:submitToRemote value="enable" action="enable" update="edit-main" controller="workspace" id="${wid}"
                                 image="${resource(dir: 'images', file: 'rightArrow.gif')}" /></td></tr>
        <tr><td><g:submitToRemote value="disable" action="enable" update="edit-main" controller="workspace" id="${wid}" mode="disable"
                                 image="${resource(dir: 'images', file: 'leftArrow.gif')}" /></td></tr>
        </table></td>
        <td><g:select name="enab" from="${enabledFeat}" multiple="true" /></td>
        </tr>
        </table>
    </g:form>
    </div>
</div>
