
<div id="workspaceView" class="content scaffold-create" role="main">
    <g:set var="wspicked" value="${wsp.desiredProject ?: ['Your Selected Projects list here']}" />
    <g:set var="wsimplied" value="${wsp.impliedProject ?: "Projects implied by the above list here"}" />
    <g:set var="wid" value="${wsp.id}" />
     <g:form controller="workspace" action="unchoose" id="${wid}" >
         <div class="panel panel-default">
             <div class="panel-heading">
                 <h3 class="panel-title">
                     Projects You Selected <span class="badge">${wspicked.size()}</span>
                 </h3>
             </div>
             <div class="panel-body panel-height-10">
                 <g:each var="prj" in="${wspicked}" >
                     <label id="projPickTableChoice">
                     <g:checkBox name="${prj}" value="${prj}" />
                     ${prj}
                     </label>
                 </g:each >
             </div>
         </div>

        <p>
        <g:submitToRemote value="remove projects" action="unchoose" update="edit-main" controller="workspace" id="${wid}" />
        </p>
        <h3>Additional Required Libraries</h3>
        <g:select name="implied" from="${wsimplied}" height="80px" line-height="10px" />
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Additional Required Projects <span class="badge">${wsimplied.size()}</span>
                </h3>
            </div>
            <div id="implied"class="panel-body panel-height-10">
                <g:each var="reqlib" in="${wsimplied}" >
                   <p> ${reqlib} </p>
                </g:each >
            </div>
        </div>

    </g:form>
</div>
