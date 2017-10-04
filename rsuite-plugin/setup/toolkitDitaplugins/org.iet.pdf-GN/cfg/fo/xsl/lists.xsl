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
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

	<xsl:template
		match="*[contains(@class, ' topic/linklist ')]/*[contains(@class, ' topic/title ')]">
		<fo:block xsl:use-attribute-sets="linklist.title">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<!--Lists-->

	<xsl:template match="*[contains(@class, ' topic/ul ')]">
		<!--<xsl:comment>custom ul</xsl:comment>-->
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/ol ')]">
		<!--<xsl:comment>custom ol</xsl:comment>-->
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template
		match="*[contains(@class, ' topic/ul ')]/*[contains(@class, ' topic/li ')][child::cite_margin]">
		<!--		<xsl:comment>custom ul/li[cite_margin]</xsl:comment>-->
		<xsl:variable name="list_level"
			select="(count(parent::ul/ancestor::ul) + count(parent::ul/ancestor::ol)) + 1"/>
		<fo:block font-size="10pt" start-indent="25pt">
			<fo:table inline-progression-dimension="100%" start-indent="-25pt" table-layout="fixed">
				<fo:table-column column-number="1" column-width="proportional-column-width(10)"/>
				<fo:table-column column-number="2" column-width="proportional-column-width(90)"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block end-indent="3pt" start-indent="-25pt" text-align="right">
								<xsl:apply-templates select="cite_margin"/>
								<!--									<fo:basic-link color="blue" external-destination="url(www.cnn.com)" font-style="italic">
										<xsl:apply-templates select="cite_margin"/>
									</fo:basic-link>-->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-left="27pt">
							<fo:list-block provisional-label-separation="4pt">
								<xsl:attribute name="provisional-distance-between-starts">
									<xsl:choose>
										<xsl:when test="parent::*[contains(@class, ' topic/ul ')][@outputclass='romanLowerCaseParen']">
											<xsl:text>18pt</xsl:text>	
										</xsl:when>
										<xsl:otherwise>						
											<xsl:text>15pt</xsl:text>	
										</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<fo:list-item>
									<fo:list-item-label end-indent="label-end()">
										<fo:block font-weight="bold" font-family="Serif">
											<xsl:call-template name="bulletStyle">
												<xsl:with-param name="style" select="parent::ul/@outputclass"/>
											</xsl:call-template>
										</fo:block>
									</fo:list-item-label>
									<fo:list-item-body start-indent="body-start()">
										<fo:block>
											<xsl:apply-templates
												select="node()[not(self::cite_margin)]"/>
										</fo:block>
									</fo:list-item-body>
								</fo:list-item>
							</fo:list-block>
							
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>


	<xsl:template
		match="*[contains(@class, ' topic/ul ')]/*[contains(@class, ' topic/li ')][not(child::cite_margin)]">
		<!--		<xsl:comment>custom ul/li[not(cite_margin)]</xsl:comment>-->
		<xsl:variable name="list_level"
			select="(count(parent::ul/ancestor::ul) + count(parent::ul/ancestor::ol)) + 1"/>
		<fo:list-block provisional-label-separation="4pt">
			<xsl:attribute name="provisional-distance-between-starts">
				<xsl:choose>
					<xsl:when test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanLowerCaseParen']">
						<xsl:text>18pt</xsl:text>	
					</xsl:when>
					<xsl:otherwise>						
						<xsl:text>15pt</xsl:text>	
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<fo:list-item>
				<fo:list-item-label end-indent="label-end()">
					<fo:block font-weight="bold" font-family="Serif">
						<xsl:call-template name="bulletStyle">
							<xsl:with-param name="style" select="parent::ul/@outputclass"/>
						</xsl:call-template>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()">
					<fo:block>
						<xsl:apply-templates
							select="node()[not(self::cite_margin)]"/>
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
		</fo:list-block>
	</xsl:template>

	<xsl:template name="bulletStyle">
		<xsl:param name="style"></xsl:param>
		<xsl:choose>
			<xsl:when test="$style='no_label'">
				<xsl:text>&#160;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='square'">
				<xsl:text>&#x25A0;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='triangle'">
				<xsl:text>&#x25BA;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='hyphen-long'">
				<xsl:text>&#x2014;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='hyphen-short'">
				<xsl:text>&#x2013;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='asterix'">
				<xsl:text>*</xsl:text>
			</xsl:when>
			<xsl:when test="$style='double-asterix'">
				<xsl:text>**</xsl:text>
			</xsl:when>
			<xsl:when test="$style='dagger'">
				<xsl:text>&#x2020;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='double-dagger'">
				<xsl:text>&#x2021;</xsl:text>
			</xsl:when>
			<xsl:when test="$style='plus'">
				<xsl:text>+</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>&#x2022;</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	

	<xsl:template
		match="*[contains(@class, ' topic/ol ')]/*[contains(@class, ' topic/li ')][child::cite_margin]">
		<xsl:comment>custom ol/li[cite_margin] 2</xsl:comment>
		<xsl:variable name="list_level"
			select="(count(parent::ol/ancestor::ol) + count(parent::ol/ancestor::ul)) + 1"/>
		<xsl:comment>list level: <xsl:value-of select="$list_level"/></xsl:comment>
		<fo:block font-size="10pt" start-indent="25pt">
			<fo:table inline-progression-dimension="100%" start-indent="-25pt" table-layout="fixed">
				<fo:table-column column-number="1" column-width="proportional-column-width(10)"/>
				<fo:table-column column-number="2" column-width="proportional-column-width(90)"/>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block end-indent="3pt" start-indent="-25pt" text-align="right">
								<xsl:apply-templates select="cite_margin"/>
								<!--									<fo:basic-link color="blue" external-destination="url(www.cnn.com)" font-style="italic">
										<xsl:apply-templates select="cite_margin"/>
									</fo:basic-link>-->
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding-left="27pt">
							<fo:list-block provisional-label-separation="4pt">
								<xsl:attribute name="provisional-distance-between-starts">
									<xsl:choose>
										<xsl:when test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanLowerCaseParen']">
											<xsl:text>18pt</xsl:text>	
										</xsl:when>
										<xsl:otherwise>						
											<xsl:text>15pt</xsl:text>	
										</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<fo:list-item>
									<fo:list-item-label end-indent="label-end()">
										<fo:block font-weight="bold">
											<xsl:call-template name="listStyle">
												<xsl:with-param name="style" select="parent::ol/@outputclass"/>
											</xsl:call-template>
										</fo:block>
									</fo:list-item-label>
									<fo:list-item-body start-indent="body-start()">
										<fo:block>
											<xsl:apply-templates
												select="node()[not(self::cite_margin)]"/>
										</fo:block>
									</fo:list-item-body>
								</fo:list-item>
							</fo:list-block>

						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	
	<xsl:template
		match="*[contains(@class, ' topic/ol ')]/*[contains(@class, ' topic/li ')][not(cite_margin)]">
		<xsl:comment>custom ol/li[cite_margin] 1</xsl:comment>
		<xsl:variable name="list_level"
			select="(count(parent::ol/ancestor::ol) + count(parent::ol/ancestor::ul)) + 1"/>
		<xsl:comment>list level: <xsl:value-of select="$list_level"/></xsl:comment>
		<fo:list-block provisional-label-separation="4pt">
			<xsl:attribute name="provisional-distance-between-starts">
				<xsl:choose>
					<xsl:when test="parent::*[contains(@class, ' topic/ol ')][@outputclass='romanLowerCaseParen']">
						<xsl:text>18pt</xsl:text>	
					</xsl:when>
					<xsl:otherwise>						
						<xsl:text>15pt</xsl:text>	
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<fo:list-item>
				<fo:list-item-label end-indent="label-end()">
					<fo:block font-weight="bold">
						<xsl:call-template name="listStyle">
							<xsl:with-param name="style" select="parent::ol/@outputclass"/>
						</xsl:call-template>
					</fo:block>
				</fo:list-item-label>
				<fo:list-item-body start-indent="body-start()">
					<fo:block>
						<xsl:apply-templates
							select="node()[not(self::cite_margin)]"/>
					</fo:block>
				</fo:list-item-body>
			</fo:list-item>
		</fo:list-block>
	</xsl:template>

	<xsl:template name="listStyle">
		<xsl:param name="style"></xsl:param>
		<xsl:choose>
			<xsl:when test="$style='arabicParen'">
				<xsl:text>(</xsl:text>
				<xsl:number format="1"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="$style='letterUpperCase'">
				<xsl:number format="A"/>
			</xsl:when>
			<xsl:when test="$style='letterLowerCase'">
				<xsl:number format="a"/>
			</xsl:when>
			<xsl:when test="$style='letterLowerCaseParen'">
				<xsl:text>(</xsl:text>
				<xsl:number format="a"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="$style='romanUpperCase'">
				<xsl:number format="I"/>
			</xsl:when>
			<xsl:when test="$style='romanLowerCase'">
				<xsl:number format="i"/>
			</xsl:when>
			<xsl:when test="$style='romanLowerCaseParen'">
				<xsl:text>(</xsl:text>
				<xsl:number format="i"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="$style='letterLowerCaseRightParen'">
				<xsl:number format="a"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="$style='arabicRightParen'">
				<xsl:number format="1"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="$style='romanLowerCaseRightParen'">
				<xsl:number format="i"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:number format="1"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/li ')]/*[contains(@class, ' topic/itemgroup ')]">
		<fo:block xsl:use-attribute-sets="li.itemgroup">
			<!--	<xsl:call-template name="commonattributes"/>-->
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/sl ')]">
		<fo:list-block xsl:use-attribute-sets="sl" d="c">
			<!--<xsl:call-template name="commonattributes"/>-->
			<xsl:apply-templates/>
		</fo:list-block>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/sl ')]/*[contains(@class, ' topic/sli ')]">
		<fo:list-item xsl:use-attribute-sets="sl.sli">
			<fo:list-item-label xsl:use-attribute-sets="sl.sli__label">
				<fo:block xsl:use-attribute-sets="sl.sli__label__content">
					<fo:inline>
						<!--					<xsl:call-template name="commonattributes"/>-->
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

</xsl:stylesheet>
