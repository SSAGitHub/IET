<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  exclude-result-prefixes="xlink mml">

  <xsl:output doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd"
    encoding="UTF-8"/>
 
  <xsl:strip-space elements="*"/>

  <!-- Space is preserved in all elements allowing #PCDATA -->
  <xsl:preserve-space
    elements=""/>


 

  
  <!-- ============================================================= -->
  <!--  ROOT TEMPLATE - HANDLES HTML FRAMEWORK                       -->
  <!-- ============================================================= -->

  <xsl:template match="/">
    <html>
      <!-- HTML header -->      
      <body>
        <h1>Preview not available</h1>
      </body>
    </html>
  </xsl:template>


</xsl:stylesheet>
