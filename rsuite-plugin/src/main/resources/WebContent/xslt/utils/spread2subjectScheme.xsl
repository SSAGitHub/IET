<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  xmlns:sh="urn:schemas-microsoft-com:office:spreadsheet"
  exclude-result-prefixes="xs xd sh"
  version="2.0">
  
  <!-- Generates a subject scheme map from an Office 2004 XML spreadsheet -->
  
  <xsl:output method="xml"
    indent="yes"
    doctype-public="-//OASIS//DTD DITA Subject Scheme Map//EN" 
    doctype-system="subjectScheme.dtd"  
  />
  
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="sh:Table">
    <subjectScheme>
      <xsl:for-each-group select="sh:Row" group-by="string(sh:Cell[1])">
        <subjectdef>
          <topicmeta>
            <navtitle><xsl:value-of select="./sh:Cell[1]"/></navtitle>
          </topicmeta>
          <xsl:for-each-group select="current-group()" group-by="string(sh:Cell[2])">
            <subjectdef>
              <topicmeta>
                <navtitle><xsl:value-of select="./sh:Cell[2]"/></navtitle>
              </topicmeta>
              <xsl:for-each-group select="current-group()" group-by="string(sh:Cell[3])">
                <xsl:if test="current-grouping-key() != ''">
                  <subjectdef>
                    <topicmeta>
                      <navtitle><xsl:value-of select="./sh:Cell[3]"/></navtitle>
                    </topicmeta>
                    <xsl:for-each-group select="current-group()" group-by="string(sh:Cell[4])">
                      <xsl:if test="current-grouping-key() != ''">
                        <subjectdef>
                          <topicmeta>
                            <navtitle><xsl:value-of select="./sh:Cell[4]"/></navtitle>
                          </topicmeta>
                        </subjectdef>
                      </xsl:if>
                    </xsl:for-each-group>  
                    
                  </subjectdef>
                </xsl:if>
              </xsl:for-each-group>  
            </subjectdef>
          </xsl:for-each-group>
        </subjectdef>
      </xsl:for-each-group>
    </subjectScheme>
  </xsl:template>
  
</xsl:stylesheet>