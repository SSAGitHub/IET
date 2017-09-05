<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">
    <xsl:output encoding="UTF-8" method="html"/>

    <xsl:param name="skey" as="xs:string" select="'unset'"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Production Report</title>
                <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
                <script Language="JavaScript">self.moveTo(0,0);self.resizeTo(screen.availWidth*.75,screen.availHeight*.75);</script>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>

        </html>
    </xsl:template>

    <xsl:template match="delivery-report">

        <h1>Digital Library Delivery Report <xsl:sequence select="string(@timestamp)"></xsl:sequence></h1>
        
        <xsl:choose>
            <xsl:when test="article | issue">
                <table width="75%">
                    <tr>
                        <th><xsl:sequence select="string(@type)"/> Id</th>
                        <th>Result</th>
                    </tr>
                    <xsl:apply-templates/>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <p>No deliveries</p>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>

    <xsl:template match="article | issue">
        <tr>
            <xsl:if test="@success='yes'">
                <td class="success" width="25%"><xsl:sequence select="string(@id)"/></td>
                <td class="success" width="75%"><xsl:apply-templates/></td>
            </xsl:if>
            <xsl:if test="@success='no'">
                <td class="fail" width="25%"><xsl:sequence select="string(@id)"/></td>
                <td class="fail" width="75%"><xsl:apply-templates/></td>
            </xsl:if>
        </tr>
    </xsl:template>

</xsl:stylesheet>