<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    basics-referencing-elements-1-0.xsl               -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             cross-referencing of most types..                 -->
<!--             Note: xref when ref-type="fn" is handled in the   -->
<!--             module for footnotes, groups-footnotes-0-1.xsl.   -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) xref when ref-type is NOT fn, table-fn, bibr   -->
<!--             2) named template: make-reference                 -->
<!--             3) xref when ref-type="bibr"                      -->
<!--             4) xref when ref-type="table-fn"                  -->
<!--             5) supressing sup if appears to be a ref number   -->
<!--                                                               -->
<!-- CREATED FOR:                                                  -->
<!--             Digital Archive of Journal Articles               -->
<!--             National Center for Biotechnology Information     -->
<!--                (NCBI)                                         -->
<!--             National Library of Medicine (NLM)                -->
<!--                                                               -->
<!-- ORIGINAL CREATION DATE:                                       -->
<!--             September 2004                                    -->
<!--                                                               -->
<!-- CREATED BY: Kate Hamilton (Mulberry Technologies, Inc.)       -->
<!--             Deborah Lapeyre (Mulberry Technologies, Inc.)     -->
<!--                                                               -->
<!--             Suggestions for refinements and enhancements to   -->
<!--             this stylesheet suite should be sent in email to: -->
<!--                 publishing-dtd@ncbi.nlm.nih.gov               -->
<!-- ============================================================= -->


<!-- ============================================================= -->
<!--                    VERSION/CHANGE HISTORY                     -->
<!-- ============================================================= -->
<!--
     =============================================================

No.  Reason/Occasion                       (who) vx.x (yyyymmdd)

     =============================================================
 1.  Original version                            v1.0  20040823    
                                                                   -->
                                                                                                                                  
<!-- ============================================================= -->
<!--                    XSL STYLESHEET INVOCATION                  -->
<!-- ============================================================= -->


<xsl:transform version="2.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mml="http://www.w3.org/1998/Math/MathML">
                

<!-- ============================================================= -->
<!-- XREF: THE REFERENCING ELEMENT                                 -->
<!-- ============================================================= -->


<!-- The logic for determining what an xref does uses several
     possibly overlapping pieces of information: @rid, @ref-type,
     and the target element name. Therefore we use
     elimination logic (a horrid long choose) to deal with
     the exception cases, and then finally with the normal case.
     -->    

<!-- But first, deal separately and peremptorily
     with xrefs that have no rid -->
     
<xsl:template match="xref[not(@rid)]">
  
	<xsl:choose>

		<xsl:when test="@ref-type">
			<xsl:text>unspecified </xsl:text>
			<xsl:value-of select="@ref-type"/>
		</xsl:when>

		<xsl:otherwise>
			<xsl:text>unspecified object</xsl:text>
		</xsl:otherwise>

	</xsl:choose>

	<xsl:text> </xsl:text>
	
  <!-- put out the xref's content too, in case it helps -->
	<xsl:apply-templates/>

</xsl:template>



<!-- If we get here, we DO have an rid for the target element -->
<!-- Note: ref-types bibr, fn, and table-fn are handled in
     separate templates with "naturally" higher priority. -->
  <!-- SA00 -->
  
<xsl:template match="xref" priority="5">
  <fo:basic-link internal-destination="{@rid}">
    <xsl:value-of select="."/>
  </fo:basic-link>
</xsl:template>
  
<xsl:template match="xref">

  <xsl:param name="my-target-id" select="@rid"/>
  <xsl:param name="my-target-element" select="key('element-by-id', @rid)"/>
  
  
  <xsl:param name="my-ref-type">

    <xsl:choose>
    
      <!-- if the document specifies a @ref-type, use it -->
      <xsl:when test="@ref-type">
        <xsl:value-of select="@ref-type"/>
      </xsl:when>
      
      <!-- or, if can deduce a ref-type, do it -->
      <xsl:when test="$label-strings[@source-elem-name=local-name($my-target-element)]">
        <xsl:value-of select="$label-strings[@source-elem-name=local-name($my-target-element)]/@source-ref-type"/>
      </xsl:when>
      
      <!-- or, we can't tell -what- it is, 
           so record that -->
      <xsl:otherwise>unknown object</xsl:otherwise>
      
    </xsl:choose>
    
  </xsl:param>

   
  <!-- OK, now eliminate several abnormal cases,
       and hand off the normal cases to the make-reference template. -->
  
  <xsl:choose>
  
  <!-- some elements are NEVER supported objects of referencing,
       and here we produce the most useful content we can -->
  
    <xsl:when test="local-name($my-target-element)='table-wrap-group'
                 or local-name($my-target-element)='fig-group'
                 or local-name($my-target-element)='graphic'
                 or local-name($my-target-element)='mml:math'
                 or local-name($my-target-element)='tex-math'
                 or local-name($my-target-element)='media'">
                  
<!--      <fo:wrapper>
        <xsl:text>the </xsl:text>
        <xsl:value-of select="local-name($my-target-element)"/>
        <xsl:text> on page </xsl:text>
        <fo:page-number-citation ref-id="{$my-target-id}"/>
      </fo:wrapper>
-->    </xsl:when>
    

    <!-- For some @ref-types, referencing in body or back
         should do nothing (except put out the xref content). 
         (Anything in front is handled by the templates for
         page setup, cover page, and article opener.) -->
       
    <xsl:when test="$my-ref-type='contrib'
                  or $my-ref-type='kwd'
                  or $my-ref-type='other'">
          <xsl:if test="not(ancestor::front)">
            <xsl:apply-templates/>
          </xsl:if>
    </xsl:when>

 
    <!-- if we couldn't figure out a reference type at all,
         insert a "helpless" string and the target's page number -->
    
    <xsl:when test="$my-ref-type='unknown object'">
        <fo:wrapper>
          <xsl:text>[unspecified content </xsl:text>
          <xsl:value-of select="$my-target-id"/>
          <xsl:text> on page </xsl:text>
          <fo:page-number-citation ref-id="{$my-target-id}"/>
          <xsl:text>]</xsl:text>
        </fo:wrapper>
    </xsl:when>
    
    
     <!-- Now at last the "normal" case -->
    
    <xsl:otherwise>
      <xsl:call-template name="make-reference">
        <xsl:with-param name="my-target-id" select="$my-target-id"/>
        <xsl:with-param name="my-target-element" select="$my-target-element"/>
        <xsl:with-param name="my-ref-type" select="$my-ref-type"/>
      </xsl:call-template>
    </xsl:otherwise>
  
  </xsl:choose>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-REFERENCE                                -->
<!-- ============================================================= -->


<!-- If we get here, then we have an id for the target
     AND a ref-type.
     
     This template involves several parameters and a long choose, 
     whose virtue is that it keeps together and therefore visible 
     the (many) tiny variations of detail in the handling of xrefs. -->
     
