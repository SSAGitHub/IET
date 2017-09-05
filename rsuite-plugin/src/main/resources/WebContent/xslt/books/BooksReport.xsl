<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">

    <xsl:output method="html" encoding="UTF-8"/>
    
    <xsl:param name="skey" as="xs:string" select="'unset'"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="books">
        <html>
            <head>
                <title>Production Report</title>                
                <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
                <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/book-report.css?skey=', $skey)}"/>
                <script type="text/javascript" src="{concat('/rsuite/rest/v1/static/iet/scripts/sorttable.js?skey=', $skey)}"/>
            </head>
<body>
                <h1><xsl:text>Books Prog</xsl:text>
                    <xsl:if test="@startDate">
                        <xsl:sequence select="concat(' from ', string(@startDate))"/>
                    </xsl:if>
                    <xsl:if test="@endDate">
                        <xsl:sequence select="concat(' to ', string(@endDate))"/>
                    </xsl:if>
                </h1>
                <table class="sortable">
                    <tr>
                        <th class="sorttable_alpha">Product Code</th>
                        <th class="sorttable_alpha">Book Title</th>
                        <th class="sorttable_alpha">Author</th>
                        <th class="sorttable_alpha">ISBN</th>
                        <th class="sorttable_alpha">eISBN</th>
                        <th class="sorttable_alpha">eProduct Code</th>
                        <th class="sorttable_alpha">Extent</th>
                        <th class="sorttable_alpha">Trim Size</th>
                        <th class="sorttable_alpha">Format</th>
                        <th class="sorttable_alpha">Price (pounds)</th>
                        <th class="sorttable_alpha">Price (dollars)</th>
                        <th class="sorttable_alpha">Initial Print Run</th>
                        <th class="sorttable_alpha">Work Order</th>
                        <th class="sorttable_alpha">Commissioning Editor</th>
                        <th class="sorttable_alpha">Book Series Name</th>
                        <th class="sorttable_alpha">Status</th>
                        <th class="sorttable_alpha">Stage Due Complete</th>
                        <th class="sorttable_alpha">Proposal Presentation Date</th>
                        <th class="sorttable_alpha">Contract Signed Date</th>
                        <th class="sorttable_alpha">Contract TS Delivery Date</th>
                        <th class="sorttable_alpha">Reforecast Pub Date</th>
                        <th class="sorttable_alpha">Actual TS Delivery Date</th>
                        <th class="sorttable_alpha">Actual Pub Date</th>
                        <th class="sorttable_alpha">Date Created</th>
                        <th class="sorttable_alpha">Date Workflow Started</th>
                        <th class="sorttable_alpha">Pub Date in Catalogue</th>
                    </tr>
                    <xsl:for-each select="book">
						<xsl:sort select="string(@stage_due_complete)" order="descending"/>
							<xsl:variable name="row_class">
								<xsl:choose>
									<xsl:when test="string(@row_format) = 'PUBLISHED'">
										<xsl:value-of select="'published'"/>
									</xsl:when>
									<xsl:when test="string(@row_format) = 'INPRODUCTION'">
										<xsl:value-of select="'inproduction'"/>
									</xsl:when>
									<xsl:when test="string(@row_format) = 'MANUSCRIPTRECEIVED'">
										<xsl:value-of select="'manuscript_received'"/>
									</xsl:when>
									<xsl:when test="string(@row_format) = 'CONTRACTED'">
										<xsl:value-of select="'contracted'"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="'default'"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<tr class="{string($row_class)}">
								<td><xsl:value-of select="@product_id"/></td>
        						<xsl:apply-templates/>
								</tr>
					</xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="meta-item">
        <td>
            <xsl:if test="@currency='dollar'"><xsl:text>$</xsl:text></xsl:if>
            <xsl:if test="@currency='pound'"><xsl:text>&#163;</xsl:text></xsl:if>
            <xsl:apply-templates/>
        </td>
    </xsl:template>
       
</xsl:stylesheet>