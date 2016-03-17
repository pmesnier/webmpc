
<div id="workspaceView" class="content scaffold-create" role="main">
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
    <g:form >
        <h2>The Workspace so far<h2>
        <h3>Name:</h3> <g:textField name="wsName" value="${wsp.name}" />
        <h3>content:</h3><br/>
        <g:textArea name="WorkspaceView" readonly="true" value="${wsspec}" rows="10" cols="40" />
        <g:submitButton name="next" class="save" action="Next Step"
                        value="${message(code: 'default.button.create.label', default: 'Create')}" />
        <g:submitButton name="discard" class="save" action="discard"
                        value="${message(code: 'default.button.discard.label', default: 'Discard')}" />


    </g:form>
</div>
