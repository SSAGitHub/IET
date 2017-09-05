<?xml version="1.0" encoding="utf-8"?>
<xsl:transform version="2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
	xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" extension-element-prefixes="m">

	<xsl:include href="article-fo-1-0.xsl"/>
	<xsl:include href="issue-instruct-page-fo.xsl"/>

	<xsl:include href="issue-fo-common.xsl"/>
	

	<xsl:param name="first-numbered-page-param" select="1"/>

	<xsl:template match="/" priority="10">

        <fo:root font-size="9pt" line-height="10pt" font-selection-strategy="character-by-character" font-family="Times New Roman, Arial Unicode MS, STIX">

			<fo:layout-master-set>
				<xsl:call-template name="define-simple-page-masters"/>
				<xsl:call-template name="define-page-sequences"/>
				<xsl:call-template name="define-instruct-page-master"/>

				<fo:simple-page-master master-name="PageMaster-TOC-First" page-width="210mm"
					page-height="297mm" margin-top="0.3in" margin-bottom="0.1in"
					margin-left="0.5in" margin-right="0.5in">
					<fo:region-body  margin-top="0.8in" margin-bottom="0.4in"/>
					<!--<fo:region-body/>-->
					<fo:region-before region-name="xsl-region-before" extent="0.8in"/>
					<fo:region-after region-name="xsl-region-after" extent="0.3in"/>
				</fo:simple-page-master>

				<fo:simple-page-master master-name="PageMaster-TOC" page-width="210mm"
					page-height="297mm" margin-top="0.3in" margin-bottom="0.1in"
					margin-left="0.5in" margin-right="0.5in">
					<fo:region-body  margin-top="0.6in" margin-bottom="0.4in"/>
					<!--<fo:region-body/>-->
					<!--<fo:region-before region-name="xsl-region-before" extent="1.0in"/>-->
					<fo:region-after region-name="xsl-region-after" extent="0.3in"/>
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="TOC">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference page-position="first" 
							master-reference="PageMaster-TOC-First"/>
						<fo:conditional-page-master-reference page-position="rest" 
							master-name="PageMaster-TOC"/>
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<xsl:apply-templates/>
		</fo:root>
	</xsl:template>


	<xsl:template match="issuemap" priority="100">
		<xsl:call-template name="create-toc"/>
 <xsl:comment select="document(prelimpageref/@href)/*/node-name(.)"/>
		<xsl:apply-templates select="document(prelimpageref/@href)/*"/>

		<xsl:for-each select="articleref">
			<xsl:variable name="start-page">
				<xsl:choose>
					<xsl:when test="count(preceding-sibling::articleref) = 0">
						<xsl:value-of select="$first-numbered-page-param"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>auto</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xsl:apply-templates select="document(@href)/*">
				<xsl:with-param name="start-page-param" select="normalize-space($start-page)"/>
			</xsl:apply-templates>

		</xsl:for-each>

	</xsl:template>




	<xsl:template name="create-toc">
		<fo:page-sequence master-reference="TOC" force-page-count="no-force">
			<fo:static-content flow-name="xsl-region-before" font-size="8pt" font-family="Arial">
				<fo:table table-layout="auto" width="100%" border-collapse="separate"
					border-spacing="2pt 10pt">
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block font-size="13pt" font-family="Arial" font-weight="bold" id="start_toc">Contents</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block font-size="8pt" font-family="Arial" text-align="right">VOLUME <xsl:value-of select="$issue-volume"/> ISSUE <xsl:value-of select="$issue-number"/><xsl:text> </xsl:text><xsl:value-of select="upper-case($issue-abbreviated-cover-date)"/></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:static-content>
			<fo:static-content flow-name="xsl-region-after" font-size="8pt" font-family="Arial">
				<fo:block text-align="right">
					<xsl:call-template name="issue-generate-footer-text"/>										
				</fo:block>
			</fo:static-content>
			
			<fo:flow flow-name="xsl-region-body" font-size="8pt">

				<fo:table table-layout="auto" width="100%" height="235mm"
					space-before="25pt">
					<fo:table-body>
						<xsl:for-each select="articleref[lower-case(@specialIssue) = 'true']">

							<xsl:variable name="article-doc" select="document(@href)"/>
							<xsl:variable name="article-id">
								<xsl:value-of
									select="concat('startarticle_', translate($article-doc//article-id[@pub-id-type='doi'], './-', ''))"
								/>
							</xsl:variable>

							<xsl:if test="position()=1">
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt" font-family="Arial" font-weight="bold"
											id="special" padding-after="20pt">
											<xsl:value-of select="$issue-title"/>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:if>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left" margin-left="0em" space-before="5pt"
										font-size="8pt" font-family="Arial" font-weight="bold">
										<fo:basic-link internal-destination="{$article-id}">
											<xsl:apply-templates
												select="$article-doc/article/front/article-meta/title-group/article-title/node()"
											/>
										</fo:basic-link>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-right="5pt" padding-left="10pt">
									<fo:block text-align="right" font-size="8pt" font-family="Arial"
										font-weight="bold">
										<fo:basic-link internal-destination="{$article-id}">
											<fo:page-number-citation ref-id="{$article-id}"/>
										</fo:basic-link>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left"
										font-size="8pt" font-family="Arial" padding-bottom="10pt">
										<xsl:for-each select="$article-doc//contrib[@contrib-type='author']">
											<xsl:value-of select=".//given-names"/>
											<xsl:text> </xsl:text>
											<xsl:value-of select=".//surname"/>
												<xsl:if test="position()!=last()">
													<xsl:choose>
														<xsl:when test="position() = last()-1">
															<xsl:text> and </xsl:text>
														</xsl:when>
														<xsl:otherwise>
															<xsl:text>, </xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					<xsl:for-each select="articleref[lower-case(@specialIssue) != 'true']">

							<xsl:variable name="article-doc" select="document(@href)"/>
							<xsl:variable name="article-id">
								<xsl:value-of
									select="concat('startarticle_', translate($article-doc//article-id[@pub-id-type='doi'], './-', ''))"
								/>
							</xsl:variable>

							<xsl:if test="position()=1 and preceding::articleref[lower-case(@specialIssue) = 'true']">
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-size="10pt" font-family="Arial" font-weight="bold"
											id="special" padding-before="20pt" padding-after="20pt">Regular Papers</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:if>
							<fo:table-row keep-with-next="always">
								<fo:table-cell>
									<fo:block text-align="left" margin-left="0em" space-before="5pt"
										font-size="8pt" font-family="Arial" font-weight="bold">
										<fo:basic-link internal-destination="{$article-id}">
											<xsl:apply-templates
												select="$article-doc/article/front/article-meta/title-group/article-title/node()"
											/>
										</fo:basic-link>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell padding-right="5pt" padding-left="10pt">
									<fo:block text-align="right" font-size="8pt" font-family="Arial"
										font-weight="bold">
										<fo:basic-link internal-destination="{$article-id}">
											<fo:page-number-citation ref-id="{$article-id}"/>
										</fo:basic-link>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell>
									<fo:block text-align="left" font-size="8pt" font-family="Arial"
										padding-bottom="10pt">
										<xsl:for-each
											select="$article-doc//contrib[@contrib-type='author']">
											<xsl:value-of select=".//given-names"/>
											<xsl:text> </xsl:text>
											<xsl:value-of select=".//surname"/>
											<xsl:if test="position()!=last()">
												<xsl:choose>
												<xsl:when test="position() = last()-1">
												<xsl:text> and </xsl:text>
												</xsl:when>
												<xsl:otherwise>
												<xsl:text>, </xsl:text>
												</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:for-each>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</fo:table-body>
				</fo:table>
				<fo:block id="end_toc"/>
			</fo:flow>
		</fo:page-sequence>
	</xsl:template>

</xsl:transform>
