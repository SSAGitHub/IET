<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:iet="http://www.iet.org/journals/xslt-functions"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs iet" version="2.0">
    <xsl:output doctype-public="-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN"
        doctype-system="JATS-journalpublishing1.dtd" indent="yes"/>

    <xsl:param name="page-count" select="0"></xsl:param>
    <xsl:param name="iet-prefix" select="''"/>
    <xsl:param name="should-add-iet-prefix" select="false()"/>
    <xsl:param name="journal-abbrv-title" select="''"/>
    
    <xsl:variable name="journalAbbreviation" select="normalize-space(/article_set/article/journal/abbrev-journal-title)"/>
    <xsl:variable name="journalCode" select="replace($journalAbbreviation, 'IET([ -]+)', '')"/>
    <xsl:variable name="orignalArticleId" select="/article_set/article/@ms_no"/>
    <xsl:variable name="articleId" select="replace($orignalArticleId, 'ELL-', 'EL-')"/>
    <xsl:variable name="shortArticleId" select="replace($articleId, '\.[A-Z0-9]+$', '')"/>
    <xsl:variable name="currentDate" as="xs:date" select="current-date()"/>
    
    <xsl:variable name="openAccess" select="lower-case(/*/article/configurable_data_fields/custom_fields[@cd_code ='Open Access Checklist']/@cd_value)"/>
    <xsl:variable name="licenseType" select="upper-case(/*/article/configurable_data_fields/custom_fields[@cd_code ='Licence Type']/@cd_value)"/>
    
    
    <xsl:variable name="article-type-mapping">
        <entry key="Editorial">editorial</entry>
        <entry key="Invited Review">review-article</entry>
        <entry key="Research Paper">research-article</entry>
        <entry key="Regular Paper">research-article</entry>
        <entry key="Review">review-article</entry>
        <entry key="Case Study">case-report</entry>
        <entry key="Brief Paper">research-article</entry>
        <entry key="Book Review">book-review</entry>
        <entry key="Comment">letter</entry>
        <entry key="Reply">reply</entry>
        <entry key="Addendum">correction</entry>
        <entry key="Erratum">correction</entry>
        <entry key="Additional Item">correction</entry>
        <entry key="Retraction">retraction</entry>      
        <entry key="Letter">rapid-communication</entry>
        <entry key="News Items">in-brief</entry>
        <entry key="Reference Article">review-article</entry>
        <entry key="Rapid Communication">rapid-communication</entry>
    </xsl:variable>
    
    <xsl:variable name="history-date-mapping">
        <entry key="received_date">received</entry>
        <entry key="revised_date">rev-recd</entry>
        <entry key="decision_date">accepted</entry>
        <entry key="received_date_resub">received</entry>
        <entry key="submitted_date">received</entry>                
    </xsl:variable>
    
    <xsl:variable name="journal-mapping">
        <entry key="EL" prefix="no">Electron. Lett.</entry>
        <entry key="MNL" prefix="no">Micro Nano Lett.</entry>
        <entry key="HTL" prefix="no">Healthcare Technology Letters</entry>
        <entry key="ETR" prefix="no">Eng. Technol. Ref.</entry>
        <entry key="JOE" prefix="no">J Eng</entry>
        <entry key="BMT" prefix="yes">IET Biom.</entry>
        <entry key="CDS" prefix="yes">IET Circuits Devices Syst.</entry>
        <entry key="CDT" prefix="yes">IET Comput. Digit. Tech.</entry>
        <entry key="COM" prefix="yes">IET Commun.</entry>
        <entry key="CTA" prefix="yes">IET Control Theory Appl.</entry>
        <entry key="CVI" prefix="yes">IET Comput. Vis.</entry>
        <entry key="EPA" prefix="yes">IET Electr. Power Appl.</entry>
        <entry key="EST" prefix="yes">IET Electr. Syst. Transp.</entry>
        <entry key="GTD" prefix="yes">IET Gener. Transm. Distrib.</entry>
        <entry key="IFS" prefix="yes">IET Inf. Secur.</entry>
        <entry key="IPR" prefix="yes">IET Image Process.</entry>
        <entry key="ITS" prefix="yes">IET Intell. Transp. Syst.</entry>
        <entry key="MAP" prefix="yes">IET Microw. Antennas Propag.</entry>
        <entry key="NBT" prefix="yes">IET Nanobiotechnol.</entry>
        <entry key="NET" prefix="yes">IET Netw.</entry>
        <entry key="OPT" prefix="yes">IET Optoelectron.</entry>
        <entry key="PEL" prefix="yes">IET Power Electron.</entry>
        <entry key="RPG" prefix="yes">IET Renew. Power Gener.</entry>
        <entry key="RSN" prefix="yes">IET Radar Sonar Navig.</entry>
        <entry key="SEN" prefix="yes">IET Soft.</entry>
        <entry key="SMT" prefix="yes">IET Sci. Meas. Technol.</entry>
        <entry key="SPR" prefix="yes">IET Signal Process.</entry>
        <entry key="SYB" prefix="yes">IET Syst. Biol. </entry>
        <entry key="WSS" prefix="yes">IET Wirel. Sens. Syst.</entry>
        <entry key="HVE" prefix="no">High Volt.</entry>
    </xsl:variable>

	<xsl:variable name="prefix">
		<xsl:if test="$should-add-iet-prefix = 'true'">
			<xsl:choose>
				<xsl:when test="$iet-prefix = ''">
					<xsl:text>IET-</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$iet-prefix" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:variable>
    
    
    <xsl:function name="iet:hasNonEmptyResubmittedDate" as="xs:boolean">
        <xsl:param name="historyElement"/>
        
        <xsl:variable name="resubmittedElement" select="$historyElement/received_date_resub"/>
        
        <xsl:choose>
            <xsl:when test="$resubmittedElement and iet:isNotEmptyDateElement($resubmittedElement)">
                <xsl:sequence select="true()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:sequence select="false()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:function name="iet:shouldProcessDateElement" as="xs:boolean">
        <xsl:param name="dateElement"/>
        
        <xsl:variable name="elementName" select="$dateElement/local-name()"/>
        
        <xsl:choose>
            <xsl:when test="$journalCode = 'EL'">
                <xsl:choose>
                    <xsl:when test="$elementName = 'revised_date' or $elementName = 'received_date'">
                        <xsl:sequence select="false()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:sequence select="true()"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$elementName = 'submitted_date'">
                <xsl:sequence select="false()"/>
            </xsl:when>
            <xsl:when test="$elementName = 'received_date' and iet:hasNonEmptyResubmittedDate($dateElement/parent::*)">
                <xsl:sequence select="false()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:sequence select="true()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:function name="iet:isNotEmptyDateElement" as="xs:boolean">
        <xsl:param name="dateElement"/>
        <xsl:choose>
            <xsl:when test="$dateElement/month != '' and $dateElement/year != ''">
                <xsl:value-of select="true()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="false()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:template match="/ | article_set" priority="2">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="article">
        <xsl:variable name="publiction_type" select="publication_type/text()"/>
        <xsl:variable name="article_type" select="$article-type-mapping/entry[@key=$publiction_type]"/>
        
        <article dtd-version="1.0" xml:lang="en" article-type="{$article_type}">
            <front>
                <journal-meta>
                    <xsl:apply-templates select="journal"/>
                </journal-meta>
                <article-meta>
                    <xsl:call-template name="create-artilce-ids"/>
                    <xsl:call-template name="create-title-group"/>
                    <xsl:call-template name="create-contrib-group"/>
                    <xsl:call-template name="create-pub-date-element"/>
                    <xsl:call-template name="create-history-element"/>
                    <xsl:call-template name="create-permissions-element"/>
                    <xsl:call-template name="create-counts"/>
                </article-meta>
            </front>
        </article>
    </xsl:template>

	<xsl:variable name="journalC">
		<xsl:choose>
			<xsl:when test="$should-add-iet-prefix = 'true'">
				<xsl:choose>
					<xsl:when test="$iet-prefix = ''">
						<xsl:value-of select="$journalCode" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text></xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$journalCode" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:template match="journal">
		<journal-id journal-id-type="publisher-id">
			<xsl:value-of select="concat($prefix, $journalC)" />
		</journal-id>
		<journal-title-group>
			<journal-title>
				<xsl:value-of select="full_journal_title" />
			</journal-title>
		
		<abbrev-journal-title>
			<xsl:value-of select="$journal-abbrv-title" />
		</abbrev-journal-title>
	    
        </journal-title-group>

        <xsl:call-template name="create-issn-element">
            <xsl:with-param name="type" select="'print'" />
            <xsl:with-param name="pub-type" select="'ppub'" />
        </xsl:call-template>
        
        <xsl:call-template name="create-issn-element">
            <xsl:with-param name="type" select="'digital'" />
            <xsl:with-param name="pub-type" select="'epub'" />
        </xsl:call-template>
        <publisher>
            <publisher-name>
                <xsl:value-of select="publisher_name"/>
            </publisher-name>
        </publisher>

    </xsl:template>
    
    <xsl:template name="create-issn-element">
        <xsl:param name="type"/>
        <xsl:param name="pub-type"/>
        
        <xsl:variable name="issn" select="issn[@issn_type = $type]"/>
        
        <xsl:if test="matches($issn, '[0-9]+.*')">
            <issn pub-type="{$pub-type}">
                <xsl:value-of select="$issn"/>
            </issn>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="create-artilce-ids">
        <xsl:variable name="normalizedArticleId" select="replace($articleId, '\-', '.')"/>
        <xsl:variable name="publisherIdInitial" select="concat($prefix, replace($shortArticleId, '\-', '.'))"/>
        <xsl:variable name="publisherId" select="replace($publisherIdInitial,'\.SI', '' )"/>

	<xsl:variable name="publisher">
		<xsl:choose>
			<xsl:when test="$should-add-iet-prefix = 'true'">
				<xsl:choose>
					<xsl:when test="$iet-prefix = ''">
						<xsl:value-of select="$publisherId" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="replace($publisherId, $journalCode, '')" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$publisherId" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
        
        <article-id pub-id-type="doi">10.1049/<xsl:value-of select="lower-case($publisher)"/></article-id>
        <article-id pub-id-type="publisher-id"><xsl:value-of select="$publisher"/></article-id>
        <article-id pub-id-type="manuscript"><xsl:value-of select="$normalizedArticleId"/></article-id>
    </xsl:template>
    
    <xsl:template name="create-title-group">
        <title-group>
            <article-title><xsl:value-of select="article_title" disable-output-escaping="yes"/></article-title>
        </title-group>
    </xsl:template>

    <xsl:template name="create-contrib-group">
        <contrib-group>
            <xsl:apply-templates select="author_list/author"/>
        </contrib-group>
    </xsl:template>

    <xsl:template match="author">
        <xsl:element name="contrib">
            <xsl:attribute name="contrib-type" select="'author'"/>
            <xsl:if test="@corr = 'true'">
                <xsl:attribute name="corresp" select="'yes'"/>
            </xsl:if>
            <name name-style="western">
                <surname>
                    <xsl:value-of select="last_name"/>
                </surname>
                <given-names>
                    <xsl:value-of select="first_name"/>
                </given-names>
            </name>
            <email>
                <xsl:value-of select="email[@addr_type = 'primary']"/>
            </email>
        </xsl:element>
    </xsl:template>

    <xsl:template match="middle_name[text() != '']">
        <xsl:text> </xsl:text>
        <xsl:value-of select="."/>
    </xsl:template>

    <xsl:template name="create-pub-date-element">
        <pub-date pub-type="epreprint">
            <day><xsl:value-of select="day-from-date($currentDate)"/></day>
            <month><xsl:value-of select="month-from-date($currentDate)"/></month>
            <year><xsl:value-of select="year-from-date($currentDate)"/></year>
        </pub-date>
    </xsl:template>
    
    <xsl:template name="create-history-element">
        
        <history>
            <xsl:apply-templates select="history/ms_id[@ms_no = $orignalArticleId]" />
        </history>
    </xsl:template>
    
    <xsl:template match="ms_id">
        <xsl:apply-templates select="submitted_date | received_date_resub | revised_date | received_date | decision_date"/>
    </xsl:template>
    
    <xsl:template match="submitted_date | received_date_resub | revised_date | received_date | decision_date">
        <xsl:variable name="element_name" select="name()"/>
        
        <xsl:if test="iet:isNotEmptyDateElement(.) and iet:shouldProcessDateElement(.)">
         
        <xsl:variable name="date_type" select="$history-date-mapping/entry[@key=$element_name]"/>
        <date date-type="{$date_type}">
            <xsl:apply-templates select="day" />
            <xsl:apply-templates select="month" />
            <xsl:apply-templates select="year" />
        </date>
        </xsl:if>
    </xsl:template>

    <xsl:template name="create-permissions-element">
        <permissions>
        <xsl:choose>
            <xsl:when test="$openAccess = 'no'">
                <xsl:call-template name="create-permissions-element-for-not-open-access"/>
            </xsl:when>
            <xsl:when test="$openAccess = 'yes'">
                <xsl:call-template name="create-permissions-element-for-open-access"/>
            </xsl:when>
        </xsl:choose>
        </permissions>
    </xsl:template>
    
    <xsl:template name="create-permissions-element-for-open-access">
        <xsl:variable name="type" select="lower-case(replace($licenseType, 'CC-', ''))" />

        <xsl:variable name="licenseTypeName">
            <xsl:choose>
                <xsl:when test="$licenseType ='CC-BY'"></xsl:when>
                <xsl:when test="$licenseType ='CC-BY-NC'">-NonCommercial</xsl:when>
                <xsl:when test="$licenseType ='CC-BY-ND'">-NoDerivs</xsl:when>
                <xsl:when test="$licenseType ='CC-BY-NC-ND'">-NonCommercial-NoDerivs</xsl:when>
            </xsl:choose>
        </xsl:variable>
        
        <license license-type="open-access" xlink:href="http://creativecommons.org/licenses/{$type}/3.0/">
            <license-p>This is an open access article published by the IET under the Creative Commons Attribution<xsl:value-of select="$licenseTypeName"/> License ( <ext-link xlink:href="http://creativecommons.org/licenses/{$type}/3.0/">http://creativecommons.org/licenses/<xsl:value-of select="$type"/>/3.0/</ext-link>)</license-p></license>
    </xsl:template>
    
    <xsl:template name="create-permissions-element-for-not-open-access">
        <xsl:choose>
            <xsl:when test="$licenseType = 'IET'">
                <copyright-statement>© Institution of Engineering and Technology</copyright-statement>                
            </xsl:when>
            <xsl:when test="$licenseType = 'OTHER'">
                <copyright-statement>© </copyright-statement>
            </xsl:when>
        </xsl:choose>
        <copyright-year><xsl:value-of select="year-from-date($currentDate)"/></copyright-year>
    </xsl:template>
    
    <xsl:template name="create-counts">
        <counts>
            <page-count count="{$page-count}"/>
        </counts>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
