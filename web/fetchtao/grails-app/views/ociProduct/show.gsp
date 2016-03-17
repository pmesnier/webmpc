<!DOCTYPE html>
<html lang="en" class=" cID-286 section-products">
    <head>
        <g:javascript library="jquery" />
        <meta name="layout" content="ociwebmock" />
        <g:set var="entityName" value="${product.name}" />
        <title>${name} Downloader</title>
    </head>
    <body>

<header class="ws-header">
  <div class="container">
    <h1 class="page_title">OCI ${product.name} Downloads</h1>  </div>
</header>
<nav class="ws-pathnav">
  <div class="container">
    <ol class="breadcrumb">
    <li><a href="http://www.ociweb.com">Home</a></li>
    <li><a href="http://www.ociweb.com/products">Products</a></li>
    <li><a href="http://www.ociweb.com/products/${product.name}">${product.name}</a></li>
    <li>Downloads</li>
    </ol>
  </div>
</nav>

<main>
    <div class="container page-sidebar">
        <div class="row">
            <section class="body col-sm-8">
             <p> ${product.descstr} </p>
               <div class="content scaffold-edit" id="choose-version" >
                    <div class="row">
                        <h2><g:message code="ociRelease.select.label" args="[product.name]" /></h2>
                        <p><g:select name="releaseSelector"
                          id="releaseSelector"
                          from="${product.releases}"
                          optionValue="rlsVersion"
                          optionKey="id"
                          value="${latest}"
                          onChange="${remoteFunction (controller: 'ociProduct',
                                                      action: "${product.updateAction}",
                                                      params: '\'id=\' + escape(this.value)  + \'&patchLevel=\' + escape(patchLevel.value) + \'&content=\' + escape(content.value)',
                                                      update: "${product.dynamicDivId}"
                                                    )}"
                            />

                              <g:actionSubmit value="remote test" action="${remoteFunction (controller: 'ociProduct',
                                                                                            action: 'updateOciAssetOptions',
                                                                                            params: '\'id=\' + 1',
                                                                                            update: 'ociAssetOptions'
                                                                                            )}" />

                        <button value="View License" onClick=""> License </button>
                        </p>
                         <div id="${product.dynamicDivId}" >
                            <g:render template="${product.dynamicDivId}" />
                        </div>
                        <div id="viewRelNotes">
                            <g:render template="viewRelNotes" />
                        </div>
                    </div>
                </div>

            </section>
        <aside class="sidebar col-sm-4">
	    <div id="blockStyle15228Sidebar122" class=" ccm-block-styles" >
          <asset:image border="0" class="ccm-image-block" src="${product.logo}" alt="OCI" width="2400" height="1982"/>

        </div>


     </aside>
     </div>
	 </div>
</main>

    </body>
</html>
