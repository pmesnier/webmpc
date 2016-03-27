<div id="featurePicker" class="content scaffold-edit" role="main">
        <g:set var="disabledFeat" value="${wsp.disabledFeatures}" />
        <g:set var="enabledFeat" value="${wsp.enabledFeatures}" />
        <g:set var="wid" value="${wsp.id}" />
    <h2>Additional Features </h2>
    <div id = "featureSelectFrame">
    <g:form controller="workspace" id="${wid}">
        <table id="feature-outer">
        <tr><th>Disabled</th><th></th><th>Enabled</th></tr>
        <tr>
        <td><g:select id="feature-picker" name="dis" from="${disabledFeat}" multiple="true" height="50px" line-height="50px" />
        </td>
        <td><table>
        <tr><td><g:submitToRemote value="enable" action="enable" update="edit-main" controller="workspace" id="${wid}"
                                 image="${resource(dir: 'images', file: 'rightArrow.gif')}" /></td></tr>
        <tr><td><g:submitToRemote value="disable" action="disable" update="edit-main" controller="workspace" id="${wid}"
                                 image="${resource(dir: 'images', file: 'leftArrow.gif')}" /></td></tr>
        </table></td>
        <td><g:select id="feature-picker" name="enab" from="${enabledFeat}" multiple="true" height="50px" line-height="50px" /></td>
        </tr>
        </table>
    </g:form>
    </div>
</div>
