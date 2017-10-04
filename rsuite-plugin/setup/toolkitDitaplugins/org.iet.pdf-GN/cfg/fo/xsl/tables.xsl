<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:opentopic-func="http://www.idiominc.com/opentopic/exsl/function"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:dita2xslfo="http://dita-ot.sourceforge.net/ns/200910/dita2xslfo"
  xmlns:exsl="http://exslt.org/common"
  xmlns:exslf="http://exslt.org/functions"
  exclude-result-prefixes="exsl exslf opentopic-func xs dita2xslfo"
  version="2.0">


    <xsl:template match="*[contains(@class, ' topic/table ')]/*[contains(@class, ' topic/title ')]">
        <fo:block xsl:use-attribute-sets="table.title">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates/>   
        </fo:block>
    </xsl:template>

    <xsl:template match="*[contains(@class, ' topic/table ')]/*[contains(@class, ' topic/title ')]/*[contains(@class, ' hi-d/b ')]/*[contains(@class, ' d4p_simplenum-d/d4pSimpleEnumerator ')]">
      <xsl:apply-templates/>   
    </xsl:template>

  <xsl:template match="*[contains(@class,' topic/table ')][descendant::cite_margin]" priority="10">
    <fo:block font-size="10pt" start-indent="25pt">
      <fo:table inline-progression-dimension="100%" start-indent="-25pt" table-layout="fixed">
        <fo:table-column column-number="1" column-width="proportional-column-width(10)"/>
        <fo:table-column column-number="2" column-width="proportional-column-width(90)"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell>
<!--              <xsl:for-each select=".//cite_margin">
                <fo:block end-indent="3pt" start-indent="-25pt" text-align="right" color="blue" font-style="italic">
                    <xsl:value-of select="."/>
                </fo:block>
              </xsl:for-each>
-->            </fo:table-cell>
            <fo:table-cell padding-left="27pt">
              <fo:block xsl:use-attribute-sets="table.title">
                <xsl:call-template name="commonattributes"/>
                <xsl:apply-templates/>   
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:block>
  </xsl:template>
  
  <xsl:template match="cite_margin[ancestor::*[contains(@class,' topic/table ')]]" priority="15"/>

  <xsl:template match="*[contains(@class, ' topic/tbody ')]/*[contains(@class, ' topic/row ')][descendant::cite_margin]/*[contains(@class, ' topic/entry ')]">
    <xsl:choose>
      <xsl:when test="count(preceding-sibling::entry)=0">
        <fo:table-cell xsl:use-attribute-sets="tbody.row.entry">
          <xsl:call-template name="commonattributes"/>
          <xsl:call-template name="applySpansAttrs"/>
          <xsl:call-template name="applyAlignAttrs"/>
          <xsl:call-template name="generateTableEntryBorder"/>
          <fo:table inline-progression-dimension="100%" table-layout="fixed">
            <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
            <fo:table-column column-number="2" column-width="proportional-column-width(99)"/>
            <fo:table-body>
              <fo:table-row>
                <fo:table-cell>
                  <fo:block font-weight="normal" color="blue" font-style="italic" margin-left="-20pt" margin-right="-16pt" text-align="right">
                    <xsl:value-of select=".//cite_margin"/>  
                    <xsl:value-of select="following-sibling::entry//cite_margin"/>  
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell padding-left="27pt">
                  <fo:block xsl:use-attribute-sets="tbody.row.entry__content">
                    <xsl:call-template name="processEntryContent"/>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
        </fo:table-cell>
      </xsl:when>
      <xsl:otherwise>
        <fo:table-cell xsl:use-attribute-sets="tbody.row.entry">
          <xsl:call-template name="commonattributes"/>
          <xsl:call-template name="applySpansAttrs"/>
          <xsl:call-template name="applyAlignAttrs"/>
          <xsl:call-template name="generateTableEntryBorder"/>
          <fo:block xsl:use-attribute-sets="tbody.row.entry__content">
            <xsl:call-template name="processEntryContent"/>
          </fo:block>
        </fo:table-cell>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>