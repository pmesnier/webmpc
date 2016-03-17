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
            <h1 class="page_title">OCI ${product.name} Workspace Builder</h1>  </div>
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
                        <h2>Select projects from these categories </h2>
                        <ul>
                        <g:each var="cat" in="${categoryList}">
                            <li>${cat.name} has ${cat.subList.size()} subsets
                            <ul>
                            <g:each in="${cat.subList.mpcSub}" status="i" var="sub">
                              <li>
                                <g:remoteLink action="showPicker" controller="workspace" id="${sub.id}" params="${[wid:workspace.id]}" update="projectPicker">
                                ${sub.alias} ( ${sub.mpcProjects.size()} projects )

                                </g:remoteLink>
                              </li>
                            </g:each >
                            </ul>
                            </li>
                        </g:each >
                        </ul>
                    </section>

                    <aside class="sidebar col-sm-4">
                        <div id="workspaceView" >
                            <g:render template="workspaceView" />
                        </div>
                    </aside>
                 </div>
             </div>
        </main>
        <div id="projectPicker" >
            <g:render template="projectPicker" />
        </div>

    </body>
</html>
