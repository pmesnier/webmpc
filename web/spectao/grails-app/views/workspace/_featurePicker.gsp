<div id="featurePicker" class="panel panel-default">
    <div class="panel-header" >
        <h2>${pickerTitle}</h2>
    </div>
    <div id="featureSelectFrame" class="panel-body " >
        <g:form controller="workspace" id="${wsp.id}">
            <div class="panel-group" id="accordion_${side}">
                <g:each var="fset" in="${listItems}">
                    <g:set var="link" value="${fset.title.split()[0]}_${side}" />
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion_${side}" href="#${link}">
                                ${fset.title} <span class="badge">${fset.items.size()}</span>
                            </a>
                            </h4>
                        </div>
                        <div id="${link}" class="panel-collapse collapse">
                            <div class="panel-body panel-height-10">
                                <g:each var="feat" in="${fset.items}" >
                                    <label id="projPickTableChoice">
                                    <g:checkBox name="${feat.name}" value="${feat.name}" checked="${feat.checked}"/>
                                                  ${feat.name}
                                    </label>
                                </g:each >
                            </div>
                        </div>
                    </div>
                </g:each >
            </div>
            <p>
            <g:submitToRemote value="${buttonAction}" action="${buttonAction}" update="edit-main" controller="workspace" id="${wid}" />
            </p>
        </g:form>
    </div>
</div>
