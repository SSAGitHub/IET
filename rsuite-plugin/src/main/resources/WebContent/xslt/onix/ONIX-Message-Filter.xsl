<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ONIX="http://ns.editeur.org/onix/3.0/reference"     
    exclude-result-prefixes="ONIX">
    
    
    
    <xsl:param name="ONIX_source_message_uri" />
    <xsl:param name="ONIX_configuration_uri" />
    
    <xsl:variable name="ONIX_source_message" select="document($ONIX_source_message_uri)" as="node()"/>
    <xsl:variable name="ONIX_configuration_file"
        select="document($ONIX_configuration_uri)"/>
    
    
    
    <!-- the param "productFormat" takes three possible values:
        1. print
        2. digital
        3. print;digital
    -->
    <xsl:param name="productFormat">print</xsl:param>
    <xsl:param name="productOnly">false</xsl:param>
    
    <xsl:param name="debug">false</xsl:param>
    <xsl:variable name="output_file" select="concat('ONIX Filtered Message Output - ',
        $productFormat, '.xml' )"></xsl:variable>
    <xsl:strip-space elements="*"/>
    
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" encoding="utf-8"/>
    
    <xsl:template match="/">
            <!-- start by processing the root element of the source ONIX message -->
            <xsl:for-each select="$ONIX_source_message">
                <xsl:apply-templates/>
            </xsl:for-each>        
    </xsl:template>
    
    <xsl:template match="ONIX:ONIXMessage">
    <xsl:choose>
    	<xsl:when test="$productOnly ='true'">
    		<xsl:apply-templates/>
    	</xsl:when>
    	<xsl:otherwise>
    		<ONIXMessage xmlns="http://ns.editeur.org/onix/3.0/reference" release="3.0">
            <xsl:apply-templates/>
        </ONIXMessage>
    	</xsl:otherwise>
    </xsl:choose>                             
    </xsl:template>
    
    <xsl:template match="ONIX:*">
        <xsl:variable name="element_name" select="name()"/>
        <!-- make a basic distinction between the set of XHTML elements included in ONIX and the
            ONIX-proper elements. All XHTML elements found in the ONIX Message are copied to output
            IF their ancester/parent ONIX element is defined in the configuration -->
        <xsl:choose>
            <!-- this is the list of XHTML elements in the ONIX Schema. This must be maintained and
                updated if the ONIX Schema changes (adds or removes) the list of XHTML elements
                included. The elements are in alphabetical order to ease maintenance -->
            <xsl:when
                test="self::ONIX:a or self::ONIX:abbr or self::ONIX:acronym or
                self::ONIX:address or self::ONIX:area or self::ONIX:b or self::ONIX:bdo or self::ONIX:big  or
                self::ONIX:blockquote or self::ONIX:br or self::ONIX:caption or self::ONIX:cite or
                self::ONIX:code or self::ONIX:col or self::ONIX:colgroup or
                self::ONIX:dfn or self::ONIX:div or self::ONIX:dd or self::ONIX:dl or self::ONIX:dt
                or self::ONIX:em or self::ONIX:h1 or self::ONIX:h2 or self::ONIX:h3 or self::ONIX:h5 or
                self::ONIX:h6 or self::ONIX:hr or self::ONIX:i or self::ONIX:img or self::ONIX:kdb
                or self::ONIX:li or self::ONIX:map or self::ONIX:ol or self::ONIX:p or
                self::ONIX:pre or self::ONIX:q or self::ONIX:rb or self::ONIX:rbc or self::ONIX:rt
                or self::ONIX:rtc or self::ONIX:ruby or self::ONIX:samp or self::ONIX:small or
                self::ONIX:span or self::ONIX:strong or self::ONIX:sub or self::ONIX:sup or
                self::ONIX:table or self::ONIX:tbody or self::ONIX:tfoot or self::ONIX:thead or
                self::ONIX:td or self::ONIX:th or self::ONIX:tr or self::ONIX:ul or self::ONIX:var">
                <xsl:element name="{$element_name}" xmlns="http://ns.editeur.org/onix/3.0/reference">
                    <xsl:apply-templates select="@*"/>
                    <xsl:apply-templates/>
                </xsl:element>
            </xsl:when>
            <xsl:when test="self::ONIX:TitleText">
                <xsl:variable name="title_text" select="."/>
                <xsl:choose>
                    <xsl:when test="substring-before($title_text, ' ') = 'A' or
                        substring-before($title_text, ' ') = 'An' or 
                        substring-before($title_text, ' ') = 'The'">
                        <TitlePrefix xmlns="http://ns.editeur.org/onix/3.0/reference"><xsl:apply-templates select="@textcase" /><xsl:value-of select="substring-before($title_text, ' ')"/></TitlePrefix>
                        <TitleWithoutPrefix xmlns="http://ns.editeur.org/onix/3.0/reference"><xsl:apply-templates select="@textcase" /><xsl:value-of select="substring-after($title_text, ' ')"/></TitleWithoutPrefix>                        
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:element name="{$element_name}" xmlns="http://ns.editeur.org/onix/3.0/reference">
                            <xsl:apply-templates select="@textcase" />
                            <xsl:apply-templates select="text() |*" mode="copy"/>
                        </xsl:element>                       
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- here starts processing of ONIX-proper elements (except <TitleText>)-->
            <xsl:otherwise>
                <xsl:variable name="gen-id" select="generate-id()"/>
                <xsl:variable name="ONIX_message_xpath">
                    <xsl:call-template name="xpathTracer">
                        <xsl:with-param name="element_name" select="$element_name"/>
                        <xsl:with-param name="file_context">source message</xsl:with-param>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:if test="$debug = 'true'">
                    <xsl:comment> MESSAGE element: <xsl:value-of select="$element_name"/>, XPATH: <xsl:value-of select="$ONIX_message_xpath"/></xsl:comment>
                </xsl:if>
                <xsl:choose>
                    <!-- if this element has the attribute @productFormat, retain it or omit it
                        depending upon the value of @productFormat and the value of param $productFormat -->
                    <xsl:when test="@productFormat">                        
                        <xsl:choose>
                            <xsl:when test="@productFormat = 'print' and $productFormat = 'print'">
                                <!-- now check whether the element is included in the configuration file -->
                                <xsl:call-template name="check_configuration_file">
                                    <xsl:with-param name="element_name" select="$element_name"/>                            
                                    <xsl:with-param name="gen-id" select="$gen-id"/>
                                    <xsl:with-param name="ONIX_message_xpath" select="$ONIX_message_xpath"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:when test="@productFormat = 'digital' and $productFormat = 'digital'">
                                <!-- now check whether the element is included in the configuration file -->
                                <xsl:call-template name="check_configuration_file">
                                    <xsl:with-param name="element_name" select="$element_name"/>                            
                                    <xsl:with-param name="gen-id" select="$gen-id"/>
                                    <xsl:with-param name="ONIX_message_xpath" select="$ONIX_message_xpath"/>
                                </xsl:call-template>                            
                            </xsl:when>
                            <xsl:when test="@productFormat = 'print;digital' and $productFormat = 'print;digital'">
                                <!-- now check whether the element is included in the configuration file -->
                                <xsl:call-template name="check_configuration_file">
                                    <xsl:with-param name="element_name" select="$element_name"/>                            
                                    <xsl:with-param name="gen-id" select="$gen-id"/>
                                    <xsl:with-param name="ONIX_message_xpath" select="$ONIX_message_xpath"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- use this to see elements that are filtered out of the result on the basis of @productFormat -->
                                <!--<xsl:message>Element &lt;<xsl:value-of select="$element_name"/>&gt; in the
                                    ONIX master file has a @productFormat of "<xsl:value-of
                                        select="@productFormat"/>". This does not match the input
                                    param setting of "<xsl:value-of select="$productFormat"/>; the
                                    element is omitted from output</xsl:message>-->
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="$productOnly = 'true' and  not(ancestor-or-self::ONIX:Product)"/>
                    <xsl:otherwise>
                        <xsl:call-template name="check_configuration_file">
                            <xsl:with-param name="element_name" select="$element_name"/>                            
                            <xsl:with-param name="gen-id" select="$gen-id"/>
                            <xsl:with-param name="ONIX_message_xpath" select="$ONIX_message_xpath"/>
                        </xsl:call-template>        
                    </xsl:otherwise>                   
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="check_configuration_file">
        <xsl:param name="element_name" />
        <xsl:param name="gen-id" />
        <xsl:param name="ONIX_message_xpath" />
        <xsl:for-each
            select="$ONIX_configuration_file/descendant::*[name() = $element_name]">
            <!-- 1a: get the matching configuration element's XPath identity -->
            <xsl:variable name="ONIX_configuration_xpath">
                <xsl:call-template name="xpathTracer">
                    <xsl:with-param name="element_name" select="$element_name"/>
                    <xsl:with-param name="file_context">configuration file</xsl:with-param>
                </xsl:call-template>
            </xsl:variable>
            <xsl:if test="$debug = 'true'">
                <xsl:comment> CONFIG element: <xsl:value-of select="$ONIX_configuration_xpath"/></xsl:comment>
            </xsl:if>
            <!--  2: for each element name that matches, check whether it matches the current element's XPath identity -->
            <xsl:variable name="match">
                <xsl:choose>
                    <xsl:when test="$ONIX_message_xpath = $ONIX_configuration_xpath"
                        >YES</xsl:when>
                    <xsl:otherwise>NO</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <!-- if there is a match, switch back to the source document (remember, we're
                    currently in the configuration file) and get the content -->
            <xsl:choose>
                <xsl:when test="$match = 'YES'">
                    <xsl:if test="$debug = 'true'">
                        <xsl:comment> YES XPATH MATCH for <xsl:value-of select="$ONIX_configuration_xpath"/></xsl:comment>
                    </xsl:if>
                    <xsl:for-each
                        select="$ONIX_source_message/descendant::ONIX:*[generate-id() = $gen-id]">
                        <xsl:if test="$debug = 'true'">
                            <xsl:comment> CHECK: only one match for <xsl:value-of select="$ONIX_configuration_xpath"/> @ <xsl:value-of select="$gen-id"/></xsl:comment>
                        </xsl:if>
                        <xsl:element name="{$element_name}"
                            xmlns="http://ns.editeur.org/onix/3.0/reference">
                            <xsl:if test="$debug = 'true'">
                                <xsl:attribute name="refname">
                                    <xsl:value-of select="$gen-id"/>
                                </xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates select="@*"/>                                 
                            <xsl:apply-templates/>                                    
                        </xsl:element>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="$debug = 'true'">
                        <xsl:comment> NO XPATH MATCH for <xsl:value-of select="$ONIX_configuration_xpath"/></xsl:comment>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>     
    </xsl:template>

    <xsl:template name="xpathTracer">
        <xsl:param name="element_name"/>
        <xsl:param name="file_context"/>
        <xsl:if test="$debug = 'true'">
            <xsl:message>FC = <xsl:value-of select="$file_context"/></xsl:message>
        </xsl:if>
        <xsl:for-each select="ancestor-or-self::*">
            <xsl:variable name="currentLoc" select="."/>
            <xsl:value-of select="concat('/', name())"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="*" mode="copy">
        <xsl:variable name="name" select="name()"/>
        
        <xsl:if test="not($productOnly = 'true' and not(ancestor-or-self::ONIX:Product))">
            <xsl:element name="{$name}" xmlns="http://ns.editeur.org/onix/3.0/reference">
                <xsl:apply-templates select="@*"/>
                <xsl:apply-templates mode="copy"/>
            </xsl:element>                
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:variable name="name" select="name()"/>
        <xsl:choose>
            <!-- @productFormat is always prevented in output -->
            <xsl:when test="$name = 'productFormat'"/>
            <xsl:otherwise>
                <xsl:attribute name="{$name}">
                    <xsl:value-of select="."/>
                </xsl:attribute>                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>
