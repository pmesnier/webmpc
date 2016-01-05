<div id="downloadLinkTao">
<p> Click on the link to begin your download </p>
    <p><a href="${basePath}${pkg.targetName}">${basePath}${pkg.targetName}</a></p>
    <p>md5sum ${pkg.md5sum} </p>
    <p>file size =
    <g:if test="${pkg.filesize > 1048576}" > <g:formatNumber number="${pkg.filesize / 1048576}" format="###.##M" /> </g:if>
    <g:elseif test="${pkg.filesize > 1024}" > <g:formatNumber number="${pkg.filesize / 1024}" format="###.##K" /> </g:elseif>
    <g:else> <g:formatNumber number="${pkg.filesize / 1024}" format="######b" /> </g:else>
    File posting date: ${pkg.timestamp} </p>
</dif>
