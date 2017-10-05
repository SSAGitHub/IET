<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:mml="http://www.w3.org/1998/Math/MathML"
    version="2.0">

    <xsl:param name="show_comment_content">true</xsl:param>

    <xsl:param name="debug">false</xsl:param>

    <xsl:variable name="quote">"</xsl:variable>

    <xsl:template
        match="processing-instruction('oxy_delete') |
        processing-instruction('oxy_comment_start')"
        mode="apply_change_marks_to_block">
        <xsl:param name="comment"/>
        <xsl:param name="delete"/>
        <xsl:param name="comment_content"/>
        <xsl:param name="delete_content"/>
        <xsl:param name="context">paragraph</xsl:param>
        <xsl:if test="$debug='true'">[A]</xsl:if>
        <xsl:choose>
            <xsl:when test="$context = 'list'">
                <fo:list-item >
                    <fo:list-item-label >
                        <fo:block/>
                    </fo:list-item-label>
                    <fo:list-item-body>
                        <fo:block xsl:use-attribute-sets="p common.border__right">
                            <xsl:call-template name="commonattributes"/>
                            <xsl:choose>
                                <xsl:when test="self::processing-instruction('oxy_delete')">
                                    <xsl:if test="$debug='true'">[1]</xsl:if>
                                    <xsl:call-template name="delete"/>
                                </xsl:when>
                                <xsl:when test="self::processing-instruction('oxy_comment_start')">
                                    <xsl:if test="$debug='true'">[2]</xsl:if>
                                    <xsl:call-template name="comment"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="$debug='true'">[3]</xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </xsl:when>
            <xsl:otherwise>
                <fo:block xsl:use-attribute-sets="p common.border__right">
                    <xsl:call-template name="commonattributes"/>
                    <xsl:choose>
                        <xsl:when test="self::processing-instruction('oxy_delete')">
                            <xsl:if test="$debug='true'">[1]</xsl:if>
                            <xsl:call-template name="delete"/>
                        </xsl:when>
                        <xsl:when test="self::processing-instruction('oxy_comment_start')">
                            <xsl:if test="$debug='true'">[2]</xsl:if>
                            <xsl:call-template name="comment"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="$debug='true'">[3]</xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <xsl:template match="node()" mode="filter_for_PIs">

        <xsl:if test="$debug='true'">[B]</xsl:if>
        <xsl:if test="$debug='true'">
            <xsl:choose>
                <xsl:when test="self::text()">[Text Node]</xsl:when>
                <xsl:when test="self::processing-instruction()">[PI Node]</xsl:when>
                <xsl:when test="self::comment()">[Comment Node]</xsl:when>
                <xsl:when test="self::element()">[Element Node]</xsl:when>
            </xsl:choose>
        </xsl:if>

        <xsl:variable name="last_node">
            <xsl:choose>
                <xsl:when test="not(following-sibling::node())">last node</xsl:when>
                <xsl:otherwise>not last node</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="previous_node">
            <xsl:choose>
                <xsl:when test="preceding-sibling::node()">
                    <xsl:copy-of select="preceding-sibling::node()[position()=1]"/>
                </xsl:when>
                <xsl:otherwise>none</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="pn">
            <xsl:for-each select="$previous_node">
                <xsl:choose>
                    <xsl:when test="processing-instruction('oxy_insert_start')"
                        >insert_start</xsl:when>
                    <xsl:when test="processing-instruction('oxy_comment_start')"
                        >comment_start</xsl:when>
                    <xsl:when test="processing-instruction('oxy_delete')">delete</xsl:when>
                    <xsl:otherwise>doesn't matter</xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$pn = 'insert_start'">
                <xsl:if test="$debug='true'">[1]</xsl:if>
                <fo:inline color="green" font-weight="bold">
                    <xsl:apply-templates select="."/>
                </fo:inline>
            </xsl:when>
            <xsl:when test="self::processing-instruction('oxy_comment_start')">
                <xsl:if test="$debug='true'">[2]</xsl:if>
                <xsl:choose>
                    <xsl:when test="$show_comment_content = 'true'">
                        <xsl:call-template name="comment"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>
            <!--            <xsl:when test="$pn = 'comment_start'"><xsl:if test="$debug='true'">[2]</xsl:if>
                <xsl:choose>
                    <xsl:when test="$show_comment_content = 'true'">
                        <xsl:for-each select="$previous_node/processing-instruction()">
                            <xsl:call-template name="comment"/>
                        </xsl:for-each>
                        <xsl:choose>
                            <xsl:when test="self::processing-instruction('oxy_delete')">
                                <xsl:call-template name="delete"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="." />
                            </xsl:otherwise>
                        </xsl:choose>                       
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="." />
                    </xsl:otherwise>
                </xsl:choose>                
            </xsl:when>-->
            <xsl:when test="self::processing-instruction('oxy_delete')">
                <xsl:if test="$debug='true'">[3]</xsl:if>
                <xsl:call-template name="delete"/>
            </xsl:when>
            <!--            <xsl:when test="$last_node = 'last node'"><xsl:if
                test="$debug='true'">[LN]</xsl:if>
                <xsl:choose>
                    <xsl:when test="self::processing-instruction('oxy_delete')">
                        <xsl:call-template name="delete"/>
                    </xsl:when>
                    <xsl:when test="$show_comment_content = 'true'">
                        <xsl:for-each select="self::processing-instruction('oxy_comment_start')">
                            <xsl:call-template name="comment"/>
                        </xsl:for-each>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>-->
            <xsl:otherwise>
                <xsl:if test="$debug='true'">[4]</xsl:if>
                <xsl:apply-templates select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="comment">
        <xsl:variable name="commenter" select="substring-before(substring-after(., 'author='), ' ')"/>
        <xsl:variable name="commenterlen" select="string-length($commenter) - 2"/>
        <xsl:variable name="commenter_name" select="substring($commenter, 2 , $commenterlen)"/>
        <xsl:variable name="c" select="substring-after(., 'comment=')"/>
        <xsl:variable name="cz" select="substring($c, 2)"/>
        <xsl:variable name="comment_content" select="substring-before($cz, $quote)"/>

        <fo:inline background-color="yellow" font-style="italic"
                >[Comment<xsl:text> </xsl:text>by<xsl:text> </xsl:text><xsl:value-of
                select="$commenter_name"/>:<xsl:text> </xsl:text><fo:inline font-weight="bold"
                    ><xsl:value-of select="$comment_content"
            /></fo:inline>]<xsl:text>
                                        </xsl:text></fo:inline>
    </xsl:template>


    <xsl:template name="delete">
        <xsl:variable name="x" select="count(preceding-sibling::text())"/>
        <xsl:variable name="c" select="substring-after(., 'content=')"/>
        <xsl:variable name="clen" select="string-length($c) - 2"/>
        <xsl:variable name="delete_content" select="substring($c, 2 , $clen)"/>
        <fo:inline color="red" text-decoration="line-through">
            <xsl:value-of select="$delete_content"/>
        </fo:inline>
    </xsl:template>

    <xsl:template name="get_previous_node">
        <xsl:choose>
            <xsl:when test="preceding-sibling::node()">
                <xsl:choose>
                    <!-- skip white space only nodes! -->
                    <xsl:when test="preceding-sibling::node()[1]='&#x20;'">
                        <xsl:copy-of select="preceding-sibling::node()[position()=2]"/>
                    </xsl:when>
                    <xsl:when test="preceding-sibling::node()[1]='&#x09;'">
                        <xsl:copy-of select="preceding-sibling::node()[position()=2]"/>
                    </xsl:when>
                    <xsl:when test="preceding-sibling::node()[1]='&#x0A;'">
                        <xsl:copy-of select="preceding-sibling::node()[position()=2]"/>
                    </xsl:when>
                    <xsl:when test="preceding-sibling::node()[1]='&#x0D;'">
                        <xsl:copy-of select="preceding-sibling::node()[position()=2]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="preceding-sibling::node()[position()=1]"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>none</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="test_previous_node_type">
        <xsl:choose>
            <xsl:when test="self::processing-instruction('oxy_insert_start')"
                >insert_start</xsl:when>
            <xsl:when test="processing-instruction('oxy_insert_start')">insert_start</xsl:when>
            <xsl:when test="processing-instruction('oxy_comment_start')">comment_start</xsl:when>
            <xsl:when test="processing-instruction('oxy_delete')">delete</xsl:when>
            <xsl:when test="element()">element</xsl:when>
            <xsl:when test="text()">text</xsl:when>
            <xsl:otherwise>doesn't matter</xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template name="determine_nested_insert_start">
        <xsl:choose>
            <xsl:when
                test="count(preceding-sibling::processing-instruction('oxy_insert_start')) mod 2
            = 1"
                >odd</xsl:when>
            <xsl:when
                test="count(preceding-sibling::processing-instruction('oxy_insert_start')) mod 2
            = 0"
                >even</xsl:when>
            <xsl:otherwise>not nested insert node</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="determine_nested_insert_end">
        <xsl:choose>
            <xsl:when
                test="count(preceding-sibling::processing-instruction('oxy_insert_end')) mod 2
                = 1"
                >odd</xsl:when>
            <xsl:when
                test="count(preceding-sibling::processing-instruction('oxy_insert_end')) mod 2
                = 0"
                >even</xsl:when>
            <xsl:otherwise>not nested insert node</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="determine_comments_and_deletes">
        <xsl:choose>
            <xsl:when
                test="preceding-sibling::processing-instruction('oxy_comment_start') or
                preceding-sibling::processing-instruction('oxy_delete')">
                <xsl:variable name="nodes_on_preceding-sibling_axis">
                    <xsl:for-each select="preceding-sibling::node()">
                        <xsl:copy-of select="."/>
                    </xsl:for-each>
                </xsl:variable>
                <xsl:for-each select="$nodes_on_preceding-sibling_axis">
                    <xsl:choose>
                        <xsl:when test="not(*)">
                            <xsl:for-each select="child::node()[1]">
                                <xsl:choose>
                                    <xsl:when test="following-sibling::*">change mark something
                                        wrong</xsl:when>
                                    <xsl:when
                                        test="self::processing-instruction('oxy_comment_start')
                                        or self::processing-instruction('oxy_delete')"
                                        >change mark for this element</xsl:when>
                                    <xsl:when
                                        test="following-sibling::processing-instruction('oxy_comment_start') or following-sibling::processing-instruction('oxy_delete')"
                                        >change mark for this element</xsl:when>
                                    <xsl:otherwise>no change mark for this element</xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:when test="*">
                            <xsl:for-each select="child::*[last()]">
                                <xsl:choose>
                                    <xsl:when test="following-sibling::*">change mark something
                                        wrong</xsl:when>
                                    <xsl:when
                                        test="following-sibling::processing-instruction('oxy_comment_start') or following-sibling::processing-instruction('oxy_delete')"
                                        >change mark for this element</xsl:when>
                                    <xsl:otherwise>no change mark for this element</xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>no change marks present</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="determine_comments_and_deletes_content">
        <xsl:param name="comments_and_deletes"/>
        <xsl:choose>
            <xsl:when test="$comments_and_deletes = 'change mark for this element'">
                <xsl:variable name="nodes_on_preceding-sibling_axis">
                    <xsl:for-each select="preceding-sibling::node()">
                        <xsl:copy-of select="."/>
                    </xsl:for-each>
                </xsl:variable>
                <xsl:for-each select="$nodes_on_preceding-sibling_axis">
                    <xsl:choose>
                        <xsl:when test="not(*)">
                            <xsl:for-each select="child::node()">
                                <xsl:choose>
                                    <xsl:when
                                        test="self::processing-instruction('oxy_comment_start')
                                            or
                                            self::processing-instruction('oxy_delete')">
                                        <xsl:copy-of select="."/>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:when test="*">
                            <xsl:choose>
                                <xsl:when
                                    test="child::*[1]/following-sibling::*[not(following-sibling::*)][1]">
                                    <xsl:for-each
                                        select="child::*[1]/following-sibling::*[not(following-sibling::*)][1]">
                                        <xsl:for-each
                                            select="following-sibling::processing-instruction()">
                                            <xsl:copy-of select="."/>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="child::*">
                                    <xsl:for-each select="child::*[1]">
                                        <xsl:for-each
                                            select="following-sibling::processing-instruction()">
                                            <xsl:copy-of select="."/>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>no change mark content</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="determine_delete_content_only">
        <xsl:choose>
            <xsl:when test="processing-instruction('oxy_delete')">
                <xsl:for-each select="processing-instruction('oxy_delete')">
                    <xsl:choose>
                        <xsl:when test="preceding-sibling::text() or preceding-sibling::* or
                            processing-instruction('oxy_insert')">delete content only: no 1</xsl:when>
                        <xsl:when test="following-sibling::text() or following-sibling::* or
                            processing-instruction('oxy_insert')">delete content only: no 2</xsl:when>                            
                        <xsl:otherwise>delete content only: yes</xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>delete content only: no 3</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
</xsl:stylesheet>
