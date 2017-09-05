<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    set-cover-page-1-0.xsl                            -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             the cover page of the formatted article.          -->
<!--                                                               -->
<!-- CONTAINS:   1) Named template: set-article-cover-page         -->
<!--             2) article title and subtitles                    -->
<!--             3) journal-meta/notes                             -->
<!--             4) article categories                             -->
<!--             5) related articles                               -->
<!--             6) keywords                                       -->
<!--             7) conferences                                    -->
<!--             8) Named template: make-journal-metadata-table    -->
<!--             9) Named template: make-article-metadata-table    -->
<!--            10) Named template: make-row                       -->
<!--            11) pub-date and date                              -->
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
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >


<xsl:param name="cover-sec1-title-size"   select="10"/> <!-- pt -->
<xsl:param name="cover-cell-heading-size" select="11"/> <!-- pt -->
<xsl:param name="cover-cell-content-size" select="11"/> <!-- pt -->
<xsl:param name="cover-cell-line-height"  select="13"/> <!-- pt -->

<!-- ============================================================= -->
<!-- NAMED TEMPLATE: SET-ARTICLE-COVER-PAGE (NO MODE)              -->
<!-- ============================================================= -->

     
<!-- table width measurements: page 8.5" wide
     1.25" left margin
     2.75" first block
     0.50" gutter
     2.75" second block  (begins at absolute 4.5" from Page LMarg)
     1.25" right margin
--> 
     
<xsl:template name="set-article-cover-page">

  <!-- the article title and subtitle -->
  
  <!-- navigate to the right place -->
  <xsl:for-each select="/article/front/article-meta/title-group">
  
    <fo:block margin-left="0in"
              start-indent="0pc"
              space-after="12pt">
      <xsl:apply-templates select="article-title" mode="cover-page"/>
      <xsl:apply-templates select="subtitle"/>
    </fo:block>
    
  </xsl:for-each>
            
  
  
  <!-- the two metadata tables -->
  <fo:block margin-left="0pt"
            start-indent="0pc"
            space-after="24pt">
            
    <xsl:call-template name="make-journal-metadata-table"/>
  
  </fo:block>


  <!-- the rest of the cover-page metadata, set like sections w/paragraphs -->
  <fo:block space-before="0pt"
            space-after="0pt"
            start-indent="0pc">
    <!-- This wrapper establishes the default text setting,
         which is at the LMarg of the full text area NOT
         the body content area.-->
    <fo:wrapper
            font-family="{$textfont}"
            font-size="{$textsize}pt"
            line-height="{$textleading}pt"
            font-style="normal"
            font-weight="normal"
            text-align="start">
       <xsl:apply-templates select="/article/front/journal-meta/notes
                                  | /article/front/article-meta/article-categories
                                  | /article/front/article-meta/related-article
                                  | /article/front/article-meta/kwd-group
                                  | /article/front//conference"/>
     </fo:wrapper>
  </fo:block>
  
</xsl:template>


<!-- ============================================================= -->
<!-- ARTICLE TITLE AND SUBTITLE(S)                                 -->
<!-- ============================================================= -->

<xsl:template match="/article/front/article-meta/title-group/article-title" mode="cover-page">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="following-sibling::subtitle">6pt</xsl:when>
      <xsl:otherwise>0pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-after="{$space-after}"
            line-height="17pt">
  
    <fo:wrapper font-family="{$titlefont}"
                font-size="14pt"
                font-weight="bold">
      <xsl:apply-templates/>
    </fo:wrapper>
    
  </fo:block>

</xsl:template>

<xsl:template match="/article/front/article-meta/title-group/subtitle">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="following-sibling::subtitle">6pt</xsl:when>
      <xsl:otherwise>0pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-after="{$space-after}">
  
    <fo:wrapper font-family="{$titlefont}"
                font-size="{$cover-sec1-title-size}pt"
                line-height="15pt"
                font-weight="bold">
      <xsl:apply-templates/>
    </fo:wrapper>
    
  </fo:block>


</xsl:template>

                   

<!-- ============================================================= -->
<!-- JOURNAL-META/NOTES                                            -->
<!-- ============================================================= -->

<!-- journal-meta/notes/title is like block-title-style-2 
     but may not be explicit in source, and adds a phrase -->
     
