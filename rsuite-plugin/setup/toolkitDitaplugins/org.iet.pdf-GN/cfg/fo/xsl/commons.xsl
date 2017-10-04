<?xml version='1.0'?>

<!--
Copyright ? 2004-2006 by Idiom Technologies, Inc. All rights reserved.
IDIOM is a registered trademark of Idiom Technologies, Inc. and WORLDSERVER
and WORLDSTART are trademarks of Idiom Technologies, Inc. All other
trademarks are the property of their respective owners.

IDIOM TECHNOLOGIES, INC. IS DELIVERING THE SOFTWARE "AS IS," WITH
ABSOLUTELY NO WARRANTIES WHATSOEVER, WHETHER EXPRESS OR IMPLIED,  AND IDIOM
TECHNOLOGIES, INC. DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING
BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE AND WARRANTY OF NON-INFRINGEMENT. IDIOM TECHNOLOGIES, INC. SHALL NOT
BE LIABLE FOR INDIRECT, INCIDENTAL, SPECIAL, COVER, PUNITIVE, EXEMPLARY,
RELIANCE, OR CONSEQUENTIAL DAMAGES (INCLUDING BUT NOT LIMITED TO LOSS OF
ANTICIPATED PROFIT), ARISING FROM ANY CAUSE UNDER OR RELATED TO  OR ARISING
OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN IF IDIOM
TECHNOLOGIES, INC. HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

Idiom Technologies, Inc. and its licensors shall not be liable for any
damages suffered by any person as a result of using and/or modifying the
Software or its derivatives. In no event shall Idiom Technologies, Inc.'s
liability for any damages hereunder exceed the amounts received by Idiom
Technologies, Inc. as a result of this transaction.

These terms and conditions supersede the terms and conditions in any
licensing agreement to the extent that such terms and conditions conflict
with those set forth herein.

