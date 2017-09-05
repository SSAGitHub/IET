<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">

    <xsl:output method="html" encoding="UTF-8"/>
    
    <xsl:param name="title" select="''" as="xs:string"/>
    <xsl:param name="date" select="''" as="xs:string"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>The IET Book Metadata</title>
                <style type="text/css">
body {
	font-family: Tahoma, 'Arial Unicode MS', 'Andale WT', Arial, 'MS UI Gothic', Gulim, SimSun, PMingLiU, Raghu8, sans-serif;
}

h1 {
    color:#008000;
}

table {
    border-width: 1px;
    border-style: solid;
    border-color: #000000;
    border-collapse: collapse;
}

th, td {
    border-width: 1px;
    border-style: solid;
    border-color: #000000;
    font-weight:normal;
}

th {
	font-size: 8pt;
	text-align: center;
	padding: 3px 5px;
	vertical-align: top;
	border: 1px solid #608BB4;
	background-color: #BFD2E2;
}

td {
    font-size: 8pt;
	padding: 3px 5px;
}

td.la {
    text-align: left;
}

td.ca {
    text-align: center;
}

td.ra {
    text-align: right;
}

tr.sum {
    background-color: #BFD2E2;
}
</style>
</head>
            <body>
                <h1>Book Metadata for <xsl:value-of select="$title"/></h1>
                <p><xsl:value-of select="$date"></xsl:value-of></p>
                <table>
                    <tr>
                        <th>Item</th>
                        <th>Value</th>
                    </tr>
                    <xsl:apply-templates/>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="meta-data">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="field">
        <tr>
            <td><xsl:value-of select="@name"/></td>
            <td>
                <xsl:if test="@currency='dollar'"><xsl:text>$</xsl:text></xsl:if>
                <xsl:if test="@currency='pound'"><xsl:text>&#163;</xsl:text></xsl:if>
                <xsl:sequence select="text()"/>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
