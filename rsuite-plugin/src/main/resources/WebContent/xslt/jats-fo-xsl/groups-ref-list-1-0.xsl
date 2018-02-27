<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    groups-ref-list-1-0.xsl                           -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             bibliographies (ref-list).                        -->
<!--                                                               -->
<!-- CONTAINS:   Lookup tables:                                    -->
<!--             1) map attributes to roles in English             -->
<!--             Templates for:                                    -->
<!--             1) ref-list                                       -->
<!--             2) ref-list title                                 -->
<!--             3) ref/citation                                   -->
<!--             4) ref/notes                                      -->
<!--             5) ref/nlm-citation (many kinds)                  -->
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

<xsl:transform version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
  xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" extension-element-prefixes="m">


  <!-- ============================================================= -->
  <!-- LOOKUP TABLE MAPPING ATTRIBUTES TO ENGLISH WORDS FOR ROLES    -->
  <!-- ============================================================= -->


  <!-- Lookup table for person-type strings
     used in nlm-citations -->
  <xsl:variable name="person-strings" select="document('')/*/m:map[@id='person-strings']/item"/>

  <m:map id="person-strings">
    <item source="editor" singular="editor" plural="editors"/>
    <item source="assignee" singular="assignee" plural="assignees"/>
    <item source="translator" singular="translator" plural="translators"/>
    <item source="transed" singular="translator and editor" plural="translators and editors"/>
    <item source="guest-editor" singular="guest editor" plural="guest editors"/>
    <item source="compiler" singular="compiler" plural="compilers"/>
    <item source="inventor" singular="inventor" plural="inventors"/>
    <!-- value 'allauthors' puts no string out -->
  </m:map>



  <!-- ============================================================= -->
  <!-- REF-LIST                                                      -->
  <!-- ============================================================= -->


  <!-- ref-list may occur in a variety of contexts: ack, app,
     app-group, back, boxed-text, notes, ref-list (nested),
     and sec. -->

  <!-- ref-list may contain a title, followed by the type of
     content one would find in a section (paras, lists, etc.),
     and then refs, and then optionally subordinate ref-lists. 
     Nested ref-lists are not handled in this transform. -->

  <!-- ref-list sets wide, to TextboxLMarg NOT ContentLMarg. -->


  <xsl:template match="ref-list">

    <xsl:param name="space-before" select="$leading-around-display-blocks"/>

    <!-- This block and wrapper establish the default typography
       for ref-list, then set the title and process the content. -->

    <fo:block start-indent="0pc" space-before="{$space-before}" space-before.precedence="2"
      line-height="9pt">
      <!-- SA00 redule line height for refs -->

      <fo:wrapper font-family="{$textfont}" font-size="{$textsize}pt">

        <xsl:choose>
          <xsl:when test="parent::back">
            <xsl:call-template name="ref-list-title-style-1">
              <xsl:with-param name="supplied-title" select="'References'"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="ref-list-title-style-2">
              <xsl:with-param name="supplied-title" select="'References'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>

        <fo:list-block>
          <xsl:apply-templates/>
        </fo:list-block>

      </fo:wrapper>

    </fo:block>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- REF-LIST TITLE (use what's there, or create one)              -->
  <!-- ============================================================= -->


  <!-- for use in back matter: 
     like block-title-style-1 a top-level section heading -->

  <xsl:template name="ref-list-title-style-1">

    <!-- received from ref-list template -->
    <xsl:param name="supplied-title"/>
    <!-- SA00 -->
    <!--    <fo:float axf:float-y="bottom"
      axf:float-move="keep"/>
-->

<!--    <fo:float axf:float-y="top" axf:float-move="keep" axf:float-reference="page" />
    <fo:float axf:float-y="bottom" axf:float-move="keep" axf:float-reference="page" />
    <fo:float axf:float-y="top" axf:float-move="keep" axf:float-reference="multicol" />
    <fo:float axf:float-y="bottom" axf:float-move="keep" axf:float-reference="multicol" />
    <fo:float axf:float-y="top" axf:float-move="keep" axf:float-reference="column" />
    <fo:float axf:float-y="bottom" axf:float-move="keep" axf:float-reference="column" />
-->    
    <fo:block line-height="{$textleading +2}pt" space-after="{$leading-below-titles-small}"
      space-after.precedence="force" keep-with-next.within-column="always">
      <fo:wrapper  font-family="Arial" font-size="10pt" font-weight="bold">
        <!-- SA00 References Section -->

        <!-- set title or supply one -->
        <xsl:choose>
          <xsl:when test="title">
            <xsl:value-of select="label"/>
            <xsl:text>&#x2003;</xsl:text>
            <xsl:apply-templates select="title" mode="display"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$supplied-title"/>
          </xsl:otherwise>
        </xsl:choose>

      </fo:wrapper>
    </fo:block>

  </xsl:template>


  <!-- for use in body/sec: 
     like block-title-style-2 -->

  <xsl:template name="ref-list-title-style-2">

    <!-- received from ref-list template -->
    <xsl:param name="supplied-title"/>

    <fo:block line-height="{$textleading +4}pt" space-after="{$leading-below-titles-small}"
      space-after.precedence="force" keep-with-next.within-page="always">
      <fo:wrapper font-family="{$titlefont}" font-weight="bold">

        <!-- set title or supply one -->
        <xsl:choose>
          <xsl:when test="title">
            <xsl:apply-templates select="title" mode="display"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$supplied-title"/>
          </xsl:otherwise>
        </xsl:choose>

      </fo:wrapper>
    </fo:block>

  </xsl:template>


  <xsl:template match="ref-list/title" mode="display">
    <xsl:apply-templates/>
  </xsl:template>

  <!-- suppress in no-mode -->
  <xsl:template match="ref-list/title"/>

  <xsl:template match="ref-list/label"/>
  <!-- SA00 Suppress label -->


  <!-- ============================================================= -->
  <!-- REF                                                           -->
  <!-- ============================================================= -->


  <!-- A ref may contain citation and nlm-citation elements.
     (It also may contain note elements, but these are intended
     for conversion use only and are accordingly suppressed.) -->

  <!-- DTD allows cross-reference to a ref AND to its child
     elements. -->

  <!-- Formatting assumption: A ref contains only one substantive
     child (citation OR nlm-citation), and any cross-reference
     is to the ref, not to a child of the ref. -->


  <xsl:template match="ref">

    <xsl:param name="space-after" select="'0pt'"/>

    <fo:list-item>
      <fo:list-item-label end-indent="label-end()">
        <fo:block id="{@id}" font-size="7pt" line-height="8pt">
          <xsl:text>[</xsl:text>
          <xsl:value-of select="label"/>
          <xsl:text>]</xsl:text>
        </fo:block>
      </fo:list-item-label>
      <fo:list-item-body start-indent="body-start()">
        <fo:block  font-size="7pt" line-height="8pt">
          <xsl:apply-templates/>
        </fo:block>
      </fo:list-item-body>
    </fo:list-item>
  </xsl:template>


  <xsl:template match="ref/label" priority="10"/>
  <!-- SA00 don't need duplicate ref #-->

  <!-- ============================================================= -->
  <!-- REF/CITATION                                                  -->
  <!-- ============================================================= -->


  <!-- Citation is a mixed-content bucket. We assume it contains all 
     needed punctuation, so just put out the content. Only two of 
     its contained elements have formatting consequences: these
     are given first below. -->


  <xsl:template match="ref/citation">
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="citation/source">
    <fo:wrapper font-style="italic">
      <xsl:apply-templates/>
    </fo:wrapper>
  </xsl:template>


  <!-- Annotation produces an interruptive block -->
  <xsl:template match="citation/annotation">
    <fo:block>
      <fo:inline>
        <xsl:text>Annotation: </xsl:text>
      </fo:inline>
      <!-- annotation contains paragraphs -->
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>


  <!-- All other citation elements merely produce their content: -->
  <xsl:template
    match="citation/access-date | citation/article-title
                   | citation/collab | citation/comment
                   | citation/conf-date | citation/conf-loc
                   | citation/conf-name | citation/day
                   | citation/edition | citation/email
                   | citation/elocation-id | citation/etal
                   | citation/fpage | citation/gov
                   | citation/issn | citation/isbn
                   | citation/issue | citation/lpage
                   | citation/month | citation/name
                   | citation/page-count | citation/patent
                   | citation/person-group | citation/pub-id
                   | citation/publisher-loc | citation/publisher-name
                   | citation/season | citation/series
                   | citation/source | citation/std
                   | citation/supplement | citation/time-stamp
                   | citation/trans-source | citation/trans-title
                   | citation/volume | citation/year">
    <xsl:apply-templates/>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- REF/NOTES                                                     -->
  <!-- ============================================================= -->


  <!-- suppressed because this element is meant for conversion
     use, not for publishable content.  -->

  <xsl:template match="ref/note"/>


  <!-- ============================================================= -->
  <!-- REF/NLM-CITATION                                              -->
  <!-- ============================================================= -->

  <!-- Punctuation logic is copied directly from NLM's HTML preview;
     the only changes are to a few typographic calls - HTML
     bold, italic, blockquote, and break - which have been
     rewritten as their fo equivalents. -->

  <!-- The nlm-citation model allows only element content, so
     it takes a pull template and adds punctuation. -->

  <!-- Processing of nlm-citation uses several modes, including
     citation, book, edited-book, conf, inconf, and mode "none".   -->

  <!-- Each @citation-type is handled in its own template. -->


  <!-- ============================================================= -->
  <!-- NLM-CITATION; book or thesis                                  -->
  <!-- ============================================================= -->


  <xsl:template
    match="ref/nlm-citation[@citation-type='book']
                   | ref/nlm-citation[@citation-type='thesis']">

    <xsl:variable name="augroupcount" select="count(person-group) + count(collab)"/>

    <xsl:choose>

      <xsl:when
        test="$augroupcount>1 and 
                    person-group[@person-group-type!='author'] and 
                    article-title ">
        <xsl:apply-templates select="person-group[@person-group-type='author']" mode="book"/>
        <xsl:apply-templates select="collab" mode="book"/>
        <xsl:apply-templates select="article-title" mode="editedbook"/>
        <xsl:text>In: </xsl:text>
        <xsl:apply-templates
          select="person-group[@person-group-type='editor'] 
                                 | person-group[@person-group-type='allauthors'] 
                                 | person-group[@person-group-type='translator']  
                                 | person-group[@person-group-type='transed'] "
          mode="book"/>
        <xsl:apply-templates select="source" mode="book"/>
        <xsl:apply-templates select="edition" mode="book"/>
        <xsl:apply-templates select="volume" mode="book"/>
        <xsl:apply-templates select="trans-source" mode="book"/>
        <xsl:apply-templates select="publisher-name | publisher-loc" mode="none"/>
        <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
        <xsl:apply-templates select="fpage | lpage" mode="book"/>
      </xsl:when>

      <xsl:when
        test="person-group[@person-group-type='author'] or 
                    person-group[@person-group-type='compiler']">
        <xsl:apply-templates
          select="person-group[@person-group-type='author'] 
                                 | person-group[@person-group-type='compiler']"
          mode="book"/>
        <xsl:apply-templates select="collab" mode="book"/>
        <xsl:apply-templates select="source" mode="book"/>
        <xsl:apply-templates select="edition" mode="book"/>
        <xsl:apply-templates
          select="person-group[@person-group-type='editor'] 
                                 | person-group[@person-group-type='translator']  
                                 | person-group[@person-group-type='transed'] "
          mode="book"/>
        <xsl:apply-templates select="volume" mode="book"/>
        <xsl:apply-templates select="trans-source" mode="book"/>
        <xsl:apply-templates select="publisher-name | publisher-loc" mode="none"/>
        <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
        <xsl:apply-templates select="article-title | fpage | lpage" mode="book"/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates
          select="person-group[@person-group-type='editor'] 
                                 | person-group[@person-group-type='translator']  
                                 | person-group[@person-group-type='transed'] 
                                 | person-group[@person-group-type='guest-editor']"
          mode="book"/>
        <xsl:apply-templates select="collab" mode="book"/>
        <xsl:apply-templates select="source" mode="book"/>
        <xsl:apply-templates select="edition" mode="book"/>
        <xsl:apply-templates select="volume" mode="book"/>
        <xsl:apply-templates select="trans-source" mode="book"/>
        <xsl:apply-templates select="publisher-name | publisher-loc" mode="none"/>
        <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
        <xsl:apply-templates select="article-title | fpage | lpage" mode="book"/>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:call-template name="citation-tag-ends"/>
  </xsl:template>




  <!-- ============================================================= -->
  <!-- NLM-CITATION: conference proceedings                          -->
  <!-- ============================================================= -->


  <xsl:template match="ref/mixed-citation[@publication-type='conf-paper']" priority="10">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <xsl:template match="ref/mixed-citation[@publication-type='journal']" priority="10">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <xsl:template match="ref/mixed-citation[@publication-type='book']" priority="10">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <xsl:template match="ref/mixed-citation[@publication-type='std']" priority="10">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <!-- ============================================================= -->
  <!-- NLM-CITATION: government and other reports, web, and commun   -->
  <!-- ============================================================= -->


  <!-- Government and other reports, other, web, and commun -->
  <xsl:template match="ref/mixed-citation[@publication-type]">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>
  
  <!-- ============================================================= -->
  <!-- NLM-CITATION: government and other reports, web, and commun   -->
  <!-- ============================================================= -->


  <!-- Government and other reports, other, web, and commun -->
  <xsl:template
    match="ref/nlm-citation[@citation-type='gov']
                   | ref/nlm-citation[@citation-type='web']
                   | ref/nlm-citation[@citation-type='commun']
                   | ref/nlm-citation[@citation-type='other']">

    <xsl:apply-templates select="person-group" mode="book"/>

    <xsl:apply-templates select="collab"/>

    <xsl:choose>
      <xsl:when test="publisher-loc | publisher-name">
        <xsl:apply-templates select="source" mode="book"/>
        <xsl:choose>
          <xsl:when test="@citation-type='web'">
            <xsl:apply-templates select="edition" mode="none"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates select="edition"/>
          </xsl:otherwise>
        </xsl:choose>

        <xsl:apply-templates select="publisher-loc" mode="none"/>
        <xsl:apply-templates select="publisher-name" mode="none"/>
        <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
        <xsl:apply-templates select="article-title|gov" mode="none"/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates select="article-title|gov" mode="book"/>
        <xsl:apply-templates select="source" mode="book"/>
        <xsl:apply-templates select="edition"/>
        <xsl:apply-templates select="publisher-loc" mode="none"/>
        <xsl:apply-templates select="publisher-name" mode="none"/>
        <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:apply-templates select="fpage | lpage" mode="book"/>

    <xsl:call-template name="citation-tag-ends"/>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NLM-CITATION: patents                                         -->
  <!-- ============================================================= -->


  <xsl:template match="ref/nlm-citation[@citation-type='patent']">

    <xsl:apply-templates select="person-group" mode="book"/>
    <xsl:apply-templates select="collab" mode="book"/>
    <xsl:apply-templates select="article-title | trans-title" mode="none"/>
    <xsl:apply-templates select="source" mode="none"/>
    <xsl:apply-templates select="patent" mode="none"/>
    <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
    <xsl:apply-templates select="fpage | lpage" mode="book"/>

    <xsl:call-template name="citation-tag-ends"/>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NLM-CITATION: discussion                                      -->
  <!-- ============================================================= -->


  <xsl:template match="ref/nlm-citation[@citation-type='discussion']">

    <xsl:apply-templates select="person-group" mode="book"/>
    <xsl:apply-templates select="collab"/>
    <xsl:apply-templates select="article-title" mode="editedbook"/>
    <xsl:text>In: </xsl:text>
    <xsl:apply-templates select="source" mode="none"/>

    <xsl:if test="publisher-name | publisher-loc">
      <xsl:text> [</xsl:text>
      <xsl:apply-templates select="publisher-loc" mode="none"/>
      <xsl:value-of select="publisher-name"/>
      <xsl:text>]; </xsl:text>
    </xsl:if>

    <xsl:apply-templates select="year | month | time-stamp | season | access-date" mode="book"/>
    <xsl:apply-templates select="fpage | lpage" mode="book"/>

    <xsl:call-template name="citation-tag-ends"/>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- NLM-CITATION: default                                         -->
  <!-- ============================================================= -->


  <!-- if none of the above citation-types applies,
     use mode="none"                             -->
  <!-- (e.g., citation-type="journal"              -->
  <xsl:template match="mixed-citation">

    <xsl:apply-templates
      select="*[not(self::annotation) and 
                                 not(self::edition) and 
                                 not(self::lpage) and 
                                 not(self::comment)]|text()"
      mode="none"/>

    <xsl:call-template name="citation-tag-ends"/>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- MODED TEMPLATES                                               -->
  <!-- ============================================================= -->


  <!-- ============================================================= -->
  <!-- person-group, mode="none"                                     -->
  <!-- ============================================================= -->


  <xsl:template match="person-group" mode="none">
    <xsl:variable name="gnms" select="string(descendant::given-names)"/>
    <xsl:variable name="GNMS">
      <xsl:call-template name="capitalize">
        <xsl:with-param name="str" select="$gnms"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$gnms=$GNMS">
        <xsl:apply-templates/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates select="node()" mode="book"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- person-group, mode=book                                       -->
  <!-- ============================================================= -->


  <xsl:template match="person-group" mode="book">

    <!--
  <xsl:choose>
  
    <xsl:when test="@person-group-type='editor'
                 or @person-group-type='assignee'
                 or @person-group-type='translator'
                 or @person-group-type='transed'
                 or @person-group-type='guest-editor'
                 or @person-group-type='compiler'
                 or @person-group-type='inventor'
                 or @person-group-type='allauthors'">

      <xsl:call-template name="make-persons-in-mode"/>
      <xsl:call-template name="choose-person-type-string"/>
      <xsl:call-template name="choose-person-group-end-punct"/>

    </xsl:when>
    
    <xsl:otherwise>
      <xsl:apply-templates mode="book"/>
    </xsl:otherwise>
   
  </xsl:choose>
-->

    <xsl:call-template name="make-persons-in-mode"/>
    <xsl:call-template name="choose-person-type-string"/>
    <xsl:call-template name="choose-person-group-end-punct"/>

  </xsl:template>




  <!-- ============================================================= -->
  <!-- Citation subparts, all modes together                         -->
  <!-- ============================================================= -->

  <!-- names -->

  <xsl:template match="processing-instruction('show')" mode="conf-paper">
    <xsl:if test="$type=$proof_type">
      <xsl:variable name="show" select="substring-before(substring-after(.,'ID='), ']')"/>
      <fo:block start-indent="-14pt">
        <fo:float axf:float="start">
          <fo:block color="blue" font-size="8pt">
            <fo:basic-link internal-destination="{$show}">
              <xsl:value-of select="$show"/>
            </fo:basic-link>
          </fo:block>
        </fo:float>
      </fo:block>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="comment" mode="conf-paper" priority="10">
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <xsl:template match="uri" mode="conf-paper" priority="10">
    <fo:basic-link external-destination="{@xlink:href}" color="blue">
      <xsl:apply-templates mode="conf-paper"/>
    </fo:basic-link>
  </xsl:template>


  <xsl:template match="person-group" mode="conf-paper" priority="10">
    <!-- SA00 -->
    <xsl:apply-templates mode="conf-paper"/>
  </xsl:template>

  <xsl:template match="string-name" mode="conf-paper">
    <xsl:apply-templates mode="conf-paper"/>
    <xsl:if test="following-sibling::string-name">
      <xsl:text>, </xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="surname" mode="conf-paper">
    <xsl:value-of select="normalize-space(.)"/>
    <xsl:text>, </xsl:text>
  </xsl:template>

  <xsl:template match="given-names" mode="conf-paper">
    <xsl:value-of select="normalize-space(.)"/>
  </xsl:template>

  <xsl:template match="article-title" mode="conf-paper">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="etal" mode="conf-paper">
    <fo:inline font-style="italic">
      <xsl:text>, et al.</xsl:text>
    </fo:inline>
  </xsl:template>

  <xsl:template match="source" mode="conf-paper" priority="10">
    <fo:inline font-style="italic">
      <xsl:value-of select="normalize-space(.)"/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="publisher" mode="conf-paper" priority="10">
    <xsl:value-of select="normalize-space(.)"/>
  </xsl:template>

  <xsl:template match="year" mode="conf-paper" priority="10">
    <xsl:value-of select="normalize-space(.)"/>
  </xsl:template>

  <xsl:template match="volume" mode="conf-paper" priority="10">
    <fo:inline font-weight="bold">
      <xsl:value-of select="normalize-space(.)"/>
    </fo:inline>
  </xsl:template>


  <xsl:template match="name" mode="nscitation">
    <xsl:value-of select="surname"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="given-names"/>
    <xsl:text>. </xsl:text>
  </xsl:template>


  <xsl:template match="name" mode="book">
    <xsl:variable name="nodetotal" select="count(../*)"/>
    <xsl:variable name="penult" select="count(../*)-1"/>
    <xsl:variable name="position" select="position()"/>

    <xsl:choose>

      <!-- if given-names -->
      <xsl:when test="given-names">
        <xsl:apply-templates select="surname"/>
        <xsl:text>, </xsl:text>
        <xsl:call-template name="firstnames">
          <xsl:with-param name="nodetotal" select="$nodetotal"/>
          <xsl:with-param name="position" select="$position"/>
          <xsl:with-param name="names" select="given-names"/>
          <xsl:with-param name="pgtype">
            <xsl:choose>
              <xsl:when test="parent::person-group[@person-group-type]">
                <xsl:value-of select="parent::person-group/@person-group-type"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="'author'"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:with-param>
        </xsl:call-template>

        <xsl:if test="suffix">
          <xsl:text>, </xsl:text>
          <xsl:apply-templates select="suffix"/>
        </xsl:if>
      </xsl:when>

      <!-- if no given-names -->
      <xsl:otherwise>
        <xsl:apply-templates select="surname"/>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:choose>
      <!-- if have aff -->
      <xsl:when test="following-sibling::aff"/>

      <!-- if don't have aff -->
      <xsl:otherwise>
        <xsl:choose>

          <!-- if part of person-group -->
          <xsl:when test="parent::person-group/@person-group-type">
            <xsl:choose>

              <!-- if author -->
              <xsl:when test="parent::person-group/@person-group-type='author'">
                <xsl:choose>
                  <xsl:when test="$nodetotal=$position">. </xsl:when>
                  <xsl:when test="$penult=$position">
                    <xsl:choose>
                      <xsl:when test="following-sibling::etal">, </xsl:when>
                      <xsl:otherwise>; </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>; </xsl:otherwise>
                </xsl:choose>
              </xsl:when>

              <!-- if not author -->
              <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="$nodetotal=$position"/>
                  <xsl:when test="$penult=$position">
                    <xsl:choose>
                      <xsl:when test="following-sibling::etal">, </xsl:when>
                      <xsl:otherwise>; </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>; </xsl:otherwise>
                </xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>

          <!-- if not part of person-group -->
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$nodetotal=$position">. </xsl:when>
              <xsl:when test="$penult=$position">
                <xsl:choose>
                  <xsl:when test="following-sibling::etal">, </xsl:when>
                  <xsl:otherwise>; </xsl:otherwise>
                </xsl:choose>
              </xsl:when>
              <xsl:otherwise>; </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>

    </xsl:choose>
  </xsl:template>


  <xsl:template match="collab" mode="book">
    <xsl:apply-templates/>
    <xsl:if test="@collab-type='compilers'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:if test="@collab-type='assignee'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="collab" mode="none">
    <xsl:apply-templates/>
    <xsl:if test="@collab-type">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>

    <xsl:choose>
      <xsl:when test="following-sibling::collab">
        <xsl:text>; </xsl:text>
      </xsl:when>

      <xsl:otherwise>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template match="etal" mode="book">
    <xsl:text>et al.</xsl:text>
    <xsl:choose>
      <xsl:when test="parent::person-group/@person-group-type">
        <xsl:choose>
          <xsl:when test="parent::person-group/@person-group-type='author'">
            <xsl:text> </xsl:text>
          </xsl:when>
          <xsl:otherwise/>
        </xsl:choose>
      </xsl:when>

      <xsl:otherwise>
        <xsl:text> </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- affiliations -->

  <xsl:template match="aff" mode="book">
    <xsl:variable name="nodetotal" select="count(../*)"/>
    <xsl:variable name="position" select="position()"/>

    <xsl:text> (</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>)</xsl:text>

    <xsl:choose>
      <xsl:when test="$nodetotal=$position">. </xsl:when>
      <xsl:otherwise>, </xsl:otherwise>
    </xsl:choose>
  </xsl:template>



  <!-- publication info -->

  <xsl:template match="article-title" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="article-title" mode="book">
    <xsl:apply-templates/>

    <xsl:choose>
      <xsl:when test="../fpage or ../lpage">
        <xsl:text>; </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="article-title" mode="editedbook">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="article-title" mode="conf">
    <xsl:apply-templates/>
    <xsl:choose>
      <xsl:when test="../conf-name">
        <xsl:text>. </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>; </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="article-title" mode="inconf">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="article-title" mode="none">
    <xsl:choose>
      <xsl:when test="../trans-title">
        <xsl:apply-templates/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates/>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>



  <xsl:template match="source" mode="nscitation">
    <fo:wrapper font-style="italic">
      <xsl:apply-templates/>
    </fo:wrapper>
  </xsl:template>

  <xsl:template match="source" mode="book">
    <xsl:choose>

      <xsl:when test="../trans-source">
        <xsl:apply-templates/>
        <xsl:choose>
          <xsl:when test="../volume | ../edition">
            <xsl:text>. </xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text> </xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates/>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="source" mode="conf">
    <xsl:apply-templates/>
    <xsl:text>; </xsl:text>
  </xsl:template>

  <xsl:template match="source" mode="none">
    <xsl:apply-templates/>

    <xsl:choose>
      <xsl:when test="../access-date">
        <xsl:if test="../edition">
          <xsl:text> (</xsl:text>
          <xsl:apply-templates select="../edition" mode="plain"/>
          <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>. </xsl:text>
      </xsl:when>

      <xsl:when test="../volume | ../fpage">
        <xsl:if test="../edition">
          <xsl:text> (</xsl:text>
          <xsl:apply-templates select="../edition" mode="plain"/>
          <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text> </xsl:text>
      </xsl:when>

      <xsl:otherwise>
        <xsl:if test="../edition">
          <xsl:text> (</xsl:text>
          <xsl:apply-templates select="../edition" mode="plain"/>
          <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template match="trans-source" mode="book">
    <xsl:text> [</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>]. </xsl:text>
  </xsl:template>

  <xsl:template match="trans-title" mode="none">
    <xsl:text> [</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>]. </xsl:text>
  </xsl:template>

  <xsl:template match="trans-title" mode="article-meta">
    <fo:block>
      <fo:wrapper font-weight="bold">
        <xsl:text>[Translated title: </xsl:text>
      </fo:wrapper>
      <xsl:apply-templates/>
      <fo:wrapper font-weight="bold">
        <xsl:text>]</xsl:text>
      </fo:wrapper>
    </fo:block>
  </xsl:template>


  <!-- alt-title occurs only in article-meta/title-group 
     but we've put it in article-meta mode to keep it
     with the other article-meta parts -->
  <xsl:template match="alt-title" mode="article-meta">
    <fo:block>
      <fo:wrapper font-weight="bold">
        <xsl:text>[Alternate title: </xsl:text>
      </fo:wrapper>
      <xsl:apply-templates/>
      <fo:wrapper font-weight="bold">
        <xsl:text>]</xsl:text>
      </fo:wrapper>
    </fo:block>
  </xsl:template>


  <xsl:template match="volume" mode="nscitation">
    <xsl:text> </xsl:text>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="volume | edition" mode="book">
    <xsl:apply-templates/>
    <xsl:if test="@collab-type='compilers'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:if test="@collab-type='assignee'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="volume" mode="none">
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="edition" mode="none">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="supplement" mode="none">
    <xsl:text> </xsl:text>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="issue" mode="none">
    <xsl:text>(</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>)</xsl:text>
  </xsl:template>

  <xsl:template match="publisher-loc" mode="none">
    <xsl:apply-templates/>
    <xsl:text>: </xsl:text>
  </xsl:template>

  <xsl:template match="publisher-name" mode="none">
    <xsl:apply-templates/>
    <xsl:text>; </xsl:text>
  </xsl:template>



  <!-- dates -->

  <xsl:template match="month" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:text>.</xsl:text>
  </xsl:template>

  <xsl:template match="month" mode="book">
    <xsl:variable name="month" select="."/>
    <xsl:choose>
      <xsl:when test="$month='01' or $month='1' or $month='January'">Jan</xsl:when>
      <xsl:when test="$month='02' or $month='2' or $month='February'">Feb</xsl:when>
      <xsl:when test="$month='03' or $month='3' or $month='March'">Mar</xsl:when>
      <xsl:when test="$month='04' or $month='4' or $month='April'">Apr</xsl:when>
      <xsl:when test="$month='05' or $month='5' or $month='May'">May</xsl:when>
      <xsl:when test="$month='06' or $month='6' or $month='June'">Jun</xsl:when>
      <xsl:when test="$month='07' or $month='7' or $month='July'">Jul</xsl:when>
      <xsl:when test="$month='08' or $month='8' or $month='August'">Aug</xsl:when>
      <xsl:when test="$month='09' or $month='9' or $month='September'">Sep</xsl:when>
      <xsl:when test="$month='10' or $month='October'">Oct</xsl:when>
      <xsl:when test="$month='11' or $month='November'">Nov</xsl:when>
      <xsl:when test="$month='12' or $month='December'">Dec</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$month"/>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:if test="../day">
      <xsl:text> </xsl:text>
      <xsl:value-of select="../day"/>
    </xsl:if>

    <xsl:choose>
      <xsl:when test="../time-stamp">
        <xsl:text>, </xsl:text>
        <xsl:value-of select="../time-stamp"/>
        <xsl:text> </xsl:text>
      </xsl:when>
      <xsl:when test="../access-date"/>
      <xsl:otherwise>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="month" mode="none">
    <xsl:variable name="month" select="."/>
    <xsl:choose>
      <xsl:when test="$month='01' or $month='1' ">Jan</xsl:when>
      <xsl:when test="$month='02' or $month='2' ">Feb</xsl:when>
      <xsl:when test="$month='03' or $month='3' ">Mar</xsl:when>
      <xsl:when test="$month='04' or $month='4' ">Apr</xsl:when>
      <xsl:when test="$month='05' or $month='5' ">May</xsl:when>
      <xsl:when test="$month='06' or $month='6'">Jun</xsl:when>
      <xsl:when test="$month='07' or $month='7'">Jul</xsl:when>
      <xsl:when test="$month='08' or $month='8' ">Aug</xsl:when>
      <xsl:when test="$month='09' or $month='9' ">Sep</xsl:when>
      <xsl:when test="$month='10' ">Oct</xsl:when>
      <xsl:when test="$month='11' ">Nov</xsl:when>
      <xsl:when test="$month='12' ">Dec</xsl:when>

      <xsl:otherwise>
        <xsl:value-of select="$month"/>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:if test="../day">
      <xsl:text> </xsl:text>
      <xsl:value-of select="../day"/>
    </xsl:if>

    <xsl:text>;</xsl:text>

  </xsl:template>



  <xsl:template match="day" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="day" mode="none"/>


  <xsl:template match="year" mode="nscitation">
    <xsl:text> </xsl:text>
    <xsl:apply-templates/>
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="year" mode="book">
    <xsl:choose>
      <xsl:when test="../month or ../season or ../access-date">
        <xsl:apply-templates/>
        <xsl:text> </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="year" mode="none">
    <xsl:choose>
      <xsl:when test="../month or ../season or ../access-date">
        <xsl:apply-templates mode="none"/>
        <xsl:text> </xsl:text>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates mode="none"/>
        <xsl:if test="../volume or ../issue">
          <xsl:text>;</xsl:text>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template match="time-stamp" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="time-stamp" mode="book"/>


  <xsl:template match="access-date" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="access-date" mode="book">
    <xsl:text> [</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>]. </xsl:text>
  </xsl:template>

  <xsl:template match="access-date" mode="none">
    <xsl:text> [</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>];</xsl:text>
  </xsl:template>



  <xsl:template match="season" mode="book">
    <xsl:apply-templates/>
    <xsl:if test="@collab-type='compilers'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:if test="@collab-type='assignee'">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="@collab-type"/>
    </xsl:if>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="season" mode="none">
    <xsl:apply-templates/>
    <xsl:text>;</xsl:text>
  </xsl:template>


  <!-- pages -->

  <xsl:template match="fpage" mode="nscitation">
    <xsl:apply-templates/>
    <xsl:if test="../lpage">
      <xsl:text>-</xsl:text>
      <xsl:value-of select="../lpage"/>
    </xsl:if>
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="fpage" mode="none">
    <xsl:variable name="fpgct" select="count(../fpage)"/>
    <xsl:variable name="lpgct" select="count(../lpage)"/>
    <xsl:variable name="hermano" select="name(following-sibling::node())"/>

    <xsl:choose>

      <xsl:when test="preceding-sibling::fpage">
        <xsl:choose>
          <xsl:when test="following-sibling::fpage">
            <xsl:text> </xsl:text>
            <xsl:apply-templates/>

            <xsl:if test="$hermano='lpage'">
              <xsl:text>&#8211;</xsl:text>
              <xsl:apply-templates select="following-sibling::lpage[1]" mode="none"/>
            </xsl:if>
            <xsl:text>,</xsl:text>
          </xsl:when>

          <xsl:otherwise>
            <xsl:text> </xsl:text>
            <xsl:apply-templates/>

            <xsl:if test="$hermano='lpage'">
              <xsl:text>&#8211;</xsl:text>
              <xsl:apply-templates select="following-sibling::lpage[1]" mode="none"/>
            </xsl:if>
            <xsl:text>.</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <xsl:otherwise>
        <xsl:text>:</xsl:text>
        <xsl:apply-templates/>

        <xsl:choose>
          <xsl:when test="$hermano='lpage'">
            <xsl:text>&#8211;</xsl:text>
            <xsl:apply-templates select="following-sibling::lpage[1]" mode="none"/>
            <xsl:text>.</xsl:text>
          </xsl:when>

          <xsl:when test="$hermano='fpage'">
            <xsl:text>,</xsl:text>
          </xsl:when>

          <xsl:otherwise>
            <xsl:text>.</xsl:text>
          </xsl:otherwise>
        </xsl:choose>

      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>

  <xsl:template match="fpage" mode="book">
    <xsl:text>p. </xsl:text>
    <xsl:apply-templates/>

    <xsl:if test="../lpage">
      <xsl:text>.</xsl:text>
    </xsl:if>

  </xsl:template>


  <xsl:template match="lpage" mode="book">
    <xsl:choose>
      <xsl:when test="../fpage">
        <xsl:text>-</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>.</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
        <xsl:text> p.</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="lpage" mode="nscitation"/>

  <xsl:template match="lpage" mode="none">
    <xsl:apply-templates/>
  </xsl:template>


  <!-- misc stuff -->

  <xsl:template match="pub-id[@pub-id-type='pmid']" mode="nscitation">
    <xsl:variable name="pmid" select="."/>
    <xsl:variable name="href"
      select="'http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&amp;db=PubMed&amp;dopt=abstract&amp;list_uids='"/>
    <xsl:text> [</xsl:text>
    <a>
      <xsl:attribute name="href">
        <xsl:value-of select="concat($href,$pmid)"/>
      </xsl:attribute>
      <xsl:attribute name="target">
        <xsl:text>_new</xsl:text>
      </xsl:attribute>PubMed </a>
    <xsl:text>]</xsl:text>
  </xsl:template>

  <xsl:template match="annotation" mode="nscitation">
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template match="comment" mode="nscitation">
    <xsl:if test="not(self::node()='.')">
      <fo:block>
        <fo:wrapper text-transform="uppercase" font-size="{$textsize -2}pt">
          <xsl:apply-templates/>
        </fo:wrapper>
      </fo:block>
    </xsl:if>
  </xsl:template>

  <xsl:template match="conf-name | conf-date" mode="conf">
    <xsl:apply-templates/>
    <xsl:text>; </xsl:text>
  </xsl:template>

  <xsl:template match="conf-loc" mode="conf">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>

  <xsl:template match="gov" mode="none">
    <xsl:choose>
      <xsl:when test="../trans-title">
        <xsl:apply-templates/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:apply-templates/>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="patent" mode="none">
    <xsl:apply-templates/>
    <xsl:text>. </xsl:text>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: CITATION-TAG-ENDS                             -->
  <!-- ============================================================= -->


  <xsl:template name="citation-tag-ends">

    <xsl:apply-templates select="series" mode="citation"/>

    <!-- If language is not English -->
    <!-- XX review logic -->
    <xsl:if test="article-title[@xml:lang!='en'] 
               or article-title[@xml:lang!='EN']">

      <xsl:call-template name="language">
        <xsl:with-param name="lang" select="article-title/@xml:lang"/>
      </xsl:call-template>
    </xsl:if>

    <xsl:if test="source[@xml:lang!='en'] 
              or source[@xml:lang!='EN']">

      <xsl:call-template name="language">
        <xsl:with-param name="lang" select="source/@xml:lang"/>
      </xsl:call-template>
    </xsl:if>

    <xsl:apply-templates select="comment" mode="citation"/>

    <xsl:apply-templates select="annotation" mode="citation"/>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: "firstnames"                                  -->
  <!-- ============================================================= -->


  <!-- called by match="name" in book mode,
     as part of citation handling
     when given-names is not all-caps -->

  <xsl:template name="firstnames">
    <xsl:param name="nodetotal"/>
    <xsl:param name="position"/>
    <xsl:param name="names"/>
    <xsl:param name="pgtype"/>

    <xsl:variable name="length" select="string-length($names)-1"/>
    <xsl:variable name="gnm" select="substring($names,$length,2)"/>
    <xsl:variable name="GNM">
      <xsl:call-template name="capitalize">
        <xsl:with-param name="str" select="substring($names,$length,2)"/>
      </xsl:call-template>
    </xsl:variable>

    <!--
<xsl:text>Value of $names = [</xsl:text><xsl:value-of select="$names"/><xsl:text>]</xsl:text>
<xsl:text>Value of $length = [</xsl:text><xsl:value-of select="$length"/><xsl:text>]</xsl:text>
<xsl:text>Value of $gnm = [</xsl:text><xsl:value-of select="$gnm"/><xsl:text>]</xsl:text>
<xsl:text>Value of $GNM = [</xsl:text><xsl:value-of select="$GNM"/><xsl:text>]</xsl:text>
-->

    <xsl:if test="$names">
      <xsl:choose>

        <xsl:when test="$gnm=$GNM">
          <xsl:apply-templates select="$names"/>
          <xsl:choose>
            <xsl:when test="$nodetotal!=$position">
              <xsl:text>.</xsl:text>
            </xsl:when>
            <xsl:when test="$pgtype!='author'">
              <xsl:text>.</xsl:text>
            </xsl:when>
          </xsl:choose>
        </xsl:when>

        <xsl:otherwise>
          <xsl:apply-templates select="$names"/>
        </xsl:otherwise>

      </xsl:choose>
    </xsl:if>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: MAKE-PERSONS-IN-MODE                          -->
  <!-- ============================================================= -->


  <!-- if given names aren't all-caps, use book mode -->

  <xsl:template name="make-persons-in-mode">

    <xsl:variable name="gnms" select="string(descendant::given-names)"/>

    <xsl:variable name="GNMS"
      select="translate($gnms, 
      'abcdefghjiklmnopqrstuvwxyz',
      'ABCDEFGHJIKLMNOPQRSTUVWXYZ')"/>

    <xsl:choose>
      <xsl:when test="$gnms=$GNMS">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates mode="book"/>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: CHOOSE-PERSON-TYPE-STRING                     -->
  <!-- ============================================================= -->


  <xsl:template name="choose-person-type-string">

    <xsl:variable name="person-group-type">
      <xsl:value-of select="@person-group-type"/>
    </xsl:variable>

    <xsl:choose>
      <!-- allauthors is an exception to the usual choice pattern -->
      <xsl:when test="$person-group-type='allauthors'"/>

      <!-- the usual choice pattern: singular or plural? -->
      <xsl:when test="count(name) > 1 or etal ">
        <xsl:text>, </xsl:text>
        <xsl:value-of select="($person-strings[@source=$person-group-type]/@plural)"/>
      </xsl:when>

      <xsl:otherwise>
        <xsl:text>, </xsl:text>
        <xsl:value-of select="($person-strings[@source=$person-group-type]/@singular)"/>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: CHOOSE-PERSON-GROUP-END-PUNCT                 -->
  <!-- ============================================================= -->


  <xsl:template name="choose-person-group-end-punct">

    <xsl:choose>
      <!-- compiler is an exception to the usual choice pattern -->
      <xsl:when test="@person-group-type='compiler'">
        <xsl:text>. </xsl:text>
      </xsl:when>

      <!-- the usual choice pattern: semi-colon or period? -->
      <xsl:when test="following-sibling::person-group">
        <xsl:text>; </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>. </xsl:text>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: CAPITALIZE                                    -->
  <!-- ============================================================= -->


  <xsl:template name="capitalize">
    <xsl:param name="str"/>
    <xsl:value-of
      select="translate($str, 
                          'abcdefghjiklmnopqrstuvwxyz',
                          'ABCDEFGHJIKLMNOPQRSTUVWXYZ')"
    />
  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: LANGUAGE                                      -->
  <!-- ============================================================= -->


  <xsl:template name="language">
    <xsl:param name="lang"/>
    <xsl:choose>
      <xsl:when test="$lang='fr' or $lang='FR'"> (Fre).</xsl:when>
      <xsl:when test="$lang='jp' or $lang='JP'"> (Jpn).</xsl:when>
      <xsl:when test="$lang='ru' or $lang='RU'"> (Rus).</xsl:when>
      <xsl:when test="$lang='de' or $lang='DE'"> (Ger).</xsl:when>
      <xsl:when test="$lang='se' or $lang='SE'"> (Swe).</xsl:when>
      <xsl:when test="$lang='it' or $lang='IT'"> (Ita).</xsl:when>
      <xsl:when test="$lang='he' or $lang='HE'"> (Heb).</xsl:when>
      <xsl:when test="$lang='sp' or $lang='SP'"> (Spa).</xsl:when>
    </xsl:choose>
  </xsl:template>


</xsl:transform>
