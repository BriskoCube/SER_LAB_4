<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />
  <xsl:template match="countries">
    <html>
      <head>
        <style>.flag{max-width:20px;}</style>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous" />
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous" />
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous" />
      </head>
      <body>
        <div class="containers">
          <div class="row">
            <xsl:for-each select="element[region=&quot;Europe&quot; and languages/element/name=&quot;English&quot;]">
              <div class="col-2 mt-2 mx-auto">
                <button class="btn btn-light" data-toggle="modal">
                  <xsl:attribute name="data-target">
                    <xsl:value-of select="concat('.modal-',alpha3Code)" />
                  </xsl:attribute>
                  <xsl:value-of select="translations/fr" />
                  <img class="flag pl-1">
                    <xsl:attribute name="src">
                      <xsl:value-of select="flag" />
                    </xsl:attribute>
                  </img>
                </button>
              </div>
              <div class="modal fade bd-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <xsl:attribute name="class">
                  <xsl:value-of select="concat('modal fade modal-',alpha3Code)" />
                </xsl:attribute>
                <div class="modal-dialog modal-md">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLabel">
                        <xsl:value-of select="translations/fr" />
                      </h5>
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
                          <p>
                            <xsl:value-of select="concat('Capitale: ', capital)" />
                          </p>
                          <p>
                            <xsl:value-of select="concat('Population: ', population, ' habitants')" />
                          </p>
                          <p>
                            <xsl:value-of select="concat('Superficie: ', area, ' km²')" />
                          </p>
                          <p>
                            <xsl:value-of select="concat('Continent: ', region)" />
                          </p>
                          <p>
                            <xsl:value-of select="concat('Sous-continent: ', subregion)" />
                          </p>
                        </div>
                        <div class="col-12">
                          <div class="card bg-light mb-3 w-100">
                            <div class="card-header">Langues Parlées</div>
                            <div class="card-body">
                              <ul class="list-group">
                                <xsl:for-each select="languages/element">
                                  <li class="list-group-item">
                                    <xsl:value-of select="name" />
                                  </li>
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
