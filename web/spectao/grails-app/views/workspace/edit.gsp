<!DOCTYPE html>
<html lang="en" class=" cID-286 section-products">
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="ociwebmockspec" />
        <g:set var="entityName" value="${product.name}" />
        <title>${name} Workspace Builder</title>
    </head>
    <body>
        <div id="ociTopStripDiv">
            <g:render template="ociTopStrip" />
        </div>

        <main>
            <div id="edit-main" class="container page-sidebar">
                <g:render template="editMainView" categoryList="${categoryList}", wsp="${wsp}" />
             </div>
        </main>

    </body>
</html>