<xsl:template match="journal-meta/notes">

  <fo:block space-before="0pt"
            space-after="12pt">
  
    <!-- an internal block for the title, even if
         the source doesn't have one -->
    <fo:block start-indent="0pc"
              space-after="4pt"
              keep-with-next.within-page="always">
      <fo:wrapper
              font-family="{$titlefont}"
              font-size="{$cover-sec1-title-size}pt"
        font-weight="bold">
        
        <xsl:text>Notes</xsl:text>
        
        <xsl:for-each select="title">
          <xsl:text>: </xsl:text>
          <xsl:apply-templates mode="meta"/>
        </xsl:for-each>
        
      </fo:wrapper>
    </fo:block>
    
    <!-- content of the notes -->
    <xsl:apply-templates/>
    
  </fo:block> <!-- end of the notes -->

</xsl:template>


<xsl:template match="journal-meta/notes/title" mode="meta">
  <xsl:apply-templates/>
</xsl:template>

<!-- suppress in no-mode -->
<xsl:template match="journal-meta/notes/title"/>


<xsl:template match="journal-meta/notes//p">

  <xsl:param name="space-after">
     <xsl:choose>
       <xsl:when test="following-sibling::*">6pt</xsl:when>
       <xsl:otherwise>0pt</xsl:otherwise>
     </xsl:choose>
  </xsl:param>

  <fo:block space-after="{$space-after}">

    <!-- get the runin title if appropriate.
         all will be runin-title-style-2 -->
    <xsl:if test="parent::sec and not(preceding-sibling::p)">
      <xsl:call-template name="runin-title-style-2"/>   
    </xsl:if>
    
    <!-- now the paragraph's content -->
    <xsl:apply-templates/>
  </fo:block>
  
</xsl:template>


<!-- ============================================================= -->
<!-- ARTICLE CATEGORIES                                            -->
<!-- ============================================================= -->

<!-- contains subj-groups, followed by 
     series-title* and then series-text* -->
     
<xsl:template match="article-meta/article-categories">

  <fo:block space-before="0pt"
            space-after="12pt">
            
  <!-- an internal block for the generated heading. 
       Is like block-title-style-2 -->

    <fo:block start-indent="0pc"
              space-after="4pt"
              keep-with-next.within-page="always">
      <fo:wrapper
              font-family="{$titlefont}"
              font-size="{$cover-sec1-title-size}pt"
        font-weight="bold">
        
        <xsl:text>Article Categories</xsl:text>
                
      </fo:wrapper>
    </fo:block>
    
    <!-- content of the categories -->
    <xsl:apply-templates select="subj-group"/>
    
    <xsl:if test="series-title | series-text">
    
      <fo:block space-after="0pt">
        <xsl:choose>
          <xsl:when test="series-title and series-text">
            <xsl:apply-templates select="series-title" mode="meta"/>
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="series-text" mode="meta"/>
          </xsl:when>
          <xsl:when test="series-title | series-text">
            <xsl:apply-templates select="series-title | series-text" mode="meta"/>
          </xsl:when>
          <xsl:otherwise/>
        </xsl:choose>
      </fo:block>
    </xsl:if>

  </fo:block>

</xsl:template>


<xsl:template match="article-categories/subj-group">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="following-sibling::*">6pt</xsl:when>
      <xsl:otherwise>0pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-after="{$space-after}">
  
    <xsl:if test="@subj-group-type">
      <xsl:value-of select="@subj-group-type"/>
      <xsl:text>: </xsl:text>
    </xsl:if>
  
    <xsl:for-each select="subject">
      <xsl:choose>
        <xsl:when test="preceding-sibling::subject">
          <xsl:text>: </xsl:text>
        </xsl:when>
        <xsl:otherwise/>
      </xsl:choose>
      <xsl:apply-templates/>
    </xsl:for-each>
        
  </fo:block>
  
</xsl:template>



<xsl:template match="article-categories/series-title" mode="meta">
  
    <xsl:text>Series: </xsl:text>
    <xsl:apply-templates/>

</xsl:template>

<xsl:template match="article-categories/series-text" mode="meta">

    <xsl:text> (</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>)</xsl:text>

</xsl:template>

<!-- suppress in no-mode because is called in meta mode -->
<xsl:template match="article-categories/series-title
                   | article-categories/series-text"/>
         
         
