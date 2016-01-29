<div id="ghAssetOptions" class="content scaffold-edit" role="main">

    <div id="download_link">
        <h3>Files To Download</h3>
        <table id="downloads">
        <tr><th>Download Link</th><th>File size</th><th>Posting date</th></tr>
        <g:each var="p" in="${pkg}">
            <tr><td><a href="${p.targetName}">${p.displayName}</a></td>
                                       <td>${p.fileSize}</td><td>${p.timeStamp}</td>
                                       </tr>
        </g:each>
        </table>
    </div>
</div>
