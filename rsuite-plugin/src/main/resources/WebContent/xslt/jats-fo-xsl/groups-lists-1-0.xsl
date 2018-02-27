<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    groups-lists-1-0.xsl                              -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles lists,    -->
<!--             including definition lists.                       -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) list + def-list                                -->
<!--             2) deflist term and def-head                      -->
<!--             3) list-item + def-item                           -->
<!--             4) Named template: make-mark                      -->
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

<xsl:transform version="1.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mml="http://www.w3.org/1998/Math/MathML">


<!-- ============================================================= -->
<!-- LISTS and DEF-LISTS                                           -->
<!-- ============================================================= -->


<!-- Lists and def-lists are handled together because they use the
     same (many) variations: a prefix-word, a multi-level numbering
     scheme, and a label which may override all that.
     Rather than repeating the extensive logic for list and def-list,
     they're handled together with variations as required. -->

<!-- List terminology:

     mark         - a term for the [bullet, number, etc.] that appears 
                  to the left of the first line in a list item.
                  The mark is assembled and calculated from data
                  on the list -and- on the individual list-item.

     @list-type   - an attribute on list or def-list specifying 
                  what class of mark a list-item gets: simple,
                  bullet, order, alpha-upper or -lower,
                  roman-upper or -lower. 
                  
     @prefix-word - an attribute on list or def-list whose value
                  will be displayed along with the mark.
                  
     label        - an element that may appear in list-item or def-item
                  which will over-ride any generated mark. These aren't
                  allowed in the publishing DTD, but the logic was done
                  and does no harm, so it's been left in place.
                  
-->


