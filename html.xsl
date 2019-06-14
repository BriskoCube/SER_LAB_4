<?xml version = "1.0" encoding = "UTF-8"?>
<xsl:stylesheet version = "1.0"
                xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">
    <xsl:output
            method = "html"
            encoding = "UTF-8"
            doctype-public = "-//W3C//DTD HTML 4.01//EN"
            doctype-system = "http://www.w3.org/TR/html4/strict.dtd"
            indent = "yes"
    />
    <xsl:template match="/">
        <html>
            <head>
                <title>Cours HEIG-VD SER</title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
                <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
                <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
                <style>
                    .flag{
                    max-width:20px;
                    }
                </style>
            </head>
            <body>
                <div class="containers">
                    <div class="row">
                        <xsl:for-each select="countries/element">
                            <div class="col-2 mt-2 mx-auto ">
                                <button class="btn btn-light" data-toggle="modal">
                                    <xsl:attribute name="data-target"><xsl:value-of select="concat('.modal-',alpha3Code)" /></xsl:attribute>
                                    <xsl:value-of select="translations/fr" />
                                    <img class="flag pl-1">
                                        <xsl:attribute name="src">
                                            <xsl:value-of select="flag" />
                                        </xsl:attribute>
                                    </img>
                                </button>
                            </div>
                            <div class="modal fade bd-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                                <xsl:attribute name="class"><xsl:value-of select="concat('modal fade modal-',alpha3Code)" /></xsl:attribute>
                                <div class="modal-dialog modal-md">

                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel"><xsl:value-of select="translations/fr" /></h5>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-6">
                                                    <img class="w-100">
                                                        <xsl:attribute name="src">
                                                            <xsl:value-of select="flag" />
                                                        </xsl:attribute>
                                                    </img>
                                                </div>
                                                <div class="col-6">
                                                    <p>Capitale: <xsl:value-of select="capital" /></p>
                                                    <p>Polulation: <xsl:value-of select="population" /> habitants</p>
                                                    <p>Superficie: <xsl:value-of select="area" /> km<sup>2</sup></p>
                                                    <p>Continent: <xsl:value-of select="region" /></p>
                                                    <p>Sous-Continent: <xsl:value-of select="subregion" /></p>
                                                </div>
                                                <div class="col-12">
                                                    <div class="card bg-light mb-3 w-100">
                                                        <div class="card-header">Langues Parlées</div>
                                                        <div class="card-body">
                                                            <ul class="list-group">
                                                                <xsl:for-each select="languages/element">
                                                                    <li class="list-group-item"><xsl:value-of select="name" /></li>
                                                                </xsl:for-each>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-primary" data-dismiss="modal">Fermer</button>
                                        </div>

                                    </div>

                                </div>
                            </div>
                        </xsl:for-each>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>