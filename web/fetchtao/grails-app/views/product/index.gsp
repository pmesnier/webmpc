<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>OCI Download Portal</title>
        <style type="text/css" media="screen">
            #status {
                background-color: #eee;
                border: .2em solid #fff;
                margin: 2em 2em 1em;
                padding: 1em;
                width: 12em;
                float: left;
                -moz-box-shadow: 0px 0px 1.25em #ccc;
                -webkit-box-shadow: 0px 0px 1.25em #ccc;
                box-shadow: 0px 0px 1.25em #ccc;
                -moz-border-radius: 0.6em;
                -webkit-border-radius: 0.6em;
                border-radius: 0.6em;
            }

            #status ul {
                font-size: 0.9em;
                list-style-type: none;
                margin-bottom: 0.6em;
                padding: 0;
            }

            #status li {
                line-height: 1.3;
            }

            #status h1 {
                text-transform: uppercase;
                font-size: 1.1em;
                margin: 0 0 0.3em;
            }

            #page-body {
                margin: 0 0.5em 0.5em 18em;
            }

            h2 {
                margin-top: 1em;
                margin-bottom: 0.3em;
                font-size: 1em;
            }

            p {
                line-height: 1.5;
                margin: 0.25em 0;
            }

            #controller-list ul {
                list-style-position: inside;
            }

            #controller-list li {
                line-height: 1.3;
                list-style-position: inside;
                margin: 0.25em 0;
            }

            @media screen and (max-width: 2000px) {
                #status {
                    display: none;
                }

                #page-body {
                    margin: 0 1em 1em;
                }

                #page-body h1 {
                    margin-top: 0;
                }
            }
        </style>
    </head>
    <body>
        <a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="page-body" role="main">
            <h1>Welcome to OCI Download Portal</h1>

            <div id="controller-list" role="navigation">
                <h2>OCI open source products:</h2>
                <ul>
                    <g:each var="p" in="${productList}">
                    <li>${p.name}
                        <g:if test="${p instanceof com.ociweb.oss.GitHubProduct}">
                            <g:link controller="GitHubProduct" action="showSource" id="${p.id}"> Source </g:link>
                        </g:if>
                        <g:else>
                             <g:link controller="Product" action="showSource" id="${p.id}"> Source </g:link>
                        </g:else>
                        <g:if test="${p instanceof com.ociweb.oss.GitHubProduct}">
                            <g:link controller="GitHubProduct" action="showReleases" id="${p.id}"> Releases </g:link>
                        </g:if>
                        <g:else>
                             <g:link controller="Product" action="showReleases" id="${p.id}"> Releases </g:link>
                        </g:else>
                        <g:link controller="Product" action="showLicense" id="${p.id}"> License </g:link>
                        <g:link controller="Product" action="showDocs" id="${p.id}"> Documentation </g:link>
                        <g:link controller="Product" action="showFAQ" id="${p.id}"> FAQ </g:link>
                        <p>
                        ${p.descstr}
                        </p>
                    </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </body>
</html>