<xsl:template match="list">


   <!-- First, parameterize values to do with positioning the list parts
        (list-offset, start-to-start, end-to-start, space-before, and 
        space-after), which are all used in -this- template. -->
   
   <xsl:param name="list-offset">

     <xsl:choose>
       <xsl:when test="self::list and count(ancestor::list) = 0">
         <xsl:value-of select="0"/> <!-- picas (pc) -->
       </xsl:when>

       <xsl:otherwise>
         <xsl:value-of select="0"/>
       </xsl:otherwise>
     </xsl:choose>

   </xsl:param>

   <xsl:param name="start-to-start">

     <xsl:choose>
       <xsl:when test="@list-type='simple'">0in</xsl:when>
       <xsl:when test="@list-type='bullet'">10pt</xsl:when>
        <xsl:when test="@list-type='order'">15pt</xsl:when>
        <xsl:when test="@list-type='roman-lower'">15pt</xsl:when>
       <xsl:when test="self::list">0.3in</xsl:when>
       <xsl:when test="self::def-list">40pt</xsl:when>
     </xsl:choose>

   </xsl:param>
   
   <xsl:param name="end-to-start">

     <xsl:choose>
       <xsl:when test="@list-type='simple'">0in</xsl:when>
       <xsl:when test="self::list">0.05in</xsl:when>
       <xsl:when test="self::def-list">0.1in</xsl:when>
     </xsl:choose>

   </xsl:param>

  <!-- If list is within a para, 6pt above since the text won't do that itself.
       Otherwise, let the preceding sibling make the appropriate space -->

  <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
  <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>
  
  <!-- Next, parameterize three values to do with the list per se:
       1. list-behavior. 2. mark-type. 3. prefix-word.
  
       The data obtained here on list (or def-list) will be passed
       to the list-item template for assembling the item's mark.
  -->
  

  <!-- 1. list-behavior. It bundles together all list types that
       involve enumeration, which all take the same formatting
       and the same xsl syntax, so can be handled by one statement. -->
  <!-- passed to named template make-mark -->

  <xsl:param name="list-behavior">
  
    <xsl:choose>
      <xsl:when          test="not(@list-type)">simple</xsl:when>
      <xsl:when      test="@list-type='simple'">simple</xsl:when>
      <xsl:when      test="@list-type='bullet'">unordered</xsl:when>
      <xsl:when       test="@list-type='order'">enumerated</xsl:when>
      <xsl:when test="@list-type='alpha-lower'">enumerated</xsl:when>
      <xsl:when test="@list-type='alpha-upper'">enumerated</xsl:when>
      <xsl:when test="@list-type='roman-lower'">enumerated</xsl:when>
      <xsl:when test="@list-type='roman-upper'">enumerated</xsl:when>
      <xsl:otherwise>unordered</xsl:otherwise>
    </xsl:choose>
    
  </xsl:param>

  <!-- 2. mark-type. This records what numbering scheme to use 
       (unless the list-item overrides it). -->
  <!-- passed to named template make-mark -->
  
  <xsl:param name="mark-type">
  
    <xsl:choose>
    
      <xsl:when test="@list-type='bullet' or not(@list-type)">
        <xsl:variable name="list-depth" 
                    select="count(ancestor-or-self::list[@list-type='bullet'])"/>
        <xsl:choose>
        <!-- solid bullet, dash, solid diamond -->
           <xsl:when test="$list-depth mod 3 = 1">&#x2022;</xsl:when>
           <xsl:when test="$list-depth mod 3 = 2">&#x2013;</xsl:when>
           <xsl:when test="$list-depth mod 3 = 0">&#x2666;</xsl:when>
        </xsl:choose>
      </xsl:when>

      <xsl:when test="@list-type='order'">
        <xsl:variable name="list-depth"
                    select="count(ancestor-or-self::list[@list-type='order'])"/>
        <xsl:choose>
          <xsl:when test="$list-depth mod 6 = 1">1.</xsl:when>
          <xsl:when test="$list-depth mod 6 = 2">a.</xsl:when>
          <xsl:when test="$list-depth mod 6 = 3">1)</xsl:when>
          <xsl:when test="$list-depth mod 6 = 4">a)</xsl:when>
          <xsl:when test="$list-depth mod 6 = 5">i.</xsl:when>
          <xsl:when test="$list-depth mod 6 = 0">i)</xsl:when>
        </xsl:choose>
      </xsl:when>

      <xsl:when test="@list-type='alpha-lower'">a.</xsl:when>
      <xsl:when test="@list-type='alpha-upper'">A.</xsl:when>
      <xsl:when test="@list-type='roman-lower'">i.</xsl:when>
      <xsl:when test="@list-type='roman-upper'">I.</xsl:when>

    </xsl:choose>

  </xsl:param>
  
  <!-- 3. prefix-word. records value for prefix-word, if any -->
  <!-- The $prefix-word will be:
          - displayed by the list item before or after the item deco,
          - used as a signal to give the item deco more room
          - used as a signal to pop the item body down a line -->
  <!-- passed to named template make-mark -->
  
  <xsl:param name="prefix-word">
  
    <xsl:choose>
      <xsl:when test="normalize-space(@prefix-word) != ''">
        <xsl:value-of select="@prefix-word"/>
      </xsl:when>
      <xsl:otherwise/>
    </xsl:choose>
    
  </xsl:param>
  

  <!-- Now at last! put out the list block, which uses the first 5 params.
       Then pass the last three params to the list items (or def-items). -->
   
  <fo:list-block id="{@id}" space-before="10pt" space-before.precedence="1" space-after="10pt"
      space-after.precedence="1" margin-left="{$list-offset}pc"
      provisional-distance-between-starts="{$start-to-start}" provisional-label-separation="{$end-to-start}"
      text-indent="0pt">

      <xsl:apply-templates>
        <xsl:with-param name="list-behavior" select="$list-behavior"/>
        <xsl:with-param name="mark-type" select="$mark-type"/>
        <xsl:with-param name="prefix-word" select="$prefix-word"/>
      </xsl:apply-templates>

   </fo:list-block>

</xsl:template>



<!-- Actually, a few optional things may precede a list's items:
     possibly a title; if we're in a def-list, possibly also
     a label, and perhaps a term-head and def-head. -->
     
<xsl:template match="list/title | def-list/title">

  <fo:block text-align="center"
            space-after="{$leading-below-titles-big}"
            space-after.precedence="force">
    <fo:wrapper
            font-weight="bold">
      <xsl:apply-templates/>
    </fo:wrapper>
  </fo:block>

</xsl:template>


<!-- ============================================================= -->
<!-- DEF-LIST - TERM-HEAD AND DEF-HEAD                             -->
<!-- ============================================================= -->


<!-- term-head+def-head need to be self-sufficient because
     there's no wrapper around them in the source.
     So term-head puts out its own fo list item -and-
     mark (including most especially its own content), 
     then calls the def-head into a list item body. 
 -->

     
