<div id="projectPicker" class="content scaffold-edit" role="main">
    <g:set var="setname" value="${wsp?.currentSubset?.alias}" />
    <g:set var="wid" value="${wsp?.id}" />
    <g:if test="${wsp?.checklist}">
        <h3>${setname} projects</h3>
            <g:checkBox name="All" value="${false}" /> Select All

        <div id = "projectSelectFrame">
        <g:form  controller="workspace" id="${wid}">
            <div class="chex">
            <table id="projectChoices">
            <g:each var="prj" status="i" in="${wsp.checklist}">
                <g:if test="${i % 3 == 0}">
                    <g:if test="${i > 0 && i < wsp.checklist.size() - 1 }">
                        </tr>
                    </g:if>
                    <tr>
                </g:if>
                <td state="${prj.disabled ? 'disabled' : 'enabled'}">
                    <g:checkBox name="${prj.name}" label="${prj.name}" value="${prj.checked}" disabled="${prj.disabled}" />
                    ${prj.name}
                </td>
            </g:each>
            </table>
            </div>
            <g:submitToRemote value="update Workspace" update="edit-main" action="updateProject" controller="workspace" id="${wid}" />
        </g:form>
        </div>
    </g:if>
    <g:else>
        <h3>Select a project category above!</h3>
    </g:else>
</div>
