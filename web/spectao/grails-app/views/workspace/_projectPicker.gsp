<div id="projectPicker" class="content scaffold-edit" role="main">

    <g:if test="${checks}">
        <h3>Choose projects from ${setname} </h3>
        sid = ${sid} wid = ${wid}
        <g:form  controller="workspace" action="showPick2" id="${wid}">
            <g:checkBox name="All" value="${false}"
            /> Select All
            <div class="chex">
            <table id="projectChoices">
            <g:each var="prj" status="i" in="${checks}">
                <g:if test="${i % 7 == 0}">
                    <g:if test="${i > 0 && i < checks.size() - 1 }">
                        </tr>
                    </g:if>
                    <tr>
                </g:if>
                <td> <g:checkBox name="${prj.mpc.name}" label="${prj.mpc.name}" value="${prj.desired > 0 || prj.required > 0}" />${prj.mpc.name} </td>
            </g:each>
            </table>
            </div>
            <g:submitToRemote value="update Workspace" action="showPick2" controller="workspace" id="${wid}" update="workspaceView" />
        </g:form>
    </g:if>
    <g:else>
        <h3>Select a project category above!</h3>
    </g:else>
</div>