<!-- ============================================================= -->
<!-- RELATED ARTICLES                                              -->
<!-- ============================================================= -->

<!-- A related article is like a para -->
<!-- The first puts out its section title -->

<xsl:template match="article-meta/related-article">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="not(following-sibling::related-article)">12pt</xsl:when>
      <xsl:otherwise>6pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-before="0pt"
            space-after="{$space-after}">
  
    <!-- if the first one, put out an internal block
         for the group title. It's like a block-title-style-2 -->

    <xsl:if test="not(preceding-sibling::related-article)">
      <fo:block start-indent="0pc"
                space-after="4pt"
                keep-with-next.within-page="always">
        <fo:wrapper
                font-family="{$titlefont}"
                font-size="{$cover-sec1-title-size}pt"
          font-weight="bold">
        
          <xsl:text>Related Article</xsl:text>
        
          <!-- if more than one, add an "s" -->
          <xsl:if test="following-sibling::related-article">
            <xsl:text>s</xsl:text>
          </xsl:if>
                
        </fo:wrapper>
      </fo:block>
    </xsl:if> <!-- end of handling the title -->
  
    <!-- content of the related-article -->
    <xsl:apply-templates/>
    
    <!-- Now tack the attributes onto the end of the "para" -->
    
    <!-- @related-article-type is required -->
    <xsl:text> (Related Article: </xsl:text>
    <xsl:value-of select="@related-article-type"/>
    
    <!-- the others are optional, with punctuation -->
    <xsl:if test="@vol or @page">
      <xsl:text>,</xsl:text>
      <xsl:if test="@vol">
        <xsl:text> Volume: </xsl:text>
        <xsl:value-of select="@vol"/>
      </xsl:if>
      <xsl:if test="@page">
        <xsl:text> Page: </xsl:text>
        <xsl:value-of select="@page"/>
      </xsl:if>
    </xsl:if>
    
    <!-- close the attribute-values paren -->
    <xsl:text>)</xsl:text>
    
  </fo:block>

</xsl:template>


<!-- The most likely content of a related-article is 
     an article-title, which just passes its content
     through. -->
     
<xsl:template match="related-article/article-title">
  <xsl:apply-templates/>
</xsl:template>


<!-- ============================================================= -->
<!-- KEYWORD-GROUP                                                 -->
<!-- ============================================================= -->


<!-- a kwd-group is like a para -->
<!-- the kwd-group puts out the section title -->

<xsl:template match="kwd-group">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="following-sibling::*">12pt</xsl:when>
      <xsl:otherwise>0pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-before="0pt"
            space-after="{$space-after}">
  
    <!-- put out an internal block
         for the group title. It's like a block-title-style-2 -->

      <fo:block start-indent="0pc"
                space-after="4pt"
                keep-with-next.within-page="always">
        <fo:wrapper
                font-family="{$titlefont}"
                font-size="{$cover-sec1-title-size}pt"
          font-weight="bold">
        
          <xsl:text>Keywords</xsl:text>
                
        </fo:wrapper>
      </fo:block>
  
    <!-- now a block for content of the kwd's -->
    <fo:block space-before="0pt"
              space-after="0pt">
      <xsl:apply-templates/>
    </fo:block>
    
  </fo:block>
</xsl:template>


<xsl:template match="kwd">

  <xsl:choose>
    <xsl:when test="preceding-sibling::kwd">
      <xsl:text>, </xsl:text>
    </xsl:when>
    <xsl:otherwise/>
  </xsl:choose>
  
  <xsl:apply-templates/>
  
</xsl:template>


<!-- ============================================================= -->
<!-- CONFERENCES                                                   -->
<!-- ============================================================= -->

<!-- A conference is like a para.
     The first one puts out the section title. -->
     
