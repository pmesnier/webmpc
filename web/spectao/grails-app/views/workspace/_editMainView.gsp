<div id="edit-main" class="container page-sidebar">
        <g:set var="entityName" value="${wsp.product.name}" />
        <g:set var="disabledFeat" value="${wsp.disabledFeatures}" />
        <g:set var="enabledFeat" value="${wsp.enabledFeatures}" />
        <g:set var="menu" value="${wsp.product.menu}" />

        <div class="row">
        <section class="body col-sm-8">
            <h2>Project Categories </h2>
            <ul>
            <g:each var="topEntry" in="${wsp.product.menu}">
                <li>${topEntry.label}
                <ul>
                <g:each in="${topEntry.subMenu}" status="i" var="sub">
                  <li>
                    <g:remoteLink action="showProjectPicker" controller="workspace" id="${sub.id}" params="${[wid:wsp.id]}" update="projectPicker">
                    ${sub.label}
                    </g:remoteLink>
                  </li>
                </g:each >
                </ul>
                </li>
            </g:each >
            </ul>
            <div id="projectPicker" >
                <g:render template="projectPicker" />
            </div>
        </section>
        <aside class="sidebar col-sm-4">
            <div id="featurePicker">
                <g:render template="featurePicker" wsp="${wsp}" />
            </div>
            <div id="workspaceView" >
                <g:render template="workspaceView" wsp="${wsp}" />
            </div>
        </aside>
    </div>
</div>
