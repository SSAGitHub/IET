<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:df="http://dita2indesign.org/dita/functions"
      xmlns:e2s="http//dita2indesign.org/functions/element-to-style-mapping"
      xmlns:relpath="http://dita2indesign/functions/relpath"
      xmlns:local="urn:local-functions"      
      xmlns:idPkg="http://ns.adobe.com/AdobeInDesign/idml/1.0/packaging"
      exclude-result-prefixes="xs e2s df relpath local idPkg"
      version="2.0">
  
  <!-- ====================
       IET article topic to InCopy ICML transform
       
       Takes an <article> topic as input and produces one or
       more InCopy ICML files from it.
       
       
    
       ==================== -->
  
  <!-- Required libraries (imported by topic2icmlImpl.xsl):
  <xsl:import href="rsuite:/res/plugin/dita4publishers/toolkit_plugins/net.sourceforge.dita4publishers.common.xslt/xsl/lib/relpath_util.xsl"/>
  <xsl:import href="rsuite:/res/plugin/dita4publishers/toolkit_plugins/net.sourceforge.dita4publishers.common.xslt/xsl/lib/dita-support-lib.xsl"/>
  
  
-->

  <xsl:import href="dita2indesignImpl-IET.xsl"/>  
  <xsl:include href="elem2styleMapper-IET.xsl"/>
  <xsl:include href="calstbl2IcmlImpl-IET.xsl"/>
  
  <xsl:output method="xml" indent="yes" />
</xsl:stylesheet>
