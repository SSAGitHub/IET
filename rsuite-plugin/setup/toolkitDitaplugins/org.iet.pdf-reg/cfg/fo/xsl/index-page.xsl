<?xml version='1.0'?>

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:exsl="http://exslt.org/common" xmlns:exslf="http://exslt.org/functions"
  xmlns:opentopic-func="http://www.idiominc.com/opentopic/exsl/function"
  xmlns:comparer="com.idiominc.ws.opentopic.xsl.extension.CompareStrings"
  extension-element-prefixes="exsl" xmlns:opentopic-index="http://www.idiominc.com/opentopic/index"
  xmlns:ot-placeholder="http://suite-sol.com/namespaces/ot-placeholder"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  exclude-result-prefixes="xs opentopic-index exsl comparer opentopic-func exslf ot-placeholder mml">

  <xsl:param name="debug">false</xsl:param>
  <xsl:variable name="space_value">-2pt</xsl:variable>  

  <xsl:template match="body[parent::index] | section[parent::body[parent::index]]">
    <xsl:if test="$debug = 'true'">
      <xsl:message>PASS THROUGH: <xsl:value-of select="name()"/></xsl:message>
    </xsl:if>
    <xsl:apply-templates mode="IET-index-page"/>
  </xsl:template>

  <xsl:template match="*[contains(@class, ' topic/section ')]/*[contains(@class, ' topic/title ')]"
    mode="IET-index-page">
    <xsl:if test="$debug = 'true'">
      <xsl:message>Section Title: <xsl:value-of select="."/></xsl:message>
    </xsl:if>
    <fo:block font-weight="bold" font-size="14pt">
      <xsl:apply-templates mode="IET-index-page"/>
    </fo:block>
  </xsl:template>

  <xsl:template match="indexEntry[parent::section]" mode="IET-index-page">
    <xsl:if test="$debug = 'true'">
      <xsl:message>INDEX-ENTRY (top-level)</xsl:message>
    </xsl:if>
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
    <fo:block>
      <xsl:if
        test="($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
        ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
        <xsl:attribute name="color">green</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
      </xsl:if>
      <fo:table table-layout="fixed" border-collapse="separate" border-separation="12pt" width="90%"
        space-before="0pt" space-after="0pt">
        <fo:table-column column-number="1" column-width="proportional-column-width(80)"/>
        <fo:table-column column-number="2" column-width="proportional-column-width(20)"/>
        <fo:table-body>
          <fo:table-row border-spacing="0pt">
            <xsl:apply-templates select="indexTerm | ul" mode="IET-index-page"/>
          </fo:table-row>
          <xsl:apply-templates select="indexEntry" mode="IET-index-page"/>
        </fo:table-body>
      </fo:table>
    </fo:block>
  </xsl:template>

  <xsl:template match="indexEntry[parent::indexEntry]" mode="IET-index-page">    
    <xsl:if test="$debug = 'true'">
      <xsl:message>INDEX-ENTRY (nested)</xsl:message>
    </xsl:if>
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
    <fo:table-row border-spacing="0pt">
      <xsl:apply-templates select="indexTerm | ul" mode="IET-index-page">
        <xsl:with-param name="nestedIndexEntry_nested_insert_start" select="$nested_insert_start"/>
        <xsl:with-param name="nestedIndexEntry_nested_insert_end" select="$nested_insert_end"/>
      </xsl:apply-templates>
    </fo:table-row>
    <xsl:apply-templates select="indexEntry" mode="IET-index-page">
      <xsl:with-param name="nestedIndexEntry_indexEntry_nested_insert_start"
        select="$nested_insert_start"/>
      <xsl:with-param name="nestedIndexEntry_indexEntry_nested_insert_end"
        select="$nested_insert_end"/>
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="indexTerm" mode="IET-index-page">
    <xsl:param name="indexEntry_nested_insert_start"/>
    <xsl:param name="indexEntry_nested_insert_end"/>
    <xsl:param name="nestedIndexEntry_nested_insert_start"/>
    <xsl:param name="nestedIndexEntry_nested_insert_end"/>

    <xsl:if test="$debug = 'true'">
      <xsl:message>#### INDEXTERM</xsl:message>
    </xsl:if>
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
    <xsl:variable name="nesting_level" select="count(ancestor::indexEntry)"/>
    <xsl:variable name="reference_count">
      <xsl:choose>
        <xsl:when test="following-sibling::*[position()=1][name()='ul']">
          <xsl:value-of select="count(following-sibling::*[position()=1][name()='ul']/li) - 1"/>
        </xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
      <xsl:when
        test="child::processing-instruction('oxy_insert_start') or
        child::processing-instruction('oxy_delete') or
        child::processing-instruction('oxy_comment_start')">
        <fo:table-cell display-align="before">
          <fo:block text-align="start" space-before="{$space_value}" space-after="{$space_value}">
            <xsl:attribute name="start-indent">
              <xsl:choose>
                <xsl:when test="$nesting_level=1">12pt</xsl:when>
                <xsl:when test="$nesting_level=2">12pt</xsl:when>
                <xsl:when test="$nesting_level=3">18pt</xsl:when>
                <xsl:otherwise>6pt</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="text-indent">
              <xsl:choose>
                <xsl:when test="$nesting_level=1">-12pt</xsl:when>
                <xsl:when test="$nesting_level=2">-12pt</xsl:when>
                <xsl:when test="$nesting_level=3">-12pt</xsl:when>
              </xsl:choose>
            </xsl:attribute>
            <xsl:if test="$nesting_level = 2 or $nesting_level = 3"><xsl:text>- </xsl:text></xsl:if>
            <xsl:apply-templates mode="filter_for_PIs"/>
          </fo:block>
          <xsl:if test="not($reference_count = 0)">
            <xsl:call-template name="add_breaks">
              <xsl:with-param name="reference_count" select="$reference_count"/>
            </xsl:call-template>
          </xsl:if>
        </fo:table-cell>
      </xsl:when>
      <xsl:otherwise>
        <fo:table-cell display-align="before">
          <fo:block text-align="start" space-before="{$space_value}" space-after="{$space_value}">
            <xsl:if
              test="($indexEntry_nested_insert_start = 'odd' and $indexEntry_nested_insert_end = 'even') or
          ($indexEntry_nested_insert_start = 'even' and $indexEntry_nested_insert_end = 'odd') or
          ($nestedIndexEntry_nested_insert_start = 'odd' and $nestedIndexEntry_nested_insert_end = 'even') or
          ($nestedIndexEntry_nested_insert_start = 'even' and $nestedIndexEntry_nested_insert_end = 'odd') or          
          ($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
          ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
              <xsl:attribute name="color">green</xsl:attribute>
              <xsl:attribute name="font-weight">bold</xsl:attribute>
            </xsl:if>
            <xsl:attribute name="start-indent">
              <xsl:choose>
                <xsl:when test="$nesting_level=1">12pt</xsl:when>
                <xsl:when test="$nesting_level=2">12pt</xsl:when>
                <xsl:when test="$nesting_level=3">18pt</xsl:when>
                <xsl:otherwise>6pt</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="text-indent">
              <xsl:choose>
                <xsl:when test="$nesting_level=1">-12pt</xsl:when>
                <xsl:when test="$nesting_level=2">-12pt</xsl:when>
                <xsl:when test="$nesting_level=3">-12pt</xsl:when>
              </xsl:choose>
            </xsl:attribute>
            <xsl:if test="$nesting_level = 2 or $nesting_level = 3"><xsl:text>- </xsl:text></xsl:if>
            <xsl:apply-templates mode="IET-index-page"/>
          </fo:block>
          <xsl:if test="not($reference_count = 0)">
            <xsl:call-template name="add_breaks">
              <xsl:with-param name="reference_count" select="$reference_count"/>
            </xsl:call-template>
          </xsl:if>
        </fo:table-cell>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="add_breaks">
    <xsl:param name="reference_count"/>
    <xsl:choose>
      <xsl:when test="$reference_count = 1">
        <fo:block space-before="{$space_value}" space-after="{$space_value}">&#x2003;</fo:block>
      </xsl:when>
      <xsl:otherwise>
        <fo:block space-before="0pt" space-after="0pt">&#x2003;</fo:block>
        <xsl:call-template name="add_breaks">
          <xsl:with-param name="reference_count" select="$reference_count - 1"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="*[contains(@class, ' topic/ul ')]" mode="IET-index-page">
    <xsl:param name="indexEntry_nested_insert_start"/>
    <xsl:param name="indexEntry_nested_insert_end"/>
    <xsl:param name="nestedIndexEntry_nested_insert_start"/>
    <xsl:param name="nestedIndexEntry_nested_insert_end"/>
    <fo:table-cell display-align="after">
      <fo:block space-before="{$space_value}" space-after="{$space_value}">
        <xsl:if
          test="($indexEntry_nested_insert_start = 'odd' and $indexEntry_nested_insert_end = 'even') or
          ($indexEntry_nested_insert_start = 'even' and $indexEntry_nested_insert_end = 'odd') or
          ($nestedIndexEntry_nested_insert_start = 'odd' and $nestedIndexEntry_nested_insert_end = 'even') or
          ($nestedIndexEntry_nested_insert_start = 'even' and $nestedIndexEntry_nested_insert_end = 'odd')">
          <xsl:attribute name="color">green</xsl:attribute>
          <xsl:attribute name="font-weight">bold</xsl:attribute>
        </xsl:if>
        <xsl:apply-templates mode="IET-index-page">
          <xsl:with-param name="indexEntry_nested_insert_start"
            select="$indexEntry_nested_insert_start"/>
          <xsl:with-param name="indexEntry_nested_insert_end" select="$indexEntry_nested_insert_end"/>
          <xsl:with-param name="nestedIndexEntry_nested_insert_start"
            select="$nestedIndexEntry_nested_insert_start"/>
          <xsl:with-param name="nestedIndexEntry_nested_insert_end"
            select="$nestedIndexEntry_nested_insert_end"/>
        </xsl:apply-templates>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="*[contains(@class, ' topic/ul ')]/*[contains(@class, ' topic/li ')]"
    mode="IET-index-page">
    <xsl:param name="nested_insert_start"/>
    <xsl:param name="nested_insert_end"/>
    <xsl:param name="indexEntry_nested_insert_start"/>
    <xsl:param name="indexEntry_nested_insert_end"/>
    <xsl:param name="nestedIndexEntry_nested_insert_start"/>
    <xsl:param name="nestedIndexEntry_nested_insert_end"/>
    <xsl:if test="$debug = 'true'">
      <xsl:message>LI</xsl:message>
    </xsl:if>
    <xsl:choose>
      <xsl:when
        test="child::processing-instruction('oxy_insert_start') or
        child::processing-instruction('oxy_delete') or
        child::processing-instruction('oxy_comment_start')">
        <fo:block keep-with-previous="always" space-before="{$space_value}"
          space-after="{$space_value}">
          <xsl:apply-templates mode="filter_for_PIs"/>
        </fo:block>
      </xsl:when>
      <xsl:otherwise>
        <fo:block keep-with-previous="always" space-before="{$space_value}"
          space-after="{$space_value}">
          <xsl:if
            test="($indexEntry_nested_insert_start = 'odd' and $indexEntry_nested_insert_end = 'even') or
            ($indexEntry_nested_insert_start = 'even' and $indexEntry_nested_insert_end = 'odd') or
            ($nestedIndexEntry_nested_insert_start = 'odd' and $nestedIndexEntry_nested_insert_end = 'even') or
            ($nestedIndexEntry_nested_insert_start = 'even' and $nestedIndexEntry_nested_insert_end = 'odd') or
            ($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
            ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
            <xsl:attribute name="color">green</xsl:attribute>
            <xsl:attribute name="font-weight">bold</xsl:attribute>
          </xsl:if>                 
          <xsl:apply-templates mode="IET-index-page"/>
        </fo:block>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="indexSee | indexSeeAlso" mode="IET-index-page">
    <xsl:param name="nested_insert_start"/>
    <xsl:param name="nested_insert_end"/>
    <xsl:param name="indexEntry_nested_insert_start"/>
    <xsl:param name="indexEntry_nested_insert_end"/>
    <xsl:param name="nestedIndexEntry_nested_insert_start"/>
    <xsl:param name="nestedIndexEntry_nested_insert_end"/>
    <fo:inline font-style="italic">
      <xsl:choose>
        <xsl:when test="self::indexSee"><xsl:text>see </xsl:text></xsl:when>
        <xsl:when test="self::indexSeeAlso"><xsl:text>see also </xsl:text></xsl:when>
      </xsl:choose>
      <xsl:if
        test="($indexEntry_nested_insert_start = 'odd' and $indexEntry_nested_insert_end = 'even') or
        ($indexEntry_nested_insert_start = 'even' and $indexEntry_nested_insert_end = 'odd') or
        ($nestedIndexEntry_nested_insert_start = 'odd' and $nestedIndexEntry_nested_insert_end = 'even') or
        ($nestedIndexEntry_nested_insert_start = 'even' and $nestedIndexEntry_nested_insert_end = 'odd') or
        ($nested_insert_start = 'odd' and $nested_insert_end = 'even') or
        ($nested_insert_start = 'even' and $nested_insert_end = 'odd')">
        <xsl:attribute name="color">green</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when
          test="child::processing-instruction('oxy_insert_start') or
    child::processing-instruction('oxy_delete') or
    child::processing-instruction('oxy_comment_start')">
          <xsl:apply-templates mode="filter_for_PIs"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates mode="IET-index-page"/>
        </xsl:otherwise>
      </xsl:choose>
    </fo:inline>
  </xsl:template>

  <xsl:template match="text()" mode="IET-index-page">
    <xsl:if test="$debug = 'true'">
      <xsl:message>TEXT NODE</xsl:message>
    </xsl:if>
    <xsl:value-of select="."/>
  </xsl:template>

  <!-- copied this template from links.xsl because need to stop <desc> content in figures and tables
    from being processed in the Index. Don't want to alter the template in links.xsl because I'm afraid it
    will mess up links outside the index -->
  <xsl:template match="*[contains(@class,' topic/xref ')]"  mode="IET-index-page">
    <fo:inline>
      <xsl:call-template name="commonattributes"/>
    </fo:inline>
    <xsl:variable name="destination" select="opentopic-func:getDestinationId(@href)"/>
    <xsl:variable name="element" select="key('key_anchor',$destination)[1]"/>
    <xsl:variable name="referenceTitle">
      <xsl:apply-templates select="." mode="insertReferenceTitle">
        <xsl:with-param name="href" select="@href"/>
        <xsl:with-param name="titlePrefix" select="''"/>
        <xsl:with-param name="destination" select="$destination"/>
        <xsl:with-param name="element" select="$element"/>
      </xsl:apply-templates>
    </xsl:variable>
    
    <xsl:variable name="previous_node" >
      <xsl:call-template name="get_previous_node"/>
    </xsl:variable>    
    <xsl:variable name="pn">
      <xsl:for-each select="$previous_node">
        <xsl:call-template name="test_previous_node_type"/>
      </xsl:for-each>
    </xsl:variable> 
    
    <xsl:choose>
      <xsl:when test="$pn = 'insert_start'">
        <fo:inline color="green" font-weight="bold">
          <xsl:apply-templates mode="filter_for_PIs"/>                    
        </fo:inline>
      </xsl:when>			
      <xsl:when test="child::processing-instruction('oxy_insert_start') or 
        child::processing-instruction('oxy_delete') or
        child::processing-instruction('oxy_comment_start')">
        <xsl:apply-templates  mode="filter_for_PIs"/>
      </xsl:when>
      <xsl:otherwise>
        <fo:basic-link xsl:use-attribute-sets="xref">
          <xsl:call-template name="buildBasicLinkDestination">
            <xsl:with-param name="scope" select="@scope"/>
            <xsl:with-param name="format" select="@format"/>
            <xsl:with-param name="href" select="@href"/>
          </xsl:call-template>
          
          <xsl:choose>
            <xsl:when
              test="not(@scope = 'external' or @format = 'html') and not($referenceTitle = '')">
              <xsl:copy-of select="$referenceTitle"/>
            </xsl:when>
            <xsl:when test="not(@scope = 'external' or @format = 'html')">
              <xsl:call-template name="insertPageNumberCitation">
                <xsl:with-param name="isTitleEmpty" select="'yes'"/>
                <xsl:with-param name="destination" select="$destination"/>
                <xsl:with-param name="element" select="$element"/>
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="text()">									
                  <xsl:value-of select="."/>									
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="@href"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </fo:basic-link>
      </xsl:otherwise>
    </xsl:choose>
    
    
    <!--
				Disable because of the CQ#8102 bug
				<xsl:if test="*[contains(@class,' topic/desc ')]">
					<xsl:call-template name="insertLinkDesc"/>
				</xsl:if>
		-->
    
    <xsl:if
      test="not(@scope = 'external' or @format = 'html') and not($referenceTitle = '') and not($element[contains(@class, ' topic/fn ')])">
      <!-- SourceForge bug 1880097: should not include page number when xref includes author specified text -->
      <xsl:if test="not(processing-instruction()[name()='ditaot'][.='usertext'])">
        <xsl:call-template name="insertPageNumberCitation">
          <xsl:with-param name="destination" select="$destination"/>
          <xsl:with-param name="element" select="$element"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>
    
  </xsl:template>
  


</xsl:stylesheet>
