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
                        <h2>Welcome to the MPC Custom workspace builder for ${product.name} </h2>
                        <p>These pages allow you to select just the projects you want, tailored to your particular feature set,
                        and generating a download package containing the source code files, along with just the build files
                        necessary to build your chosen packages.
                        </p>
                        <p>Workspaces, also known as Solutions, are collections of projects. A project is a buildable specification
                        that describes how to build a single binary, whether that is a library or an executable
                        </p>

                    </section>
                    <aside class="sidebar col-sm-4">
                        <div id="blockStyle15228Sidebar122" class=" ccm-block-styles" >
                          <h2> Create A Workspace </h2>
                          <g:form controller="workspace" action="create">
                          Name your workspace:  <g:textField name="wsName" />
                          <h3><g:actionSubmit action="create" value="Create it!" /><h3>
                          </g:form>
                        </div>
                    </aside>
                 </div>
             </div>
        </main>

    </body>
</html>
