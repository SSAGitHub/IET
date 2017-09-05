<?xml version="1.0" encoding="utf-8"?>
<xsl:transform version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
	xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" extension-element-prefixes="m">

	<xsl:template name="define-instruct-page-master">
		<fo:simple-page-master master-name="instruct-page" page-height="297mm" page-width="210mm"
			margin-top="0.4in" margin-bottom="0.4in" margin-left="15mm" margin-right="15mm">

			<fo:region-body region-name="body" margin-top="20pt" margin-bottom="28pt"
				margin-left="0mm" margin-right="0mm" column-count="2" column-gap="8mm"/>
			<fo:region-after region-name="xsl-region-after" extent="0.3in"/>
		</fo:simple-page-master>
	</xsl:template>
												
																					   
	<xsl:template match="/article[@specific-use='instruct_page']" priority="100">
		<fo:page-sequence master-reference="instruct-page" force-page-count="no-force"
			id="instruct_page">
			<fo:static-content flow-name="xsl-region-after" font-size="8pt" font-family="Arial">				
			</fo:static-content>
			
			<fo:flow flow-name="body" font-size="8pt">

				<fo:block space-before="0pt" padding-bottom="0" margin-top="-25pt"
					margin-bottom="0pt" line-stacking-strategy="font-height"
					line-height-shift-adjustment="disregard-shifts" font-size="12pt"
					font-weight="bold" span="all" font-family="Arial" id="start_instruct_page">
					<xsl:value-of select="/artcile/body/sec[@specific-use = 'issue-page-title']/title"/>
				</fo:block>

				<xsl:apply-templates select="body" mode="instructPage"/>

				<fo:block id="end_instruct_page"/>
				<fo:block span="all">
					<fo:leader leader-length="0pt" leader-pattern="space"/>
				</fo:block>
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
		and not(ancestor::fn)]" mode="instructPage">

		<xsl:param name="space-after">
			<xsl:choose>
				<xsl:when test="/article[@specific-use='instruct_page']">
					<xsl:text>8pt</xsl:text>
				</xsl:when>

				<!-- if the next thing is another para
            and we're in a low-level wrapper -->
				<xsl:when
					test="following-sibling::*[1][self::p]
					and (parent::disp-quote | parent::list-item 
					| parent::def | parent::abstract)">
					<xsl:value-of select="'6pt'"/>
				</xsl:when>

				<!-- otherwise, use "normal" paragraph spacing: 
            either this is appropriate, OR the next element 
            encountered will override it using .precedence
            1, 2, or force. -->
				<xsl:otherwise>
					<xsl:value-of select="'0pt'"/>
				</xsl:otherwise>

			</xsl:choose>

		</xsl:param>


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

		<fo:block font-size="8pt" line-height="9pt" text-indent="0pt" space-after="10pt"
			font-family="Arial">


			<xsl:apply-templates/>

		</fo:block>
	</xsl:template>

	<xsl:template match="/article/body/sec/title" mode="instructPage">
		<xsl:call-template name="block-title-style-instruct1"/>
	</xsl:template>

	<xsl:template match="body/sec/sec/title" mode="instructPage">
		<xsl:call-template name="block-title-style-instruct2"/>
	</xsl:template>


	<xsl:template name="block-title-style-instruct1">

		<xsl:param name="title-string"/>

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
				<xsl:when test="/article/@specific-use='instruct_page'">
					<xsl:text>11pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>10pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="span">
			<xsl:choose>
				<xsl:when test="count(parent::sec/preceding-sibling::sec)=0">
					<xsl:text>all</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="l-below">
			<xsl:choose>
				<xsl:when test="count(parent::sec/preceding-sibling::sec)=0">
					<xsl:text>10pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'0pt'"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="padding-bottom">
			<xsl:choose>
				<xsl:when test="count(parent::sec/preceding-sibling::sec)=0">
					<xsl:text>10pt</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>0pt</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:block start-indent="0pc" line-height="11pt" space-after="{$l-below}"
			padding-bottom="{$padding-bottom}" space-after.precedence="force"
			keep-with-next.within-column="always" span="{$span}" space-before="20pt">
			<fo:wrapper font-family="Arial" font-size="{$font-size}" font-weight="bold"
				color="black">
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
				<xsl:if
					test="not(/article/@specific-use='instruct_page') and not(/article/@specific-use='legal_page')">
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

	<xsl:template name="block-title-style-instruct2">

		<xsl:param name="title-string"/>

		<xsl:variable name="font-style">
			<xsl:choose>
				<xsl:when test="/article/@specific-use='instruct_page'">
					<xsl:text>italic</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>italic</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:block start-indent="0pc" line-height="10pt" space-after="0"
			space-after.precedence="force" keep-with-next="always">
			<fo:wrapper font-family="Arial" font-size="9pt" font-style="italic" font-weight="bold">

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
	
	

	
	

	<xsl:template match="body" mode="instructPage">
		<xsl:apply-templates mode="instructPage"/>
	</xsl:template>

	<xsl:template match="sec" mode="instructPage">
		<xsl:apply-templates mode="instructPage"/>
	</xsl:template>

	<!-- xsl:template match="bold">
		<fo:inline font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>

	<xsl:template match="italic">
		<fo:inline font-style="italic">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template -->
	
	<xsl:template match="ext-link" priority="10">
		<fo:block font-weight="bold" font-size="7pt" color="black">
			<!--<fo:basic-link external-destination="{@xlink:href}">-->
			<xsl:apply-templates/>
			<!--</fo:basic-link>-->
		</fo:block>
	</xsl:template>
	
</xsl:transform>
