<?xml version="1.0" encoding="UTF-8"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:saxon="http://saxon.sf.net/"
	xmlns:m="http://www.w3.org/1998/Math/MathML"
  exclude-result-prefixes="saxon ditaarch" version="2.0"
  xmlns:ditaarch="http://dita.oasis-open.org/architecture/2005/">

  <!--  xsl:output doctype-public="urn:pubid:org.iet:doctypes:dita:regulation"
    doctype-system="regulation.dtd"/ -->

  <xsl:param name="show_deleted_content_as_comment">false</xsl:param>
  <xsl:param name="show_comment_content_as_comment">false</xsl:param>
  <xsl:param name="change_element">ph</xsl:param>

<!-- ================= begining of the base part of the transform ======================== -->
  <!-- match every element and copy it to output -->
  <xsl:template match="*">
    <xsl:variable name="element_name" select="name()"/>
    <xsl:element name="{$element_name}">
      <xsl:apply-templates
        select="@*[not(name()='domains') and
        not(name()='name')]"/>
      <xsl:apply-templates mode="filter"/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="node()" mode="filter">
   <xsl:choose>
     <xsl:when test="self::text()"><xsl:apply-templates select="."/></xsl:when>
     <xsl:when test="self::processing-instruction()"><xsl:apply-templates select="."/></xsl:when>
     <xsl:when test="self::comment()"><xsl:apply-templates select="."/></xsl:when>
     <xsl:when test="self::*">
       <xsl:choose>
         <xsl:when test="preceding-sibling::processing-instruction('oxy_insert_end')[1] and
           following-sibling::processing-instruction('oxy_insert_start')[1]">
           <xsl:apply-templates select="."/>
         </xsl:when>
         <xsl:when test="preceding-sibling::processing-instruction('oxy_insert_start')[1] and
           following-sibling::processing-instruction('oxy_insert_end')[1]"/>
         <xsl:otherwise><xsl:apply-templates select="."/></xsl:otherwise>
       </xsl:choose>
     </xsl:when>
     <xsl:otherwise><xsl:apply-templates select="."/></xsl:otherwise>
   </xsl:choose>     
  </xsl:template> 
  <!-- ================= ending of the base part of the transform ======================== -->  

  
  <!-- ================ begin handling of nodes that occur "within" PIs =================== -->

  <!-- match the Oxygen PI that signals the start of a change and process the nodes 
  that occur between it and PI that signals the end of the change -->
  <xsl:template match="processing-instruction('oxy_insert_start')">
    <!-- count how many PIs are to be found on the following-sibling axis -->
    <xsl:variable name="x" select="count(following-sibling::processing-instruction())"/>
    
    <!-- output the DITA element that will mark the change -->
    <xsl:element name="{$change_element}">
      <!-- output the DITA status attribute and its value -->
      <xsl:attribute name="status">changed</xsl:attribute>
      <!-- apply templates to every following-sibling node so long as the count of processing
        instructions that follow that node are equal to "x": this means we only get the nodes
          that are "inside" the current PI start-end -->
      <xsl:apply-templates
        select="following-sibling::node()[count(following-sibling::processing-instruction())=$x]"
        mode="output"/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="*" mode="output">
    <!-- if you are a element node inside the PIs, you go to output
      otherwise, you get ignored -->
    <xsl:variable name="element_name" select="name()"/>
    <xsl:element name="{$element_name}">
      <xsl:apply-templates
        select="@*[not(name()='domains') and
        not(name()='name')]"/>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="text()" mode="output">
    <!-- if you are a text() node inside the PIs, you go to output
      otherwise, you get ignored -->
    <xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="text()[count(following-sibling::processing-instruction()) mod 2 = 1]">
    <!-- since text nodes that occur within the PI start-end pair are processing in the template
      that matches the start PI, this template blocks all text() nodes that fall inside the PIs start-end pair so that we
      don't dupliate content -->
    <!--<xsl:comment>@@ blocked: <xsl:value-of select="."/></xsl:comment>-->
  </xsl:template>
  
  <!-- match the Oxygen PI that signals the end of a change and do nothing -->
  <xsl:template match="processing-instruction()[name()='oxy_insert_end']"/>
<!-- ================ end handling of nodes that occur "within" PIs =================== -->


<!-- ================ begin handling of deletion PIs =================== -->

  <!-- match the Oxygen PI that signals the start of a deletion. Process the deleted content is
    optional depending upon the value of the "show_deleted_content_as_comment" param -->
  <xsl:template match="processing-instruction('oxy_delete')">
    <xsl:variable name="x" select="count(preceding-sibling::processing-instruction())"/>

    <!-- there may be text() nodes that preced the start deletion PI. We need to output them -->
    <xsl:if test="$x = 0">
      <xsl:apply-templates select="preceding-sibling::node()" mode="yes"/>
    </xsl:if>

    <!-- Process the deleted content depending upon the value of the "show_deleted_content_as_comment" param -->
    <xsl:if test="$show_deleted_content_as_comment = 'true'">
      <xsl:variable name="c" select="substring-after(., 'content=')"/>
      <xsl:variable name="clen" select="string-length($c) - 2"/>
      <xsl:variable name="delete_content" select="substring($c, 2 , $clen)"/>
      <xsl:comment><xsl:value-of select="$delete_content"/></xsl:comment>
    </xsl:if>
  </xsl:template>
<!-- ================ end handling of deletion PIs =================== -->

  <!-- ================ begin handling of comment PIs =================== -->
  
  <!-- match the Oxygen PI that signals the start of a comment. Process the deleted content is
    optional depending upon the value of the "show_comment_content_as_comment" param -->
  <xsl:template match="processing-instruction('oxy_comment_start')">
    <xsl:variable name="x" select="count(preceding-sibling::processing-instruction())"/>
    <xsl:variable name="y" select="count(following-sibling::processing-instruction())"/>    
    <!-- there may be text() nodes that preced the start comment PI. We need to output them -->
    <xsl:if test="$x = 0">
      <xsl:apply-templates select="preceding-sibling::node()" mode="yes"/>
    </xsl:if>
    
    <!-- Process the comment content depending upon the value of the "show_comment_content_as_comment" param -->
    <xsl:if test="$show_comment_content_as_comment = 'true'">
      <xsl:variable name="c" select="substring-after(., 'comment=')"/>
      <xsl:variable name="clen" select="string-length($c) - 2"/>
      <xsl:variable name="comment_content" select="substring($c, 2 , $clen)"/>
      <xsl:comment><xsl:value-of select="$comment_content"/></xsl:comment>
    </xsl:if>

    <xsl:apply-templates
      select="following-sibling::node()[count(following-sibling::processing-instruction())=$y]"
      mode="output"/>

  </xsl:template>

  <xsl:template match="processing-instruction()[name()='oxy_comment_end']"/>
  <!-- ================ end handling of comment PIs =================== -->


<!-- ================ general templates =================== -->
  <xsl:template match="@ditaarch:*"/>

  <xsl:template match="@*">
    <xsl:copy>
      <xsl:value-of select="."/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
