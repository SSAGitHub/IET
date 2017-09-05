<?xml version="1.0" encoding="utf-8"?>
<xsl:transform version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
	xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" extension-element-prefixes="m">

	<xsl:import href="article-fo-1-0.xsl"/>
	<xsl:include href="issue-fo-common.xsl"/>


<xsl:template match="/" priority="10">

        <fo:root font-size="8pt" line-height="9pt" font-selection-strategy="character-by-character" font-family="Times New Roman, Arial Unicode MS, STIX">

			<fo:layout-master-set>
				<xsl:call-template name="define-legal-page-master"/>
			</fo:layout-master-set>

			<xsl:apply-templates/>
		</fo:root>
	</xsl:template>


	<xsl:template name="define-legal-page-master">

		<fo:simple-page-master master-name="legal-page" page-height="297mm" page-width="210mm"
			margin-top="0.4in" margin-bottom="0.4in" margin-left="15mm" margin-right="15mm">

			<fo:region-body region-name="body" margin-top="20pt" margin-bottom="28pt"
				margin-left="0mm" margin-right="0mm" column-count="2" column-gap="8mm"/>
			<fo:region-after region-name="xsl-region-after" extent="0.3in"/>
			

		</fo:simple-page-master>
	</xsl:template>

	<xsl:template match="/article[@specific-use='legal_page']" priority="10">

		<fo:page-sequence master-reference="legal-page" force-page-count="no-force" id="legal_page">
			<fo:static-content flow-name="xsl-region-after" font-size="8pt" font-family="Arial">
				<fo:block text-align="left">
					<xsl:call-template name="issue-generate-footer-text"/>
				</fo:block>					
			</fo:static-content>
			<fo:flow flow-name="body" font-size="10pt">

				<fo:block space-before="0pt" padding-bottom="0" margin-top="-25pt"
					margin-bottom="0pt" line-stacking-strategy="font-height"
					line-height-shift-adjustment="disregard-shifts" font-size="12pt"
					font-weight="bold" span="all" font-family="Arial" id="start_legal_page">
					<xsl:value-of select="/artcile/body/sec[@specific-use = 'issue-page-title']/title"/>										
				</fo:block>

				<xsl:apply-templates select="body" mode="legal"/>
				
				<fo:block id="end_legal_page"/>
				<fo:block span="all"><fo:leader leader-length="0pt" 
					leader-pattern="space"/></fo:block>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>

	<xsl:template
		match="ref-list/p 
		| body//p[not(ancestor::boxed-text)
		and not(ancestor::speech)
		and not(ancestor::statement)
		and not(ancestor::caption)
		and not(ancestor::fn)]
		| back//p[not(ancestor::boxed-text)
		and not(ancestor::speech)
		and not(ancestor::statement)
		and not(ancestor::caption)
		and not(ancestor::fn)]" mode="legal">
		
		
		<!-- now set the paragraph. -->
		<xsl:variable name="paraIndent">
			<xsl:choose>
				<xsl:when test="count(preceding-sibling::p) > 0">
					<xsl:text>0pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>0pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="space-after">
			<xsl:choose>
				<xsl:when test="/article[@specific-use='legal_page']">
					<xsl:text>3pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>10pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<fo:block font-size="8pt" line-height="{$textleading}pt" 
			text-indent="{$paraIndent}" space-after="{$space-after}"
			font-family="Arial">

			<xsl:apply-templates/>
			
		</fo:block>
	</xsl:template>

	<xsl:template match="/article/body/sec/title | back/sec/title" mode="legal">
		<xsl:call-template name="block-title-style-legal1"/>
	</xsl:template>
	
	<xsl:template match="body/sec/sec/title | back/sec/sec/title" mode="legal">
		<xsl:call-template name="block-title-style-legal2"/>
	</xsl:template>
	
	
	<xsl:template name="block-title-style-legal1">
		
		<xsl:param name="title-string"/>
		
		<xsl:message>STYLE 1</xsl:message>
		<xsl:variable name="leading-below">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='instruct_page'">
					<xsl:text>0pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>4pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<xsl:variable name="font-size">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='legal_page'">
					<xsl:text>14pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>10pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<xsl:variable name="span">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='legal_page'">
					<xsl:text>all</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text></xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<xsl:variable name="l-below">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='legal_page'">
					<xsl:text>10pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$leading-below"/>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<xsl:variable name="padding-bottom">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='legal_page'">
					<xsl:text>10pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>0pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<fo:block start-indent="0pc" line-height="11pt" space-after="{$l-below}" padding-bottom="{$padding-bottom}"
			space-after.precedence="force" keep-with-next.within-column="always" span="{$span}" space-before="20pt">
			<fo:wrapper font-family="Arial" font-size="{$font-size}" font-weight="bold" color="black">
				<xsl:choose>
					<xsl:when test="label">
						<xsl:value-of select="label"/>
						<!-- SA00:label -->
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="preceding-sibling::label"/>
						<!-- SA00:label -->
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="not(/article/@specific-use='instruct_page') and not(/article/@specific-use='legal_page')">
					<xsl:text>&#x2003;</xsl:text>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="boolean(normalize-space($title-string))">
						<xsl:value-of select="$title-string"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates/>
					</xsl:otherwise>
				</xsl:choose>
			</fo:wrapper>
		</fo:block>
		
	</xsl:template>

	<xsl:template name="block-title-style-legal2">
		
		<xsl:param name="title-string"/>
		
		<xsl:variable name="font-style">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='legal_page'">
					<xsl:text>normal</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>italic</xsl:text>
				</xsl:otherwise>
			</xsl:choose> 
		</xsl:variable>
		
		<fo:block start-indent="0pc" line-height="{$textleading}pt"
			space-after="0" space-after.precedence="force"
			keep-with-next="always" >
			<fo:wrapper font-family="Arial" font-size="{$textsize}pt" font-style="$font-style" font-weight="bold">
				
				<xsl:value-of select="preceding-sibling::label"/>
				<!-- SA00:label -->
				<xsl:text> </xsl:text>
				<xsl:choose>
					<xsl:when test="boolean(normalize-space($title-string))">
						<xsl:value-of select="$title-string"/>
					</xsl:when>
					
					<xsl:otherwise>
						<xsl:apply-templates/>
					</xsl:otherwise>
				</xsl:choose>
				
			</fo:wrapper>
		</fo:block>
		
	</xsl:template>
	
</xsl:transform>
