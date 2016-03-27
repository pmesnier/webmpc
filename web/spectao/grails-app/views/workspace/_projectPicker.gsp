<div id="projectPicker" class="content scaffold-edit" role="main">
    <g:set var="setname" value="${wsp?.currentSubset?.alias}" />
    <g:set var="wid" value="${wsp?.id}" />
    <g:set var="checklist" value="${wsp?.checklist}" />
    <g:if test="${checklist}">
        <h3>${setname} projects</h3>
        <div id = "projectSelectFrame">
        <g:form  controller="workspace" id="${wid}">
            <div class="chex">
            <table id="projectChoices">
            <g:each var="prj" status="i" in="${checklist}">
                <g:if test="${i % wsp.columns == 0}">
                    <tr>
                </g:if>
                <td state="${prj.disabled ? 'disabled' : 'enabled'}">
                    <g:checkBox name="${prj.name}" label="${prj.name}" value="${prj.checked}"  />
                    ${prj.name}
                </td>
                <g:if test="${(i+1) % wsp.columns == 0}">
                    </tr>
                </g:if>
                <g:elseif test="${i == checklist.size() - 1}" >
                    <g:while test="${ (i+1) % wsp.columns != 0}">
                        <%i++%>
                        <td> </td>
                    </g:while>
                    </tr>
                </g:elseif>
            </g:each>
            </table>
            </div><p>
            <g:submitToRemote value="add Projects" update="edit-main" action="updateProject" controller="workspace" id="${wid}" />
            </p>
        </g:form>
        </div>
    </g:if>
    <g:else>
        <h3>Select a project category above!</h3>
    </g:else>
</div>
