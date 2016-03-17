<!DOCTYPE html>
<html lang="en" class=" cID-286 section-products">
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="ociwebmockspec" />
        <g:set var="entityName" value="${product.name}" />
        <title>${name} Workspace Builder</title>
    </head>
    <body>

        <header class="ws-header">
          <div class="container">
            <h1 class="page_title">OCI ${product.name} Workspace Builder</h1>
          </div>
        </header>
        <nav class="ws-pathnav">
          <div class="container">
            <ol class="breadcrumb">
            <li><a href="http://www.ociweb.com">Home</a></li>
            <li><a href="http://www.ociweb.com/products">Products</a></li>
            <li><a href="http://www.ociweb.com/products/${product.name}">${product.name}</a></li>
            <li>Builder</li>
            </ol>
          </div>
        </nav>

        <main>
            <div class="container page-sidebar">
                <div class="row">
                    <section class="body col-sm-8">
                        <h2>Categories</h2>
                        <ul>
                        <g:each var="cat" in="${categoryList}">
                            <li>${cat.name}
                            <ul>
                            <g:each var="sub" in="${cat.subList}">
                                <li> ${sub.name} (${sub.total}) </li>
                            </g:each >
                            </ul>
                            </li>
                        </g:each >
                        </ul>
                    </section>
                    <aside class="sidebar col-sm-4">
                        <div id="blockStyle15228Sidebar122" class=" ccm-block-styles" >
                          <h2> Workspaces </h2>
                          <h3><g:link class="create" action="create"><g:message code="default.createWS.label" args="" /></g:link></h3>
                        </div>
                    </aside>
                 </div>
             </div>
        </main>

    </body>
</html>
