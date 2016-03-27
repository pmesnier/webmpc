
<div id="workspaceView" class="content scaffold-create" role="main">
    <g:set var="wspicked" value="${wsp.desiredProject ?: ['Your Selected Projects list here']}" />
    <g:set var="wsimplied" value="${wsp.wsimplied ?: "Projects implied by the above list here"}" />
    <g:set var="wid" value="${wsp.id}" />
    <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.workspace}">
    <ul class="errors" role="alert">
        <g:eachError bean="${this.workspace}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
        <g:message error="${error}"/></li>
        </g:eachError>
    </ul>
    </g:hasErrors>
     <g:form controller="workspace" action="disable" id="${wid}" >
        <h2>Workspace: ${wsp.name}</h2>
        <g:select name="userPicked" from="${wspicked}" multiple="true" height="80px" line-height="10px" />
        <p>
        <g:submitToRemote value="remove projects" action="unchoose" update="edit-main" controller="workspace" id="${wid}" />
        </p><h2>Output Options</h2>
        <table pad="20px">
        <tr><th>Generate project files for</th><th>Package Archive uising</th></tr>
        <tr><td>
        <g:select name="output" from="${wsp.product.availableBuildTypes}" multiple="true" height="10" />
        </td><td>
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
