<?xml version='1.0'?>

<!-- 
Copyright © 2004-2006 by Idiom Technologies, Inc. All rights reserved. 
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

<!-- Elements for steps have been relocated to task-elements.xsl -->
<!-- Templates for <dl> are in tables.xsl -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:mml="http://www.w3.org/1998/Math/MathML"
    version="2.0">

    <xsl:template match="*[contains(@class,' topic/linklist ')]/*[contains(@class,' topic/title ')]">
        <fo:block xsl:use-attribute-sets="linklist.title">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <!--Lists-->
    <xsl:template match="*[contains(@class, ' topic/ul ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>
        <xsl:choose>
            <!-- if there is an odd number of insert start PIs and an even number of insert end PIs
                then this element is "nested" inside one and the entire contents of the paragraph need to
                be marked as inserted -->
            <xsl:when
                test="
                ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                <fo:block xsl:use-attribute-sets="p common.border__right">
                    <xsl:attribute name="color">green</xsl:attribute>
                    <xsl:attribute name="font-weight">bold</xsl:attribute>
                    <fo:list-block provisional-label-separation="4mm"
                        provisional-distance-between-starts="7mm">
                        <xsl:call-template name="commonattributes"/>
                        <xsl:apply-templates/>
                    </fo:list-block>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:list-block provisional-label-separation="4mm"
                    provisional-distance-between-starts="7mm">
                    <xsl:call-template name="commonattributes"/>
                    <xsl:apply-templates/>
                </fo:list-block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/ol ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>
        <xsl:choose>
            <!-- if there is an odd number of insert start PIs and an even number of insert end PIs
                then this element is "nested" inside one and the entire contents of the paragraph need to
                be marked as inserted -->
            <xsl:when
                test="
                ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                <fo:block xsl:use-attribute-sets="p common.border__right">
                    <xsl:attribute name="color">green</xsl:attribute>
                    <xsl:attribute name="font-weight">bold</xsl:attribute>
                    <fo:list-block provisional-label-separation="4mm"
                        provisional-distance-between-starts="11mm">
                        <xsl:call-template name="commonattributes"/>
                        <xsl:apply-templates/>
                    </fo:list-block>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <!-- SA00 list labels -->
                <fo:list-block provisional-label-separation="4mm"
                    provisional-distance-between-starts="11mm">
                    <xsl:call-template name="commonattributes"/>
                    <xsl:apply-templates/>
                </fo:list-block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template
        match="*[contains(@class, ' topic/ul ')][
        @outputclass='square' or
        @outputclass='triangle' or
        @outputclass='hyphen-long' or
        @outputclass='hyphen-short' or
        @outputclass='no_label']/*[contains(@class, ' topic/li ')]"
        priority="10">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block">
                <xsl:with-param name="context">list</xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:variable name="delete_content_only">
            <xsl:call-template name="determine_delete_content_only"/>
        </xsl:variable>
        <fo:list-item xsl:use-attribute-sets="ul.li">
            <xsl:if test="ancestor::dd and parent::ul/@outputclass = 'hyphen-long'">
                <xsl:attribute name="start-indent">16pt</xsl:attribute>
            </xsl:if>
            <fo:list-item-label xsl:use-attribute-sets="ul.li__label">
                <fo:block xsl:use-attribute-sets="ul.li__label__content">
                    <xsl:choose>
                        <xsl:when
                            test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                        <xsl:when test="$delete_content_only = 'delete content only: yes'">
                            <xsl:attribute name="color">red</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                    </xsl:choose>
                    <fo:inline>
                        <xsl:call-template name="commonattributes"/>
                    </fo:inline>
                    <xsl:choose>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ul ')][@outputclass='square']">
                            <xsl:text>&#x25a0;</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ul ')][@outputclass='triangle']">
                            <xsl:text>&#x25ba;</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ul ')][@outputclass='hyphen-long']">
                            <xsl:text>&#x2014;</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ul ')][@outputclass='hyphen-short']">
                            <xsl:text>-</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ul ')][@outputclass='no_label']">
                            <xsl:text> </xsl:text>
                        </xsl:when>
                    </xsl:choose>
                </fo:block>
            </fo:list-item-label>

            <fo:list-item-body xsl:use-attribute-sets="ul.li__body">
                <xsl:choose>
                    <!-- added to provide IET change bar -->
                    <xsl:when
                        test="descendant-or-self::processing-instruction('oxy_insert_start') or
                        descendant-or-self::processing-instruction('oxy_delete') or
                        descendant-or-self::processing-instruction('oxy_comment') or
                        ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                        <fo:block xsl:use-attribute-sets="ul.li__content common.border__right">
                            <xsl:if
                                test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                                <xsl:attribute name="color">green</xsl:attribute>
                                <xsl:attribute name="font-weight">bold</xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates mode="filter_for_PIs"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block xsl:use-attribute-sets="ul.li__content">
                            <xsl:apply-templates/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:list-item-body>

        </fo:list-item>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/ul ')]/*[contains(@class, ' topic/li ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block">
                <xsl:with-param name="context">list</xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:variable name="delete_content_only">
            <xsl:call-template name="determine_delete_content_only"/>
        </xsl:variable>
        <fo:list-item xsl:use-attribute-sets="ul.li">
            <fo:list-item-label xsl:use-attribute-sets="ul.li__label">
                <fo:block xsl:use-attribute-sets="ul.li__label__content">
                    <xsl:choose>
                        <xsl:when
                            test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                        <xsl:when test="$delete_content_only = 'delete content only: yes'">
                            <xsl:attribute name="color">red</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                    </xsl:choose>
                    <fo:inline>
                        <xsl:call-template name="commonattributes"/>
                    </fo:inline>
                    <xsl:call-template name="insertVariable">
                        <xsl:with-param name="theVariableID" select="'Unordered List bullet'"/>
                    </xsl:call-template>
                </fo:block>
            </fo:list-item-label>

            <fo:list-item-body xsl:use-attribute-sets="ul.li__body">
                <xsl:choose>
                    <!-- added to provide IET change bar -->
                    <xsl:when
                        test="descendant-or-self::processing-instruction('oxy_insert_start') or
                        descendant-or-self::processing-instruction('oxy_delete') or
                        descendant-or-self::processing-instruction('oxy_comment_start') or
                        ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                        <fo:block xsl:use-attribute-sets="ul.li__content common.border__right">
                            <xsl:if
                                test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                                <xsl:attribute name="color">green</xsl:attribute>
                                <xsl:attribute name="font-weight">bold</xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates mode="filter_for_PIs"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block xsl:use-attribute-sets="ul.li__content">
                            <xsl:apply-templates/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:list-item-body>

        </fo:list-item>
    </xsl:template>

    <xsl:template
        match="*[contains(@class, ' topic/ol ')]
        [@outputclass='arabicParen' or
        @outputclass='letterUpperCase' or
        @outputclass='letterLowerCase' or
        @outputclass='letterLowerCaseParen' or
        @outputclass='romanUpperCase' or
        @outputclass='romanLowerCase' or
        @outputclass='romanLowerCaseParen']
        /*[contains(@class, ' topic/li ')]"
        priority="5">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block">
                <xsl:with-param name="context">list</xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:variable name="delete_content_only">
            <xsl:call-template name="determine_delete_content_only"/>
        </xsl:variable>
        <fo:list-item xsl:use-attribute-sets="ol.li">
            <fo:list-item-label xsl:use-attribute-sets="ol.li__label">
                <fo:block xsl:use-attribute-sets="ol.li__label__content">
                    <xsl:choose>
                        <xsl:when
                            test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                        <xsl:when test="$delete_content_only = 'delete content only: yes'">
                            <xsl:attribute name="color">red</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                    </xsl:choose>
                    <fo:inline>
                        <xsl:call-template name="commonattributes"/>
                    </fo:inline>
                    <xsl:choose>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='arabicParen']">
                            <xsl:text>(</xsl:text>
                            <xsl:number format="1"/>
                            <xsl:text>)</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='letterUpperCase']">
                            <xsl:number format="A"/>
                            <xsl:text>.</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='letterLowerCase']">
                            <xsl:number format="a"/>
                            <xsl:text>.</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='letterLowerCaseParen']">
                            <xsl:text>(</xsl:text>
                            <xsl:number format="a"/>
                            <xsl:text>)</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanUpperCase']">
                            <xsl:number format="I"/>
                            <xsl:text>.</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanLowerCase']">
                            <xsl:number format="i"/>
                            <xsl:text>.</xsl:text>
                        </xsl:when>
                        <xsl:when
                            test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanLowerCaseParen']">
                            <xsl:text>(</xsl:text>
                            <xsl:number format="i"/>
                            <xsl:text>)</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:number format="1"/>
                            <xsl:text>.</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
            </fo:list-item-label>

            <fo:list-item-body xsl:use-attribute-sets="ol.li__body">
                <xsl:choose>
                    <!-- added to provide IET change bar -->
                    <xsl:when
                        test="descendant-or-self::processing-instruction('oxy_insert_start') or
                        descendant-or-self::processing-instruction('oxy_delete') or
                        descendant-or-self::processing-instruction('oxy_comment_start') or
                        ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                        <fo:block xsl:use-attribute-sets="ol.li__content common.border__right">
                            <xsl:if
                                test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                                <xsl:attribute name="color">green</xsl:attribute>
                                <xsl:attribute name="font-weight">bold</xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates mode="filter_for_PIs"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block xsl:use-attribute-sets="ol.li__content">
                            <xsl:apply-templates/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:list-item-body>

        </fo:list-item>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/ol ')]/*[contains(@class, ' topic/li ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block">
                <xsl:with-param name="context">list</xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:variable name="delete_content_only">
            <xsl:call-template name="determine_delete_content_only"/>
        </xsl:variable>
        <fo:list-item xsl:use-attribute-sets="ol.li">
            <fo:list-item-label xsl:use-attribute-sets="ol.li__label">
                <fo:block xsl:use-attribute-sets="ol.li__label__content">
                    <xsl:choose>
                        <xsl:when
                            test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                        <xsl:when test="$delete_content_only = 'delete content only: yes'">
                            <xsl:attribute name="color">red</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:when>
                    </xsl:choose>
                    <fo:inline>
                        <xsl:call-template name="commonattributes"/>
                    </fo:inline>
                    <xsl:call-template name="insertVariable">
                        <xsl:with-param name="theVariableID" select="'Ordered List Number'"/>
                        <xsl:with-param name="theParameters">
                            <number>
                                <xsl:choose>
                                    <xsl:when
                                        test="parent::*[contains(@class, ' topic/ol ')]/parent::*[contains(@class, ' topic/li ')]/parent::*[contains(@class, ' topic/ol ')]">
                                        <xsl:number format="a"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:number/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </number>
                        </xsl:with-param>
                    </xsl:call-template>
                </fo:block>
            </fo:list-item-label>

            <fo:list-item-body xsl:use-attribute-sets="ol.li__body">
                <xsl:choose>
                    <!-- added to provide IET change bar -->
                    <xsl:when
                        test="descendant-or-self::processing-instruction('oxy_insert_start') or
                        descendant-or-self::processing-instruction('oxy_delete') or
                        descendant-or-self::processing-instruction('oxy_comment') or
                        ($nested_insert_start = 'odd' and $nested_insert_end = 'even')">
                        <fo:block xsl:use-attribute-sets="ol.li__content common.border__right">
                            <xsl:if
                                test="$nested_insert_start = 'odd' and $nested_insert_end = 'even'">
                                <xsl:attribute name="color">green</xsl:attribute>
                                <xsl:attribute name="font-weight">bold</xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates mode="filter_for_PIs"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block xsl:use-attribute-sets="ol.li__content"><xsl:apply-templates/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:list-item-body>
        </fo:list-item>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/li ')]/*[contains(@class, ' topic/itemgroup ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>
        <fo:block xsl:use-attribute-sets="li.itemgroup">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/sl ')]">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>
        <fo:list-block xsl:use-attribute-sets="sl">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates/>
        </fo:list-block>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/sl ')]">
        <fo:list-block xsl:use-attribute-sets="sl">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates/>
        </fo:list-block>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/sl ')]/*[contains(@class, ' topic/sli ')]">
        <fo:list-item xsl:use-attribute-sets="sl.sli">
            <fo:list-item-label xsl:use-attribute-sets="sl.sli__label">
                <fo:block xsl:use-attribute-sets="sl.sli__label__content">
                    <fo:inline>
                        <xsl:call-template name="commonattributes"/>
                    </fo:inline>
                </fo:block>
            </fo:list-item-label>

            <fo:list-item-body xsl:use-attribute-sets="sl.sli__body">
                <fo:block xsl:use-attribute-sets="sl.sli__content">
                    <xsl:apply-templates/>
                </fo:block>
            </fo:list-item-body>

        </fo:list-item>
    </xsl:template>

    <!-- the following templates in mode "IET_defintions" are for processing IET Defintions (!) -->
    <xsl:template match="*[contains(@class, ' topic/dlentry ')]" mode="IET_definitions">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>
        <xsl:choose>
            <!-- if there is an odd number of insert start PIs and an even number of insert end PIs
                then this element is "nested" inside one and the entire contents of the paragraph need to
                be marked as inserted -->
            <xsl:when
                test="
                ($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
                ($nested_insert_start = 'even' and $nested_insert_end = 'odd') or
                child::dt/child::processing-instruction('oxy_insert_start') or
                child::dt/child::processing-instruction('oxy_delete') or
                child::dd/child::processing-instruction('oxy_insert_start') or
                child::dd/child::processing-instruction('oxy_delete')">
                <fo:block xsl:use-attribute-sets="p common.border__right">
                    <xsl:call-template name="commonattributes"/>
                    <xsl:if
                        test="($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
                        ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
                        <xsl:attribute name="color">green</xsl:attribute>
                        <xsl:attribute name="font-weight">bold</xsl:attribute>
                    </xsl:if>
                    <xsl:comment>dlentry block</xsl:comment>
                    <xsl:choose>
                        <xsl:when
                            test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_bullet'] or
                            ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_dash']">
                            <fo:list-block provisional-label-separation="6pt">
                                <xsl:attribute name="start-indent">
                                    <xsl:choose>
                                        <xsl:when
                                            test="ancestor::*[contains(@class,
                                            'topic/bodydiv')][@outputclass='definitions_level_2_bullet']"
                                            >6pt</xsl:when>
                                        <xsl:when
                                            test="ancestor::*[contains(@class,
                                            'topic/bodydiv')][@outputclass='definitions_level_2_dash']"
                                            >18pt</xsl:when>
                                        <xsl:otherwise>6pt</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                                <fo:list-item>
                                    <fo:list-item-label end-indent="label-end()">
                                        <fo:block>
                                            <xsl:choose>
                                                <xsl:when
                                                  test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_bullet']">
                                                  <xsl:call-template name="insertVariable">
                                                  <xsl:with-param name="theVariableID"
                                                  select="'Unordered List bullet'"/>
                                                  </xsl:call-template>
                                                </xsl:when>
                                                <xsl:when
                                                  test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_dash']">
                                                  <xsl:text>-</xsl:text>
                                                </xsl:when>
                                            </xsl:choose>
                                        </fo:block>
                                    </fo:list-item-label>
                                    <fo:list-item-body start-indent="body-start()">
                                        <fo:block>
                                            <xsl:apply-templates mode="IET_definitions"/>
                                        </fo:block>
                                    </fo:list-item-body>
                                </fo:list-item>
                            </fo:list-block>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates mode="IET_definitions"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when
                        test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_bullet'] or
                ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_dash']">
                        <fo:list-block provisional-label-separation="6pt">
                            <xsl:attribute name="start-indent">
                                <xsl:choose>
                                    <xsl:when
                                        test="ancestor::*[contains(@class,
                                'topic/bodydiv')][@outputclass='definitions_level_2_bullet']"
                                        >6pt</xsl:when>
                                    <xsl:when
                                        test="ancestor::*[contains(@class,
                                'topic/bodydiv')][@outputclass='definitions_level_2_dash']"
                                        >18pt</xsl:when>
                                    <xsl:otherwise>6pt</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <fo:list-item>
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>
                                        <xsl:choose>
                                            <xsl:when
                                                test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_bullet']">
                                                <xsl:call-template name="insertVariable">
                                                  <xsl:with-param name="theVariableID"
                                                  select="'Unordered List bullet'"/>
                                                </xsl:call-template>
                                            </xsl:when>
                                            <xsl:when
                                                test="ancestor::*[contains(@class, 'topic/bodydiv')][@outputclass='definitions_level_2_dash']">
                                                <xsl:text>-</xsl:text>
                                            </xsl:when>
                                        </xsl:choose>
                                    </fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block>
                                        <xsl:apply-templates mode="IET_definitions"/>
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </fo:list-block>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="IET_definitions"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/dt ')]" mode="IET_definitions">
        <xsl:choose>
            <xsl:when
                test="child::processing-instruction('oxy_insert_start') or
                child::processing-instruction('oxy_delete') or
                child::processing-instruction('oxy_comment_start')">
                <fo:inline font-weight="bold">
                    <xsl:apply-templates mode="filter_for_PIs"/>
                </fo:inline>
            </xsl:when>
            <xsl:otherwise>
                <fo:inline font-weight="bold">
                    <xsl:apply-templates/>
                </fo:inline>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/dd ')]" mode="IET_definitions">
        <xsl:choose>
            <xsl:when
                test="child::processing-instruction('oxy_insert_start') or
                child::processing-instruction('oxy_delete') or
                child::processing-instruction('oxy_comment_start')">
                <xsl:choose>
                    <xsl:when test="preceding-sibling::dt[text() = 'Voltage band']">
                        <fo:block start-indent="30pt">
                            <xsl:apply-templates mode="filter_for_PIs"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="filter_for_PIs"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="preceding-sibling::dt[text() = 'Voltage band']">
                        <fo:block start-indent="30pt">
                            <xsl:apply-templates/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- the following templates in mode "IET_symbols" are for processing IET Symbols (!) -->
    <xsl:template match="*[contains(@class, ' topic/dlentry ')]" mode="IET_symbols">
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <fo:table-row>
            <xsl:apply-templates mode="IET_symbols">
                <xsl:with-param name="nested_insert_start" select="$nested_insert_start"/>
                <xsl:with-param name="nested_insert_end" select="$nested_insert_end"/>
            </xsl:apply-templates>

            <xsl:variable name="additional_symbol_element_para"
                select="count(ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/p[@outputclass='symbol_suffix'])"/>
            <!-- determine of there is a paragraph in "additional_symbol_info".                
                If not, then add a cell to make up for it
                -->
            <xsl:choose>
                <xsl:when test="$additional_symbol_element_para = 0">
                    <fo:table-cell>
                        <fo:block>&#160;</fo:block>
                    </fo:table-cell>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates
                        select="ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/p[@outputclass='symbol_suffix']"
                        mode="IET_symbols"/>
                </xsl:otherwise>
            </xsl:choose>
            <!-- determine of there is an xref or ph in "additional_symbol_info" (there should be).               
                If not, then add a cell to make up for it
                -->
            <xsl:variable name="additional_symbol_element_xref_or_ph">
                <xsl:choose>
                    <xsl:when
                        test="ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/xref">1</xsl:when>
                    <xsl:when
                        test="ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/ph">1</xsl:when>
                    <xsl:otherwise>0</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="$additional_symbol_element_xref_or_ph = 0">
                    <fo:table-cell>
                        <fo:block>&#160;</fo:block>
                    </fo:table-cell>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates
                        select="ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/xref|
                        ancestor::bodydiv[@outputclass='symbol_information']/bodydiv[@outputclass='additional_symbol_info']/ph"
                        mode="IET_symbols"/>
                </xsl:otherwise>
            </xsl:choose>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="*[contains(@class, ' topic/dt ')]" mode="IET_symbols">
        <xsl:param name="nested_insert_start"/>
        <xsl:param name="nested_insert_end"/>
        <!-- this template provides cell #1 of the row -->
        <xsl:choose>
            <xsl:when
                test="child::processing-instruction('oxy_insert_start') or
                child::processing-instruction('oxy_delete') or
                child::processing-instruction('oxy_comment_start')">
                <fo:table-cell>
                    <fo:block>
                        <xsl:apply-templates mode="filter_for_PIs"/>
                    </fo:block>
                </fo:table-cell>
            </xsl:when>
            <xsl:otherwise>
                <fo:table-cell>
                    <fo:block>
                        <xsl:if
                            test="($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
                            ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:if>
                        <xsl:apply-templates/>
                    </fo:block>
                </fo:table-cell>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/dd ')]" mode="IET_symbols">
        <xsl:param name="nested_insert_start"/>
        <xsl:param name="nested_insert_end"/>
        <!-- this template provides cells# 2 and 3 of the row. 3 is a blank spacer -->
        <!--        <fo:table-cell>
            <fo:block>
                <xsl:apply-templates/>
            </fo:block>
        </fo:table-cell>-->
        <xsl:choose>
            <xsl:when
                test="child::processing-instruction('oxy_insert_start') or
                child::processing-instruction('oxy_delete') or
                child::processing-instruction('oxy_comment_start')">
                <fo:table-cell>
                    <fo:block>
                        <xsl:apply-templates mode="filter_for_PIs"/>
                    </fo:block>
                </fo:table-cell>
            </xsl:when>
            <xsl:otherwise>
                <fo:table-cell>
                    <fo:block>
                        <xsl:if
                            test="($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
                            ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
                            <xsl:attribute name="color">green</xsl:attribute>
                            <xsl:attribute name="font-weight">bold</xsl:attribute>
                        </xsl:if>
                        <xsl:apply-templates/>
                    </fo:block>
                </fo:table-cell>
            </xsl:otherwise>
        </xsl:choose>
        <fo:table-cell>
            <fo:block>
                <fo:block>&#160;</fo:block>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template match="*" mode="IET_symbols">
        <!-- this template provides cells# 4 and 5 of the row IF there are two elements in the
            "additional_symbol_info
        If there's only one element in "additional_symbol_info" then it provides only cell# 4
        If there are no "additional_symbol_info" elements then it doesn't provide any cells
        -->
        <!-- determine if there is an odd or even number of insert start PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_start">
            <xsl:call-template name="determine_nested_insert_start"/>
        </xsl:variable>
        <!-- determine if there is an odd or even number of insert end PIs on the
            preceding-sibling axis -->
        <xsl:variable name="nested_insert_end">
            <xsl:call-template name="determine_nested_insert_end"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes">
            <xsl:call-template name="determine_comments_and_deletes"/>
        </xsl:variable>
        <xsl:variable name="comments_and_deletes_content">
            <xsl:call-template name="determine_comments_and_deletes_content">
                <xsl:with-param name="comments_and_deletes" select="$comments_and_deletes"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- if this element has any change marks that come immediately before it, process them
            here -->
        <xsl:if test="$comments_and_deletes = 'change mark for this element'">
            <xsl:apply-templates select="$comments_and_deletes_content/processing-instruction()"
                mode="apply_change_marks_to_block"/>
        </xsl:if>        
        <fo:table-cell>
            <fo:block>
                <xsl:if
                    test="($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
                    ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
                    <xsl:attribute name="color">green</xsl:attribute>
                    <xsl:attribute name="font-weight">bold</xsl:attribute>
                </xsl:if>
                <xsl:choose>
                    <xsl:when
                        test="child::processing-instruction('oxy_insert_start') or
                        child::processing-instruction('oxy_delete') or
                        child::processing-instruction('oxy_comment_start')">
                        <xsl:apply-templates  mode="filter_for_PIs"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="."/>
                    </xsl:otherwise>
                </xsl:choose>                                
            </fo:block>
        </fo:table-cell>
    </xsl:template>

</xsl:stylesheet>
