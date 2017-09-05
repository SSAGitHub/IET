<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="html"/>
    
    <xsl:param name="skey" as="xs:string" select="'unset'"/>

    <xsl:template match="/">
        <html>
            <xsl:apply-templates/>          
        </html>
    </xsl:template>
    
    <xsl:template match="articles">
        <head>
            <title>IET Article Summary Report</title>
            <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
            <script type="text/javascript" src="{concat('/rsuite/rest/v1/static/iet/scripts/sorttable.js?skey=', $skey)}"/>
            <script Language="JavaScript">self.moveTo(0,0);self.resizeTo(screen.availWidth*.75,screen.availHeight*.75);</script> 
            <!--<link rel="stylesheet" type="text/css" href="journal-report.css"></link>
            <script type="text/javascript" src="sorttable.js"></script>-->
        </head>
        <body>
            <xsl:choose>
                <xsl:when test="string(@status)='complete'">
                    <h1>IET Article Summary Report -- Completed Article</h1>
                </xsl:when>
                <xsl:when test="string(@status)='all'">
                    <h1>IET Article Summary Report -- All Articles</h1>
                </xsl:when>
                <xsl:otherwise>
                    <h1>IET Article Summary Report -- Articles in Progress</h1>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:choose>
                <xsl:when test="article">
                    <table class="sortable">
                        <tr>
                            <th class="sorttable_ddmm">ArticleId</th>
                            <th class="sorttable_alpha">Category</th>
                            <th class="sorttable_alpha">Article Title</th>
                            <th class="sorttable_alpha">Article type</th>
                            <th class="sorttable_ddmm">Workflow started</th>
                            <th class="sorttable_ddmm">Initial proof received</th>
                            <th class="sorttable_ddmm">Sent to author</th>
                            <th class="sorttable_ddmm">Author corrs rec.</th>
                            <th class="sorttable_ddmm">1st Typesetter corrs req.</th>
                            <th class="sorttable_ddmm">1st updated proof rec.</th>
                            <th class="sorttable_ddmm">2nd Typesetter corrs req.</th>
                            <th class="sorttable_ddmm">2nd Typesetter corrs rec.</th>
                            <th class="sorttable_ddmm">Final files req.</th>
                            <th class="sorttable_ddmm">Final files rec.</th>
                            <th class="sorttable_ddmm">Paper PFP e-first</th>
                            <th class="sorttable_alpha">Issue code</th>
                        </tr>
                        <xsl:apply-templates/>
                    </table>
                </xsl:when>
                <xsl:otherwise>
                    <p>No articles returned</p>
                </xsl:otherwise>
            </xsl:choose>
        </body>
    </xsl:template>

    <xsl:template match="article">
        <tr>
            <td><xsl:sequence select="string(@id)"/></td>
            <td><xsl:sequence select="string(@category)"/></td>
            <td><xsl:sequence select="string(@articleTitle)"/></td>
            <td><xsl:sequence select="string(@articleType)"/></td>
            <td><xsl:sequence select="string(@dtStarted)"/></td>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>
    
    <xsl:template match="event">
        <td><xsl:apply-templates/></td>
    </xsl:template>

</xsl:stylesheet>