<xsl:template name="make-reference">

  <!-- catch these params from the xref template -->
  <xsl:param name="my-target-id"/>
  <xsl:param name="my-target-element"/>
  <xsl:param name="my-ref-type"/>
  

  <!-- Set the string appropriate to the ref-type.
       It's straight'n'simple from the m:map for label-strings
       ... except for three exceptions, which we handle first. -->
           
  <xsl:param name="the-string">
  
    <xsl:choose>
   
      <!-- Deciding the label strings for references to table-wrap and 
           figure are both complicated. They're handled farther below.
           Here, we do NOT set $the-string for those two values of
           my-ref-type. -->

      <xsl:when test="$my-ref-type='table' or $my-ref-type='fig'"/> 
       
      <!-- A statement may have a @content-type, which will be more
           precise than the mere @ref-type; if it exists, use it
           instead of the @ref-type. -->
      <xsl:when test="$my-ref-type='statement'
                  and key('element-by-id', $my-target-id)/@content-type">
        <xsl:value-of select="key('element-by-id', $my-target-id)/@content-type"/>
      </xsl:when>
      
      <!-- the normal case: it's easy without those exceptions! -->
      <xsl:otherwise>
        <xsl:value-of select="$label-strings[@source-ref-type=$my-ref-type]/@display-string"/>  
      </xsl:otherwise>
      
     </xsl:choose>
     
  </xsl:param>
  
  <!-- Now capture the other data needed to handle figures and table-wraps:
       we need to record whether to make labels. 
       For figs this is more complicated than for table-wraps, because 
       figs have separate numbering sequences for each @fig-type. -->

  <xsl:param name="object-fig-type">
      <!-- this decision is externalized because it is
           also used by the fig template -->
    <xsl:if test="$my-target-element[self::fig]">
      <xsl:call-template name="decide-fig-type">
        <xsl:with-param name="object-gid" select="generate-id($my-target-element)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:param>
          
  <xsl:param name="make-fig-labels-for-type">
      <!-- this decision is externalized because it is
           also used by the fig template -->
      <xsl:if test="$my-target-element[self::fig]">
        <xsl:call-template name="decide-make-fig-labels-for-type">
          <xsl:with-param name="object-gid" select="generate-id($my-target-element)"/>
          <xsl:with-param name="object-fig-type" select="$object-fig-type"/>
        </xsl:call-template>
      </xsl:if>
  </xsl:param>
  
  <xsl:param name="make-table-wrap-labels">
    <!-- this decision is externalized because it is
         also used by the table-wrap template -->
    <xsl:if test="$my-target-element[self::table-wrap]">
      <xsl:call-template name="decide-make-table-wrap-labels">
        <xsl:with-param name="object-gid" select="generate-id($my-target-element)"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:param>
      

  <!-- Produce the xref's string -->
  <!-- Remember, the string for table-wraps and figures is still
       not set at this point. -->

  <xsl:value-of select="$the-string"/>
  
  <!-- Now produce the navigation content appropriate to
       the element type and its context. It's a long choose,
       because each object type uses slightly different
       navigation data. -->
 
  <xsl:choose>
      
    <!-- First the easy ones: no object numbering to generate -->
    
    <!-- section: title is required -->
    
    <xsl:when test="$my-ref-type='sec'">
      <xsl:text>entitled </xsl:text>
      <xsl:apply-templates select="key('element-by-id', $my-target-id)/title"
                           mode="xref"/>
    </xsl:when>
    
    
    <!-- boxed-text, list, and disp-quote:
         use title if any, and page number -->
    
    <xsl:when test="$my-ref-type='boxed-text'
                 or $my-ref-type='list'
                 or $my-ref-type='disp-quote'">
                 
      <!-- give title if any -->
      <xsl:for-each select="key('element-by-id', $my-target-id)/title">
        <xsl:text>entitled </xsl:text>
        <xsl:apply-templates mode="xref"/>
        <xsl:text> </xsl:text>
      </xsl:for-each>
      
      <!-- and page number -->
      <xsl:text>on page </xsl:text>
      <fo:page-number-citation ref-id="{$my-target-id}"/>
      
    </xsl:when>
    
    
    <!-- statement uses its label and a page number -->
    
    <xsl:when test="$my-ref-type='statement'">
    
      <!-- put out its label if any -->
      
      <xsl:for-each select="key('element-by-id', $my-target-id)/label">
        <xsl:apply-templates/>
        <xsl:text> </xsl:text>
      </xsl:for-each>
      
      <!-- and a page number -->
      
      <xsl:text>on page </xsl:text>
      <fo:page-number-citation ref-id="{$my-target-id}"/>
     
    </xsl:when>
      
      
    <!-- supplementary-material: may have label, 
         but the design uses only page number -->

    <xsl:when test="$my-ref-type='supplementary-material'">
                 
      <xsl:text>on page </xsl:text>
      <fo:page-number-citation ref-id="{$my-target-id}"/>
      
    </xsl:when>
    

    <!-- Next bunch are the object types that get numbered. -->
    
    <!-- simple numbering: app -->
    
    <xsl:when test="$my-ref-type='app'">
      <xsl:call-template name="calculate-app-number">
        <xsl:with-param name="object-id" select="$my-target-id"/>
      </xsl:call-template>
    </xsl:when>
    

    <!-- disp-formula has document-dependent numbering: 
         If a disp-formula is referenced, we'll number it -->
    
    <xsl:when test="$my-ref-type='disp-formula'">
               
      <xsl:call-template name="calculate-disp-formula-number">
        <xsl:with-param name="object-id" select="$my-target-id"/>
      </xsl:call-template>
                  
    </xsl:when>
    
  
    <!-- logic for chem-struct is same as for disp-formula:
         if a chem-struct is referenced, we'll number it -->

    <xsl:when test="$my-ref-type='chem'">
               
      <xsl:call-template name="calculate-chem-struct-number">
        <xsl:with-param name="object-id" select="$my-target-id"/>
      </xsl:call-template>
                  
    </xsl:when>    
    
    
    <!-- Now at last we're going to decide
         and produce navigation data for figures and tables.
         
         Depending on the document content, we may generate a label, 
         use document's label, or have no useable label. 
         
         This logic calls on the named template determine-fig[or table]-label, 
         which is used also when setting the object itself. -->
    
    <!-- figure -->
    
    <xsl:when test="$my-ref-type='fig'">    
      <xsl:for-each select="key('element-by-id', $my-target-id)">
      
        <xsl:choose>
          <!-- if it's legitimate to use a label, AND we can determine one -->
          <xsl:when test="(key('legit-figs-no-fig-type', 'fig')[@id=$my-target-id/@rid]
                           or key('legit-figs-by-fig-type', $object-fig-type)[@id=$my-target-id/@rid] )
          
                      and ($make-fig-labels-for-type='yes' or child::label)">
            <xsl:message>FIG2: <xsl:sequence select="$object-fig-type"/></xsl:message>      
            
            <xsl:call-template name="determine-fig-label">
              <xsl:with-param name="object-gid" select="generate-id()"/>
              <xsl:with-param name="object-fig-type" select="$object-fig-type"/>
            </xsl:call-template>          
          </xsl:when>
          
          <!-- otherwise use the default string, caption/title if any,
               and the page number -->
          <xsl:otherwise>
            <xsl:value-of select="$fig-type-strings[@source-fig-type='generic']/@default-string"/>
            <xsl:if test="caption/title">
             <xsl:text>entitled </xsl:text>
              <xsl:apply-templates select="caption/title" mode="pass-through"/>
              <xsl:text> </xsl:text>
            </xsl:if>
