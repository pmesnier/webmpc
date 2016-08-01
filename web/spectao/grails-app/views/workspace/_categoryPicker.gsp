<div id="categoryPicker" class="content" role="main">
    <g:set var="setname" value="${wsp?.currentSubset?.label}" />
    <g:set var="wid" value="${wsp?.id}" />

    <h2>Project Categories </h2>
    <div class="panel-group" id="accordion">
        <g:each var="topEntry" in="${wsp.product.menu}">
            <g:set var="link" value="${topEntry.label.split()[0]}"/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#${link}">
                        ${topEntry.label}
                    </a>
                    </h4>
                </div>
                <div id="${link}" class="panel-collapse collapse">
                    <div class="panel-body panel-height-10">
                        <ul>
                            <g:each in="${topEntry.subMenu}" status="i" var="sub">
                                <li>
                                    <g:remoteLink action="showProjectPicker" controller="workspace" id="${sub.id}" params="${[wid:wsp.id]}" update="projectPicker">
                                        ${sub.label}
                                    </g:remoteLink>
                                </li>
                            </g:each >
                        </ul>
                    </div>
                </div>
            </div>
        </g:each >
     </div>
</div>
