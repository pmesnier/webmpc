<div id="pager" class="content scaffold-edit" role="main">
    <g:set var="wid" value="${wsp.id}" />
    <g:set var="prev" value="${session.editPage.prev}" />
    <g:set var="next" value="${session.editPage.next}" />

    <ul class="pager">
    <g:if test="${prev.length()==0}">
        <li class="disabled" ><a href="#">No Previous</a></li>
    </g:if>
    <g:else>
        <li>
            <g:remoteLink action="prevPressed" controller="workspace"
                         params="${[wid:wsp.id]}" update="${templateName}">
                Previous
            </g:remoteLink>
        <li>
    </g:else>
    <g:if test="${next.length()==0}">
        <li class="disabled" ><a href="#">No Next</a></li>
    </g:if>
    <g:elseif test="${templateName != null}">
        <li>
            <g:remoteLink action="nextPressed" controller="workspace"
                         params="${[wid:wsp.id]}"
                         update="${templateName}">
                Next
            </g:remoteLink>
        <li>
    </g:elseif>

    </ul>
</div>
