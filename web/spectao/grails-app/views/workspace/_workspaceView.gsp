
<div id="workspaceView" class="content scaffold-create" role="main">
    <g:set var="wspicked" value="${wsp.wsuser ?: 'Your Selected Projects list here'}" />
    <g:set var="wsimplied" value="${wsp.wsimplied ?: "Projects implied by the above list here"}" />
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
    <h2>Workspace: ${wsp.name}</h2>
    <p>Selected Projects</p>
    <g:textArea name="userSelectedProjects" readonly="true" value="${wspicked}" rows="8" cols="40" />
    <p>Required Projects</p>
    <g:textArea name="RequiredProjects" readonly="true" value="${wsimplied}" rows="8" cols="40" />
</div>
