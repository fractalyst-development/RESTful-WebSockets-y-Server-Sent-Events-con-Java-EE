<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text"/>

  <xsl:template match="/">
    Artículo - <xsl:value-of select="/articulo/titulo"/>
    Autores: <xsl:apply-templates select="/articulo/autores/autor"/>
    Contenido: <xsl:apply-templates select="/articulo/cuerpo"/>
  </xsl:template>

  <xsl:template match="autor">
    - <xsl:value-of select="." />
  </xsl:template>
  
    <xsl:template match="cuerpo">
    - <xsl:value-of select="." />
  </xsl:template>

</xsl:stylesheet>