<xsl:template match="term-head">

  <fo:list-item space-after="8pt">

    <!-- Format the term-head ("col 1") -->
    <fo:list-item-label end-indent="label-end()">
      <fo:block>
        <fo:wrapper font-weight="bold">
          <xsl:apply-templates/>
        </fo:wrapper>
      </fo:block>
    </fo:list-item-label>

    <!-- Then process the def-head ("col 2") -->
    <fo:list-item-body start-indent="body-start()">
      <fo:block>
        <xsl:apply-templates select="following-sibling::def-head" mode="heading"/>
      </fo:block>
    </fo:list-item-body>

  </fo:list-item>

</xsl:template>


<xsl:template match="def-head" mode="heading">

  <fo:wrapper font-weight="bold">
    <xsl:apply-templates/>
  </fo:wrapper>

</xsl:template>


<!-- suppress def-head in no-mode, because
     it's called explicitly by term-head in mode heading -->
<xsl:template match="def-head"/>


<!-- ============================================================= -->
<!-- LIST-ITEM AND DEF-ITEM                                        -->
<!-- ============================================================= -->


<xsl:template match="list-item">

<!-- received from the list template,
     to be passed through to make-li-deco -->
<xsl:param name="list-behavior"/>
<xsl:param name="mark-type"/>
<xsl:param name="prefix-word"/>

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="self::list-item">
        <xsl:value-of select="$leading-around-narrative-blocks"/>
      </xsl:when>
      <xsl:when test="self::def-item">
        <!--<xsl:value-of select="$leading-around-def-items"/>-->
      </xsl:when>
    </xsl:choose>
  </xsl:param>

 
  <fo:list-item space-after="{$space-after}" space-after.precedence="1">

    <!-- Assemble and produce the item mark -->

    <xsl:call-template name="make-mark">
      <xsl:with-param name="list-behavior" select="$list-behavior"/>
      <xsl:with-param name="mark-type" select="$mark-type"/>
      <xsl:with-param name="prefix-word" select="$prefix-word"/>
    </xsl:call-template>

    <!-- Then produce the item body -->
   
    <fo:list-item-body start-indent="body-start()">
    
      <xsl:if test="parent::list[@list-type != 'simple'] or parent::def-list">
          <xsl:attribute name="start-indent">
            <xsl:text>body-start()</xsl:text>
          </xsl:attribute>
        </xsl:if>
        <!-- if there's a prefix-word, pop the item body down a line -->
      <xsl:if test="$prefix-word != ''">
        <fo:block>&#x00A0;</fo:block>
      </xsl:if>
      
      <fo:block>
          <xsl:apply-templates/>
      </fo:block>
    </fo:list-item-body>

  </fo:list-item>

</xsl:template>


<!-- ****************************************** DEF-LIST **************************************** -->

  <xsl:template match="def-list">


    <!-- First, parameterize values to do with positioning the list parts
        (list-offset, start-to-start, end-to-start, space-before, and 
        space-after), which are all used in -this- template. -->


    <fo:table id="{@id}" space-before="10pt" space-before.precedence="1" space-after="10pt"
      space-after.precedence="1" margin-left="0pt" text-indent="0pt">
      <fo:table-body>
        <xsl:apply-templates/>
      </fo:table-body>
    </fo:table>

  </xsl:template>

  <xsl:template match="def-item" priority="10">
    <fo:table-row>
      <xsl:apply-templates/>
    </fo:table-row>
  </xsl:template>

  <!-- def-item/def is reached via the list-item block  -->
<xsl:template match="term" priority="10">
  <fo:table-cell>
      <fo:block>
        <xsl:apply-templates/>
      </fo:block>
    </fo:table-cell>
</xsl:template>


<xsl:template match="def" priority="10">
    <fo:table-cell padding-left="10pt">
      <fo:block>
        <xsl:apply-templates/>
      </fo:block>
    </fo:table-cell>
  </xsl:template>


  <!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-MARK                                     -->
<!-- ============================================================= -->