<xsl:template match="article-meta/conference">

  <xsl:param name="space-after">
    <xsl:choose>
      <xsl:when test="not(following-sibling::conference)">12pt</xsl:when>
      <xsl:otherwise>6pt</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  
  <fo:block space-before="0pt"
            space-after="{$space-after}">
  
    <!-- if the first one, put out an internal block
         for the group title. It's like a block-title-style-2 -->

    <xsl:if test="not(preceding-sibling::conference)">
      <fo:block start-indent="0pc"
                space-after="4pt"
                keep-with-next.within-page="always">
        <fo:wrapper
                font-family="{$titlefont}"
                font-size="{$cover-sec1-title-size}pt"
          font-weight="bold">
        
          <xsl:text>Conference</xsl:text>
        
          <!-- if more than one, add an "s" -->
          <xsl:if test="following-sibling::conference">
            <xsl:text>s</xsl:text>
          </xsl:if>
                
        </fo:wrapper>
      </fo:block>
    </xsl:if> <!-- end of handling the title -->
  
    <!-- now a block for the contained elements -->
    <fo:block space-before="0pt"
              space-after="0pt">
      <xsl:apply-templates/>
    </fo:block>
    
  </fo:block>

</xsl:template>


<xsl:template match="conference/*">

  <xsl:if test="preceding-sibling::*">
    <xsl:text> </xsl:text>
  </xsl:if>
  
  <xsl:apply-templates/>
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-JOURNAL-METADATA-TABLE                   -->
<!-- ============================================================= -->


<!-- These two templates make extensive use of the 
     named template make-row. -->