<!--            <xsl:text>on page </xsl:text>
            <fo:page-number-citation ref-id="{$my-target-id}"/>
-->          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
      
    </xsl:when>
    
    
    <!-- table logic is same as for figure, except no target @...-type to worry about -->
    
    <xsl:when test="$my-ref-type='table'">
                                 
      <xsl:for-each select="key('element-by-id', $my-target-id)">
      
        <xsl:choose>
        
          <!-- if it's legitimate to use a label AND we can determine one -->
          <xsl:when test="key('legit-table-wraps', 'table-wrap')[@id=$my-target-id]
          
                      and ($make-table-wrap-labels='yes' or child::label)">
                      
            <xsl:call-template name="determine-table-wrap-label">
              <xsl:with-param name="object-gid" select="generate-id()"/>
            </xsl:call-template>          
          </xsl:when>
          
          <!-- otherwise use the default string, caption/title if any, 
               and the page number -->
          <xsl:otherwise>
            <xsl:value-of select="$label-strings[@source-elem-name='table-wrap']/@default-string"/>
            <xsl:if test="caption/title">
             <xsl:text>entitled </xsl:text>
              <xsl:apply-templates select="caption/title" mode="pass-through"/>
              <xsl:text> </xsl:text>
            </xsl:if>
            <xsl:text>on page </xsl:text>
            <fo:page-number-citation ref-id="{$my-target-id}"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
                 
    </xsl:when>
    
    
    <!-- if an xref is -still- unhandled by all that, 
         generate nothing. -->
    <xsl:otherwise/>

 </xsl:choose>
    

  <!-- finally, in most cases, also put out the xref's content -->
  <xsl:choose>
    <xsl:when test="$my-ref-type='fig' 
                 or $my-ref-type='table'
                 or $my-ref-type='supplementary-material'"/>
    <xsl:otherwise>
      <xsl:apply-templates/>
    </xsl:otherwise>
  </xsl:choose>