This file is part of the DITA Open Toolkit project hosted on Sourceforge.net. 
See the accompanying license.txt file for applicable licenses.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:opentopic="http://www.idiominc.com/opentopic" xmlns:exsl="http://exslt.org/common"
    xmlns:opentopic-index="http://www.idiominc.com/opentopic/index"
    xmlns:exslf="http://exslt.org/functions"
    xmlns:opentopic-func="http://www.idiominc.com/opentopic/exsl/function"
    xmlns:dita2xslfo="http://dita-ot.sourceforge.net/ns/200910/dita2xslfo"
    extension-element-prefixes="exsl"
    exclude-result-prefixes="opentopic exsl opentopic-index exslf opentopic-func dita2xslfo xs"
    version="2.0">

    <xsl:template
        match="*[contains(@class, ' topic/body ')]/*[contains(@class, ' cite_margin-d/cite_margin ')]"
        priority="20"/>

    <xsl:template match="*[contains(@class, ' topic/body ')]">
        <xsl:variable name="level" as="xs:integer">
            <xsl:apply-templates select="." mode="get-topic-level"/>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="not(node())"/>
            <xsl:when test="$level = 1">
                <fo:block xsl:use-attribute-sets="body__toplevel">
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:when>
            <xsl:when test="$level = 2">
                <fo:block xsl:use-attribute-sets="body__secondLevel">
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:block xsl:use-attribute-sets="body">
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <xsl:template
        match="*[contains(@class, '- topic/bodydiv ')][*[contains(@class, ' cite_margin-d/cite_margin ')]]">
        <xsl:comment>custom bodydiv/[cite_margin]</xsl:comment>
        <fo:block font-size="10pt" start-indent="-50pt" end-indent="0pt" space-after="0.6em"
            space-before="0.6em">
            <fo:table table-layout="fixed" inline-progression-dimension="100%" start-indent="-50pt"
                end-indent="0pt">
                <fo:table-column column-number="1" column-width="proportional-column-width(13.5)"/>
                <fo:table-column column-number="2" column-width="proportional-column-width(86.5)"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell vertical-align="top">
                            <fo:block text-align="right" start-indent="0pt" end-indent="2pt">
                                <fo:basic-link color="blue" external-destination="url(www.cnn.com)"
                                    font-style="italic">
                                    <xsl:apply-templates select="cite_margin"/>
                                </fo:basic-link>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell vertical-align="top">
                            <fo:block start-indent="2pt" end-indent="0pt">
                                <xsl:apply-templates
                                    select="cite_margin[not(following-sibling::cite_margin)]/following-sibling::*[position() = 1]"
                                    mode="plain"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <xsl:apply-templates
            select="cite_margin[not(following-sibling::cite_margin)]/following-sibling::*[position() = 1]/following-sibling::*"/>

    </xsl:template>

    <xsl:template match="p" mode="plain">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template
        match="cite_margin[parent::body][not(preceding-sibling::*[not(name() = 'cite_margin')])]"/>

    <xsl:template
        match="cite_margin[parent::body][not(preceding-sibling::*[not(name() = 'cite_margin')])]"
        mode="title_cite_margin">
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template match="*" mode="processTopicTitle">
        <xsl:variable name="topic_level"
            select="count(ancestor::*[contains(@class, 'topic/topic')])"/>
        <xsl:choose>
            <!--			<xsl:when test="$topic_level = 1">
				<fo:block padding-top="16.8pt" border="thin solid red">&#160;</fo:block>				
			</xsl:when>-->
            <xsl:when
                test="$topic_level = 2 and parent::*/preceding-sibling::*[contains(@class, 'topic/topic')]">
                <fo:block padding-top="12pt">&#160;</fo:block>
            </xsl:when>
            <xsl:when test="$topic_level = 3">
                <fo:block padding-top="8pt">&#160;</fo:block>
            </xsl:when>
            <xsl:when test="$topic_level = 4">
                <fo:block padding-top="6pt">&#160;</fo:block>
            </xsl:when>
            <xsl:when test="$topic_level = 5">
                <fo:block padding-top="4pt">&#160;</fo:block>
            </xsl:when>
            <xsl:when test="$topic_level = 6">
                <fo:block padding-top="4pt">&#160;</fo:block>
            </xsl:when>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="following-sibling::body[position()=1]/*[position()=1][name()='cite_margin']">		
                <fo:block font-size="10pt" start-indent="-50pt" end-indent="0pt" >		
                    <fo:table table-layout="fixed" inline-progression-dimension="100%"  start-indent="-50pt" end-indent="0pt">
                        <fo:table-column column-number="1" column-width="proportional-column-width(13.5)"/>
                        <fo:table-column column-number="2" column-width="proportional-column-width(86.5)"/>		
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell vertical-align="bottom">
                                    <xsl:for-each select="following-sibling::body[position()=1]/cite_margin">
                                        <fo:block text-align="right" start-indent="0pt" color="blue" font-style="italic">
                                            <xsl:choose>
                                                <xsl:when test="$topic_level=1 or $topic_level=2 or $topic_level=3">
                                                    <xsl:attribute name="end-indent">24pt</xsl:attribute>
                                                    <xsl:attribute name="font-size">10pt</xsl:attribute>
                                                </xsl:when>
                                                <xsl:when test="$topic_level=4 or $topic_level=5 or $topic_level=6">
                                                    <xsl:attribute name="end-indent">4pt</xsl:attribute>
                                                    <xsl:attribute name="font-size">10pt</xsl:attribute>
                                                </xsl:when>											
                                            </xsl:choose>
                                            <xsl:value-of select="."/>
                                        </fo:block>
                                    </xsl:for-each>
                                </fo:table-cell>
                                <fo:table-cell vertical-align="top" padding-left="30pt">
                                    <xsl:variable name="level" as="xs:integer">
                                        <xsl:apply-templates select="." mode="get-topic-level"/>
                                    </xsl:variable>
                                    <xsl:variable name="attrSet1">
                                        <xsl:apply-templates select="." mode="createTopicAttrsName">
                                            <xsl:with-param name="theCounter" select="$level"/>
                                        </xsl:apply-templates>
                                    </xsl:variable>
                                    <xsl:variable name="attrSet2" select="concat($attrSet1, '__content')"/>
                                    <fo:block>
                                        <xsl:call-template name="commonattributes"/>
                                        <xsl:call-template name="processAttrSetReflection">
                                            <xsl:with-param name="attrSet" select="$attrSet1"/>
                                            <xsl:with-param name="path" select="'../../cfg/fo/attrs/commons-attr.xsl'"/>
                                        </xsl:call-template>
                                        <fo:block>
                                            <xsl:call-template name="processAttrSetReflection">
                                                <xsl:with-param name="attrSet" select="$attrSet2"/>
                                                <xsl:with-param name="path" select="'../../cfg/fo/attrs/commons-attr.xsl'"/>
                                            </xsl:call-template>
                                            <xsl:if test="$level = 1">
                                                <fo:marker marker-class-name="current-header">
                                                    <xsl:apply-templates select="." mode="getTitle"/>
                                                </fo:marker>
                                            </xsl:if>
                                            <xsl:if test="$level = 2">
                                                <fo:marker marker-class-name="current-h2">
                                                    <xsl:apply-templates select="." mode="getTitle"/>
                                                </fo:marker>
                                            </xsl:if>
                                            <fo:inline id="{parent::node()/@id}"/>
                                            <fo:inline>
                                                <xsl:attribute name="id">
                                                    <xsl:call-template name="generate-toc-id">
                                                        <xsl:with-param name="element" select=".."/>
                                                    </xsl:call-template>
                                                </xsl:attribute>
                                            </fo:inline>
                                            <!-- added by William on 2009-07-02 for indexterm bug:2815485 start-->
                                            <xsl:call-template name="pullPrologIndexTerms"/>
                                            <!-- added by William on 2009-07-02 for indexterm bug:2815485 end-->
                                            <xsl:apply-templates select="." mode="getTitle"/>
                                        </fo:block>
                                    </fo:block>				
                                </fo:table-cell>
                            </fo:table-row>					
                        </fo:table-body>
                    </fo:table>		
                </fo:block>				
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="level" as="xs:integer">
                    <xsl:apply-templates select="." mode="get-topic-level"/>
                </xsl:variable>
                <xsl:variable name="attrSet1">
                    <xsl:apply-templates select="." mode="createTopicAttrsName">
                        <xsl:with-param name="theCounter" select="$level"/>
                    </xsl:apply-templates>
                </xsl:variable>
                <xsl:variable name="attrSet2" select="concat($attrSet1, '__content')"/>
                <fo:block>
                    <xsl:call-template name="commonattributes"/>
                    <xsl:call-template name="processAttrSetReflection">
                        <xsl:with-param name="attrSet" select="$attrSet1"/>
                        <xsl:with-param name="path" select="'../../cfg/fo/attrs/commons-attr.xsl'"/>
                    </xsl:call-template>
                    <fo:block>
                        <xsl:call-template name="processAttrSetReflection">
                            <xsl:with-param name="attrSet" select="$attrSet2"/>
                            <xsl:with-param name="path" select="'../../cfg/fo/attrs/commons-attr.xsl'"/>
                        </xsl:call-template>
                        <xsl:if test="$level = 1">
                            <fo:marker marker-class-name="current-header">
                                <xsl:apply-templates select="." mode="getTitle"/>
                            </fo:marker>
                        </xsl:if>
                        <xsl:if test="$level = 2">
                            <fo:marker marker-class-name="current-h2">
                                <xsl:apply-templates select="." mode="getTitle"/>
                            </fo:marker>
                        </xsl:if>
                        <fo:inline id="{parent::node()/@id}"/>
                        <fo:inline>
                            <xsl:attribute name="id">
                                <xsl:call-template name="generate-toc-id">
                                    <xsl:with-param name="element" select=".."/>
                                </xsl:call-template>
                            </xsl:attribute>
                        </fo:inline>
                        <!-- added by William on 2009-07-02 for indexterm bug:2815485 start-->
                        <xsl:call-template name="pullPrologIndexTerms"/>
                        <!-- added by William on 2009-07-02 for indexterm bug:2815485 end-->
                        <xsl:apply-templates select="." mode="getTitle"/>
                    </fo:block>
                </fo:block>				
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/note ')]">
        <xsl:variable name="noteType">
            <xsl:choose>
                <xsl:when test="@type = 'other' and @othertype">
                    <xsl:value-of select="@othertype"/>
                </xsl:when>
                <xsl:when test="@type">
                    <xsl:value-of select="@type"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="'note'"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="noteImagePath">
            <xsl:call-template name="insertVariable">
                <xsl:with-param name="theVariableID" select="concat($noteType, ' Note Image Path')"
                />
            </xsl:call-template>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="not($noteImagePath = '')">
                <fo:table xsl:use-attribute-sets="note__table">
                    <fo:table-column xsl:use-attribute-sets="note__image__column"/>
                    <fo:table-column xsl:use-attribute-sets="note__text__column"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell xsl:use-attribute-sets="note__image__entry">
                                <fo:block start-indent="-50pt" end-indent="0pt" text-align="right"
                                    vertical-align="top">
                                    <xsl:choose>
                                        <xsl:when test="cite_margin">
                                            <xsl:apply-templates select="cite_margin"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>&#160;</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <!--<fo:external-graphic src="url({concat($artworkPrefix, $noteImagePath)})" xsl:use-attribute-sets="image"/>-->
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell xsl:use-attribute-sets="note__text__entry">
                                <xsl:choose>
                                    <xsl:when test="cite_margin">
                                        <xsl:variable name="note">
                                            <xsl:copy>
                                                <xsl:copy-of select="@*"/>
                                                <xsl:for-each select="node()">
                                                  <xsl:choose>
                                                  <xsl:when test="self::cite_margin"/>
                                                  <xsl:otherwise>
                                                  <xsl:copy-of select="."/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                </xsl:for-each>
                                            </xsl:copy>
                                        </xsl:variable>
                                        <xsl:apply-templates select="$note" mode="placeNoteContent"
                                        />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:apply-templates select="." mode="placeNoteContent"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="." mode="placeNoteContent"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/p ')]">
        <fo:block xsl:use-attribute-sets="p">
            <xsl:call-template name="commonattributes"/>
            <xsl:if test="@outputclass = 'indent'">
                <xsl:attribute name="margin-left">35pt</xsl:attribute>
                <xsl:attribute name="margin-right">35pt</xsl:attribute>
            </xsl:if>
            <xsl:if test="@status = 'changed'">
                <xsl:call-template name="add_change_bar"/>
            </xsl:if>
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>


    <xsl:template match="*[contains(@class, ' topic/fig ')]/*[contains(@class, ' topic/title ')]">
        <fo:block xsl:use-attribute-sets="fig.title">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>
    <xsl:template
        match="*[contains(@class, ' topic/fig ')]/*[contains(@class, ' topic/title ')]/*[contains(@class, ' hi-d/b ')]/*[contains(@class, ' d4p_simplenum-d/d4pSimpleEnumerator ')]">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/fig ')]">
        <fo:block xsl:use-attribute-sets="fig" keep-together="always">
            <xsl:call-template name="commonattributes"/>
            <xsl:if test="not(@id)">
                <xsl:attribute name="id">
                    <xsl:call-template name="get-id"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
            <!--<xsl:apply-templates select="*[contains(@class,' topic/title ')]"/>-->
        </fo:block>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/fig ')][descendant::cite_margin]" priority="10">
        <fo:block font-size="10pt" start-indent="25pt">
            <fo:table inline-progression-dimension="100%" start-indent="-25pt" table-layout="fixed">
                <fo:table-column column-number="1" column-width="proportional-column-width(10)"/>
                <fo:table-column column-number="2" column-width="proportional-column-width(90)"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block end-indent="3pt" start-indent="-25pt" text-align="right">
                                <!--<xsl:apply-templates select=".//cite_margin"/>-->
                                <fo:basic-link color="blue" external-destination="url(www.cnn.com)"
                                    font-style="italic">
                                    <xsl:apply-templates select=".//cite_margin/node()"/>
                                </fo:basic-link>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-left="27pt">
                            <fo:block xsl:use-attribute-sets="fig" keep-together="always">
                                <xsl:call-template name="commonattributes"/>
                                <xsl:if test="not(@id)">
                                    <xsl:attribute name="id">
                                        <xsl:call-template name="get-id"/>
                                    </xsl:attribute>
                                </xsl:if>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

    <xsl:template match="cite_margin[ancestor::*[contains(@class, ' topic/fig ')]]" priority="15"/>

    <xsl:template match="*[contains(@class, ' topic/title ')]/*[contains(@class, ' topic/data ')]">
        <fo:inline>
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[contains(@class, ' topic/data ')]" mode="insert-text"/>

</xsl:stylesheet>
