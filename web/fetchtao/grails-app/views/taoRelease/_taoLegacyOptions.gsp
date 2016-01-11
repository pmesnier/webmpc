<div id="taoLegacyOptions" class="content scaffold-edit" role="main">
Starting taoLegacyOptions div
<g:each var="okey" in="${keys}">
      key = ${okey}
    <g:set var="opt" value="${options.get(okey)}" />
      <p>opt = ${opt}</p>
    <g:if test="${opt != null}" >
    <p>opt is not nil</p>
    <h1>${opt.header}</h1>
    <p>
    <g:if test="${opt.nvlist.size() == 1}">
    <g:select name="${opt.selectlabel}"
              from="${opt.nvlist}"
              optionValue="name"
              optionKey="value"
              onChange="${opt.changeFunc}"/>
    </g:if>
    <g:else>
    <g:select name="${opt.selectlabel}"
              from="${opt.nvlist}"
              optionValue="name"
              optionKey="value"
              noSelection="${opt.deflabel}"
              onChange="${opt.changeFunc}" />
    </g:else>
    </p>
    </g:if>
    <g:else>
    test for opt not equal nil
    </g:else>
</g:each>
</div>