</xsl:template>


<xsl:template match="title | label" mode="xref">
  <xsl:apply-templates/>
</xsl:template>



<!-- ============================================================= -->
<!-- BIBLIOGRAPHIC REFERENCES                                      -->
<!-- ============================================================= -->


<!-- Source docs have punctuation when recording
     multiple xrefs; we assume this is the norm and
     so calculating ranges is not appropriate -->
     
<xsl:template match="xref[@ref-type='bibr']">
  
  <fo:wrapper>
    <xsl:text>[</xsl:text>
    <xsl:call-template name="calculate-ref-number">
      <xsl:with-param name="object-id" select="@rid"/>
    </xsl:call-template>
    <xsl:text>]</xsl:text>
  </fo:wrapper>
</xsl:template>


<!-- ============================================================= -->
<!-- FOOTNOTE REFERENCES (EXCEPT TABLE FOOTNOTES)                  -->
<!-- ============================================================= -->


<!-- xref @ref-type=fn is handled in the footnote stylesheet,
     rather than here, in order to keep in one place the logic
     of footnotes-without-xrefs and footnotes-with-xrefs. -->


<!-- ============================================================= -->
<!-- XREF when REF-TYPE='TABLE-FN'                                 -->
<!-- ============================================================= -->

<!-- Calculates and puts out only the reference number.
     The table-wrap-foot will call the fn in mode=pseudo-footnote. -->
     
<!-- Priority = 1 to resolve priority conflict with other xref
     templates. -->

<xsl:template match="xref[@ref-type='table-fn']">

  <xsl:param name="object-id" select="@rid"/>
  <xsl:param name="the-key" select="concat(@ref-type, '-refs')"/>
  
  <xsl:param name="my-number">
    <xsl:for-each select="key($the-key, $object-id)[1]">
      <xsl:call-template name="calculate-table-fn-number">
        <xsl:with-param name="object-id" select="$object-id"/>
        <xsl:with-param name="the-key" select="$the-key"/>
      </xsl:call-template>
    </xsl:for-each>
  </xsl:param>
  
  <xsl:call-template name="set-footnote-reference">
    <xsl:with-param name="my-number" select="$my-number"/>
  </xsl:call-template>
    
  <!-- don't put out the xref's content -->
  <!-- don't put out the footnote now: table-wrap-foot will do it -->
  
</xsl:template>


<!-- ============================================================= -->
<!-- SUPPRESS SOURCE'S XREF NUMBERING IF DONE BY "SUP"             -->
<!-- ============================================================= -->


<!-- Can't rely on hand-created numbers being appropriate
     to a design's numbering scheme (e.g., the numbering of
     fn in frontmatter is in this design a separate sequence
     from the body/back fn), so suppress them insofar as
     we can identify them. -->
     
<xsl:template match="xref/child::node()[1][self::sup]"/>



</xsl:transform>