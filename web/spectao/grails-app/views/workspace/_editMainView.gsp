<body>
    <g:set var="instanceName" value="${wsp.name}" />
    <g:set var="page" value="${session.editPage.curr}" />
    <div id="ociTopStripDiv">
        <g:render template="ociTopStrip" />
    </div>

        <div class="container page-sidebar">
            <div class="row">
                <div class="panel col-sm-9">
                    <div class="row">
                        <g:if test="${page == "projects"}" >
                            <div id="editLeft" class="panel col-sm-5 panel-height-40">
                                    <g:render template="categoryPicker" wsp="${wsp}" />
                            </div>
                            <div id="editRight" class="panel col-sm-7  panel-height-40">
                                    <g:render template="projectPicker" wsp="${wsp}" />
                            </div>
                        </g:if>
                        <g:elseif test="${page == "features"}">
                            <div id="editLeft" class="panel col-sm-6">
                                <g:set var="side" value="left" />
                                <g:set var="listItems" value="${wsp.disabledFeatures}" />
                                <g:set var="pickerTitle" value="Disabled MPC Features" />
                                <g:set var="buttonName"  value="Enable Selected Features"  />
                                <g:set var="buttonAction" value="enable" />
                                <g:render template="featurePicker"/>
                            </div>
                            <div id="editRight" class="panel col-sm-6">
                                <g:set var="side" value="right" />
                                <g:set var="listItems" value="${wsp.enabledFeatures}" />
                                <g:set var="pickerTitle" value="Enabled MPC Features" />
                                <g:set var="buttonName" value="Disable Selected Features"  />
                                <g:set var="buttonAction" value="disable" />
                                <g:render template="featurePicker" />
                            </div>
                        </g:elseif>
                        <g:elseif test="${page == "workspace"}">
                            <div id="editLeft" class="panel col-sm-5">
                                <g:render template="workspaceView" wsp="${wsp}" />
                            </div>
                            <div id="editRight" class="panel col-sm-7">
                                <g:render template="outputPicker" wsp="${wsp}" />
                            </div>
                        </g:elseif>
                    </div>
                    <div class="row">
                        <g:render template="pager" wsp="${wsp}" />
                    </div>
                </div>
                <div class="panel col-sm-3">
                    <g:render template="infoView" wsp="${wsp}" pages="${pages}" />
                </div>
            </div>
        </div>
</body>
