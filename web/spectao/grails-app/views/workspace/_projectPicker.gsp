<div id="projectPicker" class="content scaffold-edit" role="main">
    <g:set var="setname" value="${wsp?.currentSubset?.label}" />
    <g:set var="wid" value="${wsp?.id}" />
    <g:set var="clientChecks" value="${wsp?.clientChecks}" />
    <g:set var="serverChecks" value="${wsp?.serverChecks}" />

    <g:if test="${clientChecks?.size() > 0 || serverChecks?.size() > 0 }">
        <h3>${setname} projects</h3>
        <div id = "projectSelectFrame">
        <g:form  controller="workspace" id="${wid}">
            <div class="chex">
            <table id="projPickTable">
            <tr><th> <b>Client-Side</b> </th>
            <g:if test="${serverChecks.size() > 0}"> <th> <b>Server-Side </b> </th> </g:if> </tr>
            <tr><td>
            <g:each var="prj" in="${clientChecks}">
                <g:if test="${prj.isLabel}">
                <p id="pickLabel"> ${prj.name} </p>
                </g:if>
                <g:else>
                <p id="projPickTableChoice">
                <g:checkBox name="${prj.name}" label="${prj.name}" value="${prj.checked}"/>
                                  ${prj.name}
                </p>
                </g:else>
            </g:each>
            </td>
            <g:if test="${serverChecks.size() > 0}">
            <td>
             <g:each var="prj" in="${serverChecks}">
                            <g:if test="${prj.isLabel}">
                            <p id="projPickTableLabel"> prj.name </p>
                            </g:if>
                            <g:else>
                            <p id="projPickTableChoice">
                            <g:checkBox name="${prj.name}" label="${prj.name}" value="${prj.checked}"/>
                                              ${prj.name}
                            </p>
                            </g:else>
                        </g:each></td>
            </g:if>
            </tr>
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
