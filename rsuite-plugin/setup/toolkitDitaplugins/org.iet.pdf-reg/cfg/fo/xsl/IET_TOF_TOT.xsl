<?xml version='1.0'?>

<!--

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:exsl="http://exslt.org/common" xmlns:opentopic="http://www.idiominc.com/opentopic"
    xmlns:exslf="http://exslt.org/functions"
    xmlns:opentopic-func="http://www.idiominc.com/opentopic/exsl/function"
    xmlns:ot-placeholder="http://suite-sol.com/namespaces/ot-placeholder"
    xmlns:mml="http://www.w3.org/1998/Math/MathML" extension-element-prefixes="exsl"
    exclude-result-prefixes="xs exsl opentopic exslf opentopic-func ot-placeholder mml"
    version="2.0">


    <xsl:template name="build_tof">
        <fo:table>
            <fo:table-column column-number="1" column-width="15%"/>
            <fo:table-column column-number="2" column-width="80%"/>
            <fo:table-column column-number="1" column-width="5%"/>
            <fo:table-body>
                <xsl:for-each select="$topicFigContent/descendant::fig">
                    <xsl:variable name="current_figure_title">
                        <xsl:copy-of select="title"/>
                    </xsl:variable>
                    <xsl:variable name="title_prefix_text"
                        select="$current_figure_title/title/numcharref/preceding-sibling::text()"/>
                    <xsl:variable name="figure_number"
                        select="$current_figure_title/title/numcharref"/>                   
                    <xsl:variable name="title_non-prefix_nodes">
                        <xsl:for-each select="$current_figure_title/title/numcharref/following-sibling::node()">
                            <xsl:choose>
                                <xsl:when test="self::*">
                                    <xsl:copy-of select="."/>
                                </xsl:when>                                
                                <xsl:when test="self::text()">
                                    <!-- use a variable to normlize the space and make the string
                                        test more reliable -->
                                    <xsl:variable name="text" select="normalize-space(.)"/>
                                    <xsl:choose>
                                        <!-- cover the case where en-dash is used -->
                                        <xsl:when test="starts-with($text, '–')">
                                            <xsl:value-of select="substring-after(., '–')"/>
                                        </xsl:when>                                  
                                        <!-- cover the case where dash is used -->                                        
                                        <xsl:when test="starts-with($text, '-')">
                                            <xsl:value-of select="substring-after(., '-')"/>
                                        </xsl:when>
                                        <!-- cover the case where em-dash is used -->                                        
                                        <xsl:when test="starts-with($text, '—')">
                                            <xsl:value-of select="substring-after(., '—')"/>
                                        </xsl:when>                                        
                                        <xsl:otherwise>
                                            <xsl:value-of select="."/>
                                        </xsl:otherwise>
                                    </xsl:choose>                                    
                                </xsl:when>
                                <xsl:otherwise>SOMETHING WRONG</xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="preceding::fig/title = $current_figure_title"/>
                        <xsl:otherwise>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">
                                            <xsl:text>Fig </xsl:text>
                                            <xsl:value-of
                                                select="$figure_number"
                                            />
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">
                                            <xsl:for-each select="$title_non-prefix_nodes/node()">
                                                <xsl:choose>
                                                    <xsl:when test="self::text()">
                                                        <xsl:value-of select="."/>
                                                    </xsl:when>
                                                    <xsl:when test="self::*">
                                                        <xsl:apply-templates select="."/>
                                                    </xsl:when>                                                    
                                                </xsl:choose>
                                            </xsl:for-each>
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">
                                            <fo:page-number-citation ref-id="{@id}"/>
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <xsl:template name="build_tot">
        <fo:table>
            <fo:table-column column-number="1" column-width="15%"/>
            <fo:table-column column-number="2" column-width="80%"/>
            <fo:table-column column-number="1" column-width="5%"/>
            <fo:table-body>
                <xsl:for-each select="$topicTableContent/descendant::table">
                    <xsl:variable name="current_table_title">
                        <xsl:copy-of select="title"/>
                    </xsl:variable>
                    <xsl:variable name="title_prefix_text"
                        select="$current_table_title/title/numcharref/preceding-sibling::text()"/>
                    <xsl:variable name="table_number"
                        select="$current_table_title/title/numcharref"/>                   
                    <xsl:variable name="title_non-prefix_nodes">
                        <xsl:for-each select="$current_table_title/title/numcharref/following-sibling::node()">
                            <xsl:choose>
                                <xsl:when test="self::*">
                                    <xsl:copy-of select="."/>
                                </xsl:when>                                
                                <xsl:when test="self::text()">
                                    <!-- use a variable to normlize the space and make the string
                                        test more reliable -->
                                    <xsl:variable name="text" select="normalize-space(.)"/>
                                    <xsl:choose>
                                        <!-- cover the case where en-dash is used -->
                                        <xsl:when test="starts-with($text, '–')">
                                            <xsl:value-of select="substring-after(., '–')"/>
                                        </xsl:when>                                  
                                        <!-- cover the case where dash is used -->                                        
                                        <xsl:when test="starts-with($text, '-')">
                                            <xsl:value-of select="substring-after(., '-')"/>
                                        </xsl:when>
                                        <!-- cover the case where em-dash is used -->                                        
                                        <xsl:when test="starts-with($text, '—')">
                                            <xsl:value-of select="substring-after(., '—')"/>
                                        </xsl:when>                                        
                                        <xsl:otherwise>
                                            <xsl:value-of select="."/>
                                        </xsl:otherwise>
                                    </xsl:choose>                                    
                                </xsl:when>
                                <xsl:otherwise>SOMETHING WRONG</xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="preceding::table/title = $current_table_title"/>
                        <xsl:otherwise>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">                                           
                                            <xsl:text>TABLE </xsl:text>
                                            <xsl:value-of
                                                select="$table_number"
                                            />
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">
                                            <xsl:for-each select="$title_non-prefix_nodes/node()">
                                                <xsl:choose>
                                                    <xsl:when test="self::text()">
                                                        <xsl:value-of select="."/>
                                                    </xsl:when>
                                                    <xsl:when test="self::*">
                                                        <xsl:apply-templates select="."/>
                                                    </xsl:when>                                                    
                                                </xsl:choose>
                                            </xsl:for-each>
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block>
                                        <fo:basic-link internal-destination="{@id}">
                                            <fo:page-number-citation ref-id="{@id}"/>
                                        </fo:basic-link>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>

</xsl:stylesheet>
