<header class="ws-header">
    <div class="container">
        <h1 class="page_title">OCI ${product.name} Workspace Builder
            <g:if test="${instanceName != null && instanceName.length() > 0}">
                : ${instanceName}
            </g:if>
        </h1>
    </div>
</header>
<nav class="ws-pathnav">
    <div class="container">
        <ol class="breadcrumb">
            <li><a href="http://www.ociweb.com">Home</a></li>
            <li><a href="http://www.ociweb.com/products">Products</a></li>
            <li><a href="http://www.ociweb.com/products/${product.name}">${product.name}</a></li>
            <li>Builder</li>
            <g:if test="${instanceName != null}">
                <li>${instanceName}</li>
            </g:if>
            <g:if test="${session.editPage != null}" >
                 <li>${session.editPage.curr}</li>
             </g:if>
        </ol>
    </div>
</nav>