<xsl:template name="make-mark">

  <!-- received from parent list-item template -->
  <xsl:param name="list-behavior"/>
  <xsl:param name="mark-type"/>
  <xsl:param name="prefix-word"/>

  <!-- Can't use the fo label-end() function for label width,
       because need to calculate with it if there's a prefix-word. -->
   
  <!-- Note: Sometimes prefix-word is the first thing and sometimes not. 
       So although this is detailed it's doing what it needs to. -->
      
  <xsl:param name="mark-width">
      <xsl:choose>
        <xsl:when test="self::def-item">
          <xsl:value-of select="4"/> <!-- picas (pc) -->
        </xsl:when>
        <xsl:when test="$prefix-word != ''">
          <!-- give ample room: we'll pop the item body down a line -->
          <xsl:value-of select="2.5"/> 
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="18"/> 
        </xsl:otherwise>
      </xsl:choose>
  </xsl:param>     


  <!-- context node is list-item (or def-item) -->
  
  <fo:list-item-label end-indent="label-end()">

    <fo:block text-align="left">
    
      <fo:wrapper>

        <xsl:choose>
       
        <!-- If there's a label, the mark-type is irrelevant: produce
                                   prefix-word_label 
                                   prefix-word_label_term -->
          <xsl:when test="child::*[1]=label">

            <!-- if there's a prefix-word, prepend it to the label -->
            <xsl:if test="$prefix-word != ''">
                    <xsl:value-of select="$prefix-word"/>
                    <xsl:text> </xsl:text>
            </xsl:if>

            <xsl:apply-templates select="label" mode="mark"/>

            <!-- if there's a term, get it -->
            <xsl:if test="term">
              <xsl:text> </xsl:text>
              <xsl:apply-templates select="term" mode="mark"/>
            </xsl:if>

          </xsl:when>


        <!-- Or, if it's a bullet list, produce
                                   mark-type_prefix-word
                                   mark-type_prefix-word_term -->
          <xsl:when test="$list-behavior='unordered'">

            <fo:inline baseline-shift="super">
              <xsl:value-of select="$mark-type"/>

              <!-- if there's a prefix-word, append it, with a no-break space-->
              <xsl:if test="$prefix-word != ''">
                &#x00A0;<xsl:value-of select="$prefix-word"/>
              </xsl:if>

              <!-- if there's a term, get it -->
              <xsl:if test="term">
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="term" mode="mark"/>
              </xsl:if>

            </fo:inline>

          </xsl:when>
        

          <!-- Or, if it's any kind of enumerated list, produce
                                     prefix-word mark-type
                                     prefix-word mark-type_term -->
          <xsl:when test="$list-behavior='enumerated'">

            <!-- if there's a prefix-word, prepend it, with no space -->
            <xsl:if test="$prefix-word != ''">
              <xsl:value-of select="$prefix-word"/>
            </xsl:if>

            <xsl:number format="{$mark-type}"/>

            <!-- if there's a term, get it -->
            <xsl:if test="term">
              <xsl:text> </xsl:text>
              <xsl:apply-templates select="term" mode="mark"/>
            </xsl:if>

          </xsl:when>


        <!-- Or, if it's a "simple" list, we don't use mark-type: produce
                                       prefix-word
                                       prefix-word_term 
                                       term             -->
        <xsl:when test="$list-behavior='simple'">
        
          <!-- if there's a prefix-word, prepend it, with no space -->
          <xsl:if test="$prefix-word != ''">
            <xsl:value-of select="$prefix-word"/>
            
            <!-- if we'll have a term, put out a space first -->
            <xsl:if test="term">
              <xsl:text> </xsl:text>
            </xsl:if>
          </xsl:if>
          
          <!-- if there's a term, get it -->
          <xsl:apply-templates select="term" mode="mark"/>

        </xsl:when>
        
        
        <!-- If it's none of the above, we don't know WHAT it is:
             produce nothing -->
        <xsl:otherwise/>

      </xsl:choose>

      </fo:wrapper>

    </fo:block>

  </fo:list-item-label>

</xsl:template>


<xsl:template match="list-item/label | def-item/label | term" mode="mark">
  <xsl:apply-templates/>
</xsl:template>

<!-- suppress item's label or term in no-mode, 
     because they're each called explicitly by the list-item 
     (or def-item) when making the item's mark -->
<xsl:template match="list-item/label | def-item/label"/>



</xsl:transform>