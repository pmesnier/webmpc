<div id="projectPicker" class="content" role="main">
    <g:set var="setname" value="${wsp?.currentSubset?.label}" />
    <g:set var="widstr" value="${wsp?.id}" />
    <g:set var="picks" value="${wsp?.currentSubset?.pickList}" />

    <g:if test="${picks?.size() > 0 }">
        <h4>${setname}</h4>
        <div id = "projectSelectFrame">
            <div class="chex">
                <table id="projPickTable">
                    <g:each status="ndx" var="pickSet" in="${picks}" >
                        <tr><th> <b>${pickSet.label}</b> </th></tr>
                        <tr><td>
                        <g:each var="prj" in="${wsp.getCheckedProjects(ndx)}">
                            <g:set var="labeltext" value="${prj.name}" />
                            <g:if test="${prj.desired > 0}" >
                                <label state="desired" />
                            </g:if>
                            <g:elseif test="${prj.required > 0}" >
                                <label state="required" />
                                <g:set var="labeltext" value="${prj.name}.${prj.required}" />
                            </g:elseif>
                            <g:else>
                                <label state="none">
                            </g:else>
                                <g:checkBox name="${pickSet.label}:${prj.name}" value="${prj.name}" checked="${prj.desired > 0}"
                                            onChange="myRemote(${widstr}, this.name, this.checked)"
                                />
                                ${labeltext}
                            </label>
                        </g:each>
                        </td></tr>
                    </g:each>
                </table>
                Total Projects selected and implied = ${wsp.projects.size()}
            </div>
        </div>
    </g:if>
    <g:else>
        <h3>Select a project category</h3>
    </g:else>
</div>
<script>
function myRemote(widval, name, checked) {
    k = checked ? "checked" : "unchecked";
    v = name;
    datastr = "wid=" + escape(widval) + "&" + escape(k) + "=" + escape(v);

    jQuery.ajax( {type:'GET',
                 id: widval,
                 data: datastr,
                 url:'/workspace/updateProject',
                 update:'projectPicker',
                 success:function(data,status){jQuery('#projectPicker').html(data);},
                 error:function(XMLHttpRequest,textStatus,errorThrown){}});
 }

</script>
