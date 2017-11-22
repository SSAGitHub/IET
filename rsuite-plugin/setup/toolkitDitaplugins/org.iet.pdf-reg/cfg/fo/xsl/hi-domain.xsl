<?xml version='1.0'?>

<!-- 
Copyright © 2004-2006 by Idiom Technologies, Inc. All rights reserved. 
IDIOM is a registered trademark of Idiom Technologies, Inc. and WORLDSERVER
and WORLDSTART are trademarks of Idiom Technologies, Inc. All other 
trademarks are the property of their respective owners. 

IDIOM TECHNOLOGIES, INC. IS DELIVERING THE SOFTWARE "AS IS," WITH 
ABSOLUTELY NO WARRANTIES WHATSOEVER, WHETHER EXPRESS OR IMPLIED,  AND IDIOM
TECHNOLOGIES, INC. DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING
BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
PURPOSE AND WARRANTY OF NON-INFRINGEMENT. IDIOM TECHNOLOGIES, INC. SHALL NOT
BE LIABLE FOR INDIRECT, INCIDENTAL, SPECIAL, COVER, PUNITIVE, EXEMPLARY,
RELIANCE, OR CONSEQUENTIAL DAMAGES (INCLUDING BUT NOT LIMITED TO LOSS OF 
ANTICIPATED PROFIT), ARISING FROM ANY CAUSE UNDER OR RELATED TO  OR ARISING 
OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN IF IDIOM
TECHNOLOGIES, INC. HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. 

Idiom Technologies, Inc. and its licensors shall not be liable for any
damages suffered by any person as a result of using and/or modifying the
Software or its derivatives. In no event shall Idiom Technologies, Inc.'s
liability for any damages hereunder exceed the amounts received by Idiom
Technologies, Inc. as a result of this transaction.

These terms and conditions supersede the terms and conditions in any
licensing agreement to the extent that such terms and conditions conflict
with those set forth herein.

This file is part of the DITA Open Toolkit project hosted on Sourceforge.net. 
See the accompanying license.txt file for applicable licenses.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:mml="http://www.w3.org/1998/Math/MathML"
    version="2.0">
  
  <xsl:param name="debug">false</xsl:param>

    <xsl:template match="*[contains(@class,' hi-d/b ')]">
      <xsl:if test="$debug='true'">[hi/b]</xsl:if>  
     
      <xsl:variable name="previous_node" >
        <xsl:call-template name="get_previous_node"/>
      </xsl:variable>    
      <xsl:variable name="pn">
        <xsl:for-each select="$previous_node">
          <xsl:call-template name="test_previous_node_type"/>
        </xsl:for-each>
      </xsl:variable> 

      <xsl:choose>
        <xsl:when test="$pn = 'insert_start'"><xsl:if test="$debug='true'">[1]</xsl:if>
          <fo:inline color="green" font-weight="bold">
            <xsl:apply-templates mode="filter_for_PIs"/>                    
          </fo:inline>
        </xsl:when>
        <xsl:otherwise><xsl:if test="$debug='true'">[2]</xsl:if>
          <fo:inline xsl:use-attribute-sets="b">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates  mode="filter_for_PIs"/>
          </fo:inline>          
        </xsl:otherwise>
      </xsl:choose>
 
    </xsl:template>

    <xsl:template match="*[contains(@class,' hi-d/i ')]">
      <xsl:if test="$debug='true'">[hi/i]</xsl:if>  
      
      <xsl:variable name="previous_node" >
        <xsl:call-template name="get_previous_node"/>
      </xsl:variable>    
      <xsl:variable name="pn">
        <xsl:for-each select="$previous_node">
          <xsl:call-template name="test_previous_node_type"/>
        </xsl:for-each>
      </xsl:variable> 
      
      <xsl:choose>
        <xsl:when test="$pn = 'insert_start'"><xsl:if test="$debug='true'">[1]</xsl:if>
          <fo:inline color="green" font-weight="bold">
            <xsl:apply-templates mode="filter_for_PIs"/>                    
          </fo:inline>
        </xsl:when>
        <xsl:otherwise><xsl:if test="$debug='true'">[2]</xsl:if>
          <fo:inline xsl:use-attribute-sets="i">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates  mode="filter_for_PIs"/>
          </fo:inline>          
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class,' hi-d/u ')]">
      <xsl:if test="$debug='true'">[hi/u]</xsl:if>  
      
      <xsl:variable name="previous_node" >
        <xsl:call-template name="get_previous_node"/>
      </xsl:variable>    
      <xsl:variable name="pn">
        <xsl:for-each select="$previous_node">
          <xsl:call-template name="test_previous_node_type"/>
        </xsl:for-each>
      </xsl:variable> 
      
      <xsl:choose>
        <xsl:when test="$pn = 'insert_start'"><xsl:if test="$debug='true'">[1]</xsl:if>
          <fo:inline color="green" font-weight="bold">
            <xsl:apply-templates mode="filter_for_PIs"/>                    
          </fo:inline>
        </xsl:when>
        <xsl:otherwise><xsl:if test="$debug='true'">[2]</xsl:if>
          <fo:inline xsl:use-attribute-sets="u">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates  mode="filter_for_PIs"/>
          </fo:inline>          
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class,' hi-d/tt ')]">
      <fo:inline xsl:use-attribute-sets="tt">
        <xsl:call-template name="commonattributes"/>
        <xsl:apply-templates/>
      </fo:inline>
    </xsl:template>

    <xsl:template match="*[contains(@class,' hi-d/sup ')]">
      <xsl:if test="$debug='true'">[hi/sup]</xsl:if>  
      
      <xsl:variable name="previous_node" >
        <xsl:call-template name="get_previous_node"/>
      </xsl:variable>    
      <xsl:variable name="pn">
        <xsl:for-each select="$previous_node">
          <xsl:call-template name="test_previous_node_type"/>
        </xsl:for-each>
      </xsl:variable> 
      
      <xsl:choose>
        <xsl:when test="$pn = 'insert_start'"><xsl:if test="$debug='true'">[1]</xsl:if>
          <fo:inline color="green" font-weight="bold">
            <xsl:apply-templates mode="filter_for_PIs"/>                    
          </fo:inline>
        </xsl:when>
        <xsl:otherwise><xsl:if test="$debug='true'">[2]</xsl:if>
          <fo:inline xsl:use-attribute-sets="sup">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates  mode="filter_for_PIs"/>
          </fo:inline>          
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

    <xsl:template match="*[contains(@class,' hi-d/sub ')]">
      <xsl:if test="$debug='true'">[hi/sub]</xsl:if>  
      
      <xsl:variable name="previous_node" >
        <xsl:call-template name="get_previous_node"/>
      </xsl:variable>    
      <xsl:variable name="pn">
        <xsl:for-each select="$previous_node">
          <xsl:call-template name="test_previous_node_type"/>
        </xsl:for-each>
      </xsl:variable> 
      
      <xsl:choose>
        <xsl:when test="$pn = 'insert_start'"><xsl:if test="$debug='true'">[1]</xsl:if>
          <fo:inline color="green" font-weight="bold">
            <xsl:apply-templates mode="filter_for_PIs"/>                    
          </fo:inline>
        </xsl:when>
        <xsl:otherwise><xsl:if test="$debug='true'">[2]</xsl:if>
          <fo:inline xsl:use-attribute-sets="sub">
            <xsl:call-template name="commonattributes"/>
            <xsl:apply-templates  mode="filter_for_PIs"/>
          </fo:inline>          
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

</xsl:stylesheet>