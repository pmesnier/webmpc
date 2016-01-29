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
                        <h2><g:message code="ociRelease.select.label" args="[entityName]" /></h2>
                        <p><g:select name="releaseSelector"
                          id="releaseSelector"
                          from="${product.releases}"
                          optionValue="rlsVersion"
                          optionKey="id"
                          noSelection="['':'-Select Release-']"
                          onChange="${remoteFunction (controller: 'ociProduct',
                                                      action: "${product.updateAction}",
                                                      params: '\'id=\' + escape(this.value)',
                                                      update: "${product.dynamicDivId}"
                                                    )}"
                            />

                        <button value="Release Notes" onClick="findRelNotes()"> Release Notes </button>
                        </p>
                         <div id="${product.dynamicDivId}" >
                            <g:render template="${product.dynamicDivId}" />
                        </div>
                    </div>
                </div>

            </section>
        <aside class="sidebar col-sm-4">
	    <div id="blockStyle15228Sidebar122" class=" ccm-block-styles" >
          <asset:image border="0" class="ccm-image-block" src="${product.logo}" alt="OCI" width="2400" height="1982"/>

        </div>
        <div id="blockStyle6946Main42" class="text-banner ccm-block-styles" >
            <h3><a title="Product License" href="/services/commercial-product-support/">Product License here</a></h3>
            <p><a title="Product License" href="/services/commercial-product-support/">
            Put the license terms for ${product.name} in this panel</a></p>
            <h4><a href="mailto:info@ociweb.com?subject=Support%20Inquiry" target="_blank"><strong>Ask about Commercial Support.</strong></a></h4>
        </div>
        <div id="blockStyle2561Main42" class="text-banner ccm-block-styles" >
            <h3><a title="OCI Training" href="/services/training/"><strong>OCI Training</strong></a></h3>
            <p><a title="OCI Training" href="/services/training/">
               Put readme file here </a>
            </p>
         </div>
     </aside>
     </div>
	 </div>
</main>

    </body>
</html>
