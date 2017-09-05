<?xml version="1.0" encoding="UTF-8"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://saxon.sf.net/"
    exclude-result-prefixes="saxon" version="2.0"  >

	<xsl:output indent="yes" />
    <xsl:strip-space elements="*"/>
    
    <!-- ================= begining of the base part of the transform ======================== -->
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- ================ general templates =================== -->
    
    <xsl:template match="*">
        <xsl:copy>
             <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy>
            <xsl:value-of select="."/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
