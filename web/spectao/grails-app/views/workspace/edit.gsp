<!DOCTYPE html>
<html id="edit-main" lang="en" class=" cID-286 section-products">
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="ociwebmockspec" />
        <g:set var="entityName" value="${product.name}" />
        <title>${name} Workspace Builder</title>
    </head>
    <body>
        <div id="${templateName}">
        <g:render template="${templateName}" categoryList="${categoryList}", wsp="${wsp}" />
        </div>
    </body>
</html>