<xsl:template name="make-journal-metadata-table">

  <!-- navigate to the relevant part of the source tree -->
  <xsl:for-each select="/article/front/journal-meta">

    <!-- start the table -->
    <fo:table border-style="solid"
              border-width="0.5pt"
              width="5.0in">
      <fo:table-body>
    
      <!-- set the column heading row 
           (don't bother with proper table elements for this: 
           it's just a row -->
      <xsl:call-template name="make-row">
        <xsl:with-param name="cell-heading" select="'Journal Information'"/>
      </xsl:call-template>
      
      <!-- and set each of its single-cell rows -->
    
      <!-- first: journal abbrev-titles or journal-title -->
      
      <xsl:for-each select="abbrev-journal-title">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Abbrev Journal Title: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>

      <xsl:if test="not(abbrev-journal-title) and journal-title">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Journal Title: '"/>
          <xsl:with-param name="which-node"   select="journal-title"/>
        </xsl:call-template>
      </xsl:if>
      
      <!-- next: each issn -->
      <xsl:for-each select="issn">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'ISSN: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
      <!-- next: if a publisher-name -->
      <xsl:for-each select="publisher/publisher-name">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Publisher: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
      <!-- finally: if a publisher-loc -->
      <xsl:for-each select="publisher/publisher-loc">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Location: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
    </fo:table-body>
  </fo:table>
  
  <!-- end of using the journal-meta part of the tree -->
  </xsl:for-each>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-ARTICLE-METADATA-TABLE                   -->
<!-- ============================================================= -->


<xsl:template name="make-article-metadata-table">

  <!-- navigate to the appropriate part of the tree -->
  <xsl:for-each select="/article/front/article-meta">
  
    <!-- start the table -->
    <fo:table border-style="none"
              width="2.75in">
      <fo:table-body>
    
        <!-- set the column heading row 
             (don't bother with proper table elements for the header row,
             treat it just as any other row -->
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Article/Issue Information'"/>
        </xsl:call-template>
      
      <!-- and set each of its single-cell rows -->
      
      <!-- first, copyright -->
      
      <xsl:for-each select="copyright-statement">
        <xsl:call-template name="make-row">
          <xsl:with-param name="which-node" select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
      <xsl:if test="not(copyright-statement) and copyright-year">
        <xsl:call-template name="make-row">
          <xsl:with-param name="which-node" select="copyright-year"/>
        </xsl:call-template>
      </xsl:if>
          
      <!-- next: dates -->
      <xsl:for-each select="pub-date">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Pub Date: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
      <xsl:for-each select="history/date[@date-type='received']">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Received: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
    
      <xsl:for-each select="history/date[@date-type='accepted']">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Accepted: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>

      <xsl:for-each select="history/date[@date-type='rev-request']">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Revise-Request: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
    
      <!-- next: volume and issue -->
      
      <xsl:for-each select="volume">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Volume: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
    
      <xsl:for-each select="issue">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Issue: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
    
      <!-- next: pages -->
      
      <xsl:if test="fpage">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'First Page: '"/>
          <xsl:with-param name="which-node"   select="fpage"/>
        </xsl:call-template>
      </xsl:if>
      
      <xsl:if test="lpage">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'Last Page: '"/>
          <xsl:with-param name="which-node"   select="lpage"/>
        </xsl:call-template>
      </xsl:if>
      
      <!-- next: self-uri -->
      
      <xsl:for-each select="self-uri">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading" select="'URI: '"/>
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>
      
      <!-- finally: article-id -->
      
      <xsl:for-each select="article-id">
        <xsl:call-template name="make-row">
          <xsl:with-param name="cell-heading">
            <xsl:choose>
              <xsl:when test="@pub-id-type='doi'">    <xsl:text>DOI: </xsl:text></xsl:when>
              <xsl:when test="@pub-id-type='medline'"><xsl:text>Medline: </xsl:text></xsl:when>
              <xsl:when test="@pub-id-type='pii'">    <xsl:text>PII: </xsl:text></xsl:when>
              <xsl:when test="@pub-id-type='pmid'">   <xsl:text>PUBMED ID: </xsl:text></xsl:when>
              <xsl:when test="@pub-id-type='sici'">   <xsl:text>SICI: </xsl:text></xsl:when>
              <xsl:otherwise/>
            </xsl:choose>
          </xsl:with-param>
          
          <xsl:with-param name="which-node"   select="."/>
        </xsl:call-template>
      </xsl:for-each>

      </fo:table-body>
    </fo:table>
  
  <!-- end of dealing with the article-metadata part of the tree -->
  </xsl:for-each>  

</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-ROW                                      -->
<!-- ============================================================= -->


<!-- making single-cell rows is always the same:
     one template serves for all.
     put out the cell-heading and then the source content. -->

<xsl:template name="make-row">

<!-- values are received from the calling template
     make-...-metadata-table -->
<xsl:param name="cell-heading"/>
<xsl:param name="which-node" select="false()"/>

      <fo:table-row>
        
        <fo:table-cell padding-top="2pt"
                       padding-left="0.1in"
                       border-style="solid"
                       border-width="1pt">
          <fo:block>
            <xsl:choose>
            
              <!-- the normal case: a regular row -->
              <xsl:when test="$which-node">
                <!-- the cell heading -->
                <fo:wrapper font-family="{$textfont}"
                            font-size="{$cover-cell-heading-size}pt">
                  <xsl:value-of select="$cell-heading"/>
                </fo:wrapper>
                <!-- the source content -->
                <fo:wrapper font-family="{$textfont}"
                            font-size="{$cover-cell-heading-size}pt"
                            font-weight="bold">
                  <xsl:apply-templates select="$which-node"/>
                </fo:wrapper> 
              </xsl:when>
              
              <!-- otherwise, it's a columnhead -->
              <xsl:otherwise>
                <fo:wrapper font-family="{$titlefont}"
                            font-size="{$cover-sec1-title-size}pt"
                            font-weight="bold">
                  <xsl:value-of select="$cell-heading"/>
                </fo:wrapper>
              </xsl:otherwise>
            </xsl:choose>
              
          </fo:block>
        </fo:table-cell>
      </fo:table-row>

</xsl:template>


<!-- ============================================================= -->
<!-- PUB-DATE AND DATE                                             -->
<!-- ============================================================= -->


<xsl:template match="article-meta/pub-date 
                   | article-meta/history/date[@date-type='received']
                   | article-meta/history/date[@date-type='accepted']
                   | article-meta/history/date[@date-type='rev-request']
                   ">

  <xsl:apply-templates select="year" mode="meta"/>
  <xsl:if test="month">
    <xsl:text>-</xsl:text>
    <xsl:apply-templates select="month" mode="meta"/>
  </xsl:if>
  <xsl:if test="day">
    <xsl:text>-</xsl:text>
    <xsl:apply-templates select="day" mode="meta"/>
  </xsl:if>
  <xsl:if test="season">
    <xsl:text>[emd]</xsl:text>
    <xsl:apply-templates select="season" mode="meta"/>
  </xsl:if>
  <xsl:if test="string-date">
    <xsl:text>[emd]</xsl:text>
    <xsl:apply-templates select="string-date" mode="meta"/>
  </xsl:if>

</xsl:template>


<!-- suppress in no-mode because they're called explicitly
     in meta mode -->
<xsl:template match="article-meta/pub-date/* | history/date/*"/>


</xsl:transform>