<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://saxon.sf.net/" 
	exclude-result-prefixes="saxon ditaarch" 
	version="2.0" 
	xmlns:ditaarch="http://dita.oasis-open.org/architecture/2005/">
	<xsl:output method="xml" indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:param name="rsuite.public.id">urn:pubid:org.iet:doctypes:dita:chapter</xsl:param>
	<xsl:param name="rsuite.system.id">chapter.dtd</xsl:param>
	<!-- the following two params should be used together: if one is 'true' then the other should be as well and vice-versa for 'false';
        the reason why there are two params and not one is that it's theoretically possible that someone would want to mark an elemnet that
        has deleted content as changed and yet not retain the deleted content itself (not saying that's recommened, just possible, and so supported) -->
	<!-- setting for whether deleted content should be retained 
        1. if the deleted content is an element + its content, then output the element with @status='deleted' (hey, retaining deleted content, especially elements, is inherently weird)
        2. if the deleted content is a text() node then wrap it in <ph status="deleted"> (deleted content inside) </ph> -->
	<xsl:param name="retain_deleted_content">true</xsl:param>
	<!-- setting for whether the paragraph containing ONLY a deletion should be marked as @status="changed" -->
	<xsl:param name="mark_deleted_content_parent_paragraph">true</xsl:param>
	<xsl:param name="show_comment_content">false</xsl:param>
	<!-- setting for which element should be used to wrap comments -->
	<xsl:param name="comment_element">draft-comment</xsl:param>
	<!-- setting for which element should be used to wrap inserted or deleted text() nodes 
        (since without wrapping them in an element and marking it there's no way to show the text() node has been inserted/deleted) -->
	<xsl:param name="change_mark_element">ph</xsl:param>
	<xsl:param name="debug">false</xsl:param>
	<xsl:variable name="quote">"</xsl:variable>
	<!-- suppressed nodes listed here for easy reference -->
	<!-- match the Oxygen PI that signals the end of an insert and do nothing -->
	<xsl:template match="processing-instruction()[name() = 'oxy_insert_end']"/>
	<!-- match the Oxygen PI that signals the start of a comment and do nothing -->
	<xsl:template match="processing-instruction()[name() = 'oxy_comment_start']"/>
	<!-- match the Oxygen PI that signals the end of an attribute and do nothing -->
	<xsl:template match="processing-instruction()[name() = 'oxy_attributes']"/>
	<xsl:template match="/">
		<xsl:choose>
			<xsl:when test="$rsuite.public.id != ''">
				<xsl:result-document doctype-public="{$rsuite.public.id}" doctype-system="{$rsuite.system.id}" method="xml">
					<xsl:apply-templates/>
				</xsl:result-document>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- match every element and copy it to output -->
	<!-- during that process determine whether it needs @status change markup added and, if so, what value it should have -->
	<xsl:template match="*">
		<!-- the following variables work together to determine whether this element is an inserted element 
        given its position relative to O2 insert start and end PIs -->
		<!-- 1. count the number of insert start PIs on the preceding-sibling axis -->
		<xsl:variable name="preceding-sibling_insert_start_count" select="count(preceding-sibling::processing-instruction('oxy_insert_start'))"/>
		<!-- 2. count the number of end start PIs on the preceding-sibling axis -->
		<xsl:variable name="preceding-sibling_insert_end_count" select="count(preceding-sibling::processing-instruction('oxy_insert_end'))"/>
		<!-- 3. count the number of insert start PIs on the following-sibling axis -->
		<xsl:variable name="following-sibling_insert_start_count" select="count(following-sibling::processing-instruction('oxy_insert_start'))"/>
		<!-- 4. count the number of end start PIs on the following-sibling axis -->
		<xsl:variable name="following-sibling_insert_end_count" select="count(following-sibling::processing-instruction('oxy_insert_end'))"/>
		<xsl:variable name="determine_insert">
			<xsl:choose>
				<!-- A. when there are 0 start insert and 0 end insert PIs on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 0 and $preceding-sibling_insert_end_count = 0">no PIs no insert</xsl:when>
				<!-- B. when there is 1 start insert PI on the preceding-sibling axis and 0 end insert PI on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 1 and $preceding-sibling_insert_end_count = 0">
					<xsl:choose>
						<!-- when there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="count(following-sibling::processing-instruction('oxy_insert_end')) >= 1">insert A</xsl:when>
						<xsl:otherwise>
							<xsl:comment>WARNING 1: preceding-sibling start PI is lost</xsl:comment>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!-- C. when there is 0 start insert PI on the preceding-sibling axis and 1 end insert PI on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 0 and $preceding-sibling_insert_end_count = 1">
					<xsl:comment>WARNING 2: preceding-sibling end PI is lost</xsl:comment>
				</xsl:when>
				<!-- D. when there are 2 PIs on the preceding-sibling axis AND 1 is insert start and 1 is insert end -->
				<xsl:when test="$preceding-sibling_insert_start_count = 1 and $preceding-sibling_insert_end_count = 1">
					<xsl:choose>
						<!-- when there end insert is first and the start insert is second -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start']">
							<xsl:choose>
								<xsl:when test="following-sibling::processing-instruction('oxy_insert_end')">preceding-sibling end PI is lost; insert B</xsl:when>
								<xsl:otherwise>The preceding-sibling start insert and end insert PIs are lost</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<!-- when there start insert is first and the end insert is second -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_end']">this content comes after the previously inserted content; do nothing</xsl:when>
					</xsl:choose>
				</xsl:when>
				<!-- E. when there are 2 PIs on the preceding-sibling axis and both are insert start -->
				<xsl:when test="$preceding-sibling_insert_start_count = 2 and $preceding-sibling_insert_end_count = 0">
					<xsl:choose>
						<!-- when there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="following-sibling::processing-instruction('oxy_insert_end')">insert C</xsl:when>
						<xsl:otherwise>WARNING 3: 2 preceding-sibling insert start PIs are lost</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!-- F. when there are 2 PIs on the preceding-sibling axis and both are insert end -->
				<xsl:when test="$preceding-sibling_insert_end_count = 2 and $preceding-sibling_insert_start_count = 0">WARNING 4: 2 preceding-sibling insert end PIs are lost</xsl:when>
				<!-- G. when there are 3+ insert start/insert end PIs on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count >= 2 and $preceding-sibling_insert_end_count >= 1">
					<xsl:choose>
						<!-- when the immediately precedding sibling PI is insert start and there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and following-sibling::processing-instruction('oxy_insert_end')">insert D</xsl:when>
						<!-- when the immediately precedding sibling PI is insert start and the immediately following PI on the following sibling axis is also insert start-->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and following-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start']">Warning 5: Either the preceding insert start or the following insert start is lost</xsl:when>
						<!-- when the immediately precedding sibling PI is insert start and there are no insert end PIs on the following sibling axis -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and not(following-sibling::processing-instruction()[name() = 'oxy_insert_end'])">Warning 6: Either the preceding insert start or the following insert start is lost</xsl:when>
						<xsl:otherwise>WARNING ^^: preceding-sibling insert start PI is lost</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>well, hmm</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="non-DITA_element" select="namespace-uri(.)"/>
		<xsl:if test="($determine_insert = 'insert A' or $determine_insert = 'insert B' or $determine_insert = 'insert C' or $determine_insert = 'insert D') and
			not($non-DITA_element = '')">
			<xsl:copy-of select="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start']" />
		</xsl:if>
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<!-- 1. the conditions for applying the @status attribute -->
			<!-- NOTE: DITA <title>, <entry>, and <br> may not have @status -->
			<xsl:choose>
				<!-- if this element is in a namespace then it's not DITA. Do not add DITA change markup to it -->
				<xsl:when test="not($non-DITA_element = '')"/>				
				<!-- when this is a newly inserted elememnt -->
				<xsl:when test="($determine_insert = 'insert A' or $determine_insert = 'insert B' or $determine_insert = 'insert C' or $determine_insert = 'insert D') and not(self::title or self::entry or self::br)">
					<xsl:attribute name="status">new</xsl:attribute>
				</xsl:when>
				<!-- when this is a changed element with newly inserted content (this condition is separate from and preceds deleted content) -->
				<xsl:when test="child::processing-instruction('oxy_insert_start') and not(self::title or self::entry or self::br or self::d4pSimpleEnumerator)">
					<xsl:attribute name="status">changed</xsl:attribute>
				</xsl:when>
				<!-- when this is a changed element with (only) deleted content -->
				<xsl:when test="child::processing-instruction('oxy_delete') and not(self::title or self::entry or self::br or self::d4pSimpleEnumerator)">
					<!-- whether to mark the element as changed depends upon the parameter settings -->
					<xsl:choose>
						<xsl:when test="$mark_deleted_content_parent_paragraph = 'true'">
							<xsl:attribute name="status">changed</xsl:attribute>
						</xsl:when>
						<xsl:when test="$mark_deleted_content_parent_paragraph = 'false'"/>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
			<!-- 2. the conditions for processing the child nodes (<title> requires an exception) -->
			<xsl:choose>
				<!-- if this element is in a namespace then it's not DITA. Do not add DITA change markup to it -->
				<xsl:when test="not($non-DITA_element = '')">
					<xsl:apply-templates />
				</xsl:when>				
				<xsl:when test="$determine_insert = 'insert' and self::title">
					<!-- use these warnings to indicate to the user that <title> is wrongly inserted -->
					<xsl:message>WARNING: Incorrect Use of Oxygen Change Tracking in DITA: Cannot Insert/Delete the &lt;title> element; see &lt;<xsl:value-of select="name(parent::*)"/> id="<xsl:value-of select="parent::*/@id"/>"&gt;. Please move the Oxygen PIs inside the &lt;title&gt; element</xsl:message>
					<xsl:comment>WARNING: Incorrect Use of Oxygen Change Tracking in DITA: Cannot Insert/Delete the
  &lt;title> element; see &lt;<xsl:value-of select="name(parent::*)"/> id="<xsl:value-of select="parent::*/@id"/>"&gt;. Please move the Oxygen PIs inside the &lt;title&gt; element </xsl:comment>
					<!-- since <title> cannot have @status to indicate that it's been inserted, wrap
                    all the content of <title> in <ph> and mark @status="new" -->
					<!-- NOTE the @outputclass used here -->
					<xsl:element name="{$change_mark_element}">
						<xsl:attribute name="data">888</xsl:attribute>
						<xsl:attribute name="outputclass">change_wrapper</xsl:attribute>
						<xsl:attribute name="status">new</xsl:attribute>
						<xsl:apply-templates/>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
		<xsl:if test="($determine_insert = 'insert A' or $determine_insert = 'insert B' or $determine_insert = 'insert C' or $determine_insert = 'insert D') and
			not($non-DITA_element = '')">
			<xsl:copy-of select="following-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_end']" />
		</xsl:if>		
	</xsl:template>

	<xsl:template match="text()">
		<xsl:variable name="preceding-sibling_insert_start_count" select="count(preceding-sibling::processing-instruction('oxy_insert_start'))"/>
		<xsl:variable name="preceding-sibling_insert_end_count" select="count(preceding-sibling::processing-instruction('oxy_insert_end'))"/>
		<xsl:variable name="following-sibling_insert_start_count" select="count(following-sibling::processing-instruction('oxy_insert_start'))"/>
		<xsl:variable name="following-sibling_insert_end_count" select="count(following-sibling::processing-instruction('oxy_insert_end'))"/>
		<xsl:variable name="determine_insert">
			<xsl:choose>
				<!-- A. when there are 0 start insert and 0 end insert PIs on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 0 and $preceding-sibling_insert_end_count = 0">no PIs no insert</xsl:when>
				<!-- B. when there is 1 start insert PI on the preceding-sibling axis and 0 end insert PI on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 1 and $preceding-sibling_insert_end_count = 0">
					<xsl:choose>
						<!-- when there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="count(following-sibling::processing-instruction('oxy_insert_end')) >= 1">insert</xsl:when>
						<xsl:otherwise>
							<xsl:comment>WARNING 1: preceding-sibling start PI is lost</xsl:comment>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!-- C. when there is 0 start insert PI on the preceding-sibling axis and 1 end insert PI on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count = 0 and $preceding-sibling_insert_end_count = 1">
					<xsl:comment>WARNING 2: preceding-sibling end PI is lost</xsl:comment>
				</xsl:when>
				<!-- D. when there are 2 PIs on the preceding-sibling axis AND 1 is insert start and 1 is insert end -->
				<xsl:when test="$preceding-sibling_insert_start_count = 1 and $preceding-sibling_insert_end_count = 1">
					<xsl:choose>
						<!-- when there end insert is first and the start insert is second -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start']">
							<xsl:choose>
								<xsl:when test="following-sibling::processing-instruction('oxy_insert_end')">preceding-sibling end PI is lost; insert B</xsl:when>
								<xsl:otherwise>The preceding-sibling start insert and end insert PIs are lost</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<!-- when there start insert is first and the end insert is second -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_end']">this content comes after the previously inserted content; do nothing</xsl:when>
					</xsl:choose>
				</xsl:when>
				<!-- E. when there are 2 PIs on the preceding-sibling axis and both are insert start -->
				<xsl:when test="$preceding-sibling_insert_start_count = 2 and $preceding-sibling_insert_end_count = 0">
					<xsl:choose>
						<!-- when there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="following-sibling::processing-instruction('oxy_insert_end')">insert C</xsl:when>
						<xsl:otherwise>WARNING 3: 2 preceding-sibling insert start PIs are lost</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!-- F. when there are 2 PIs on the preceding-sibling axis and both are insert end -->
				<xsl:when test="$preceding-sibling_insert_end_count = 2 and $preceding-sibling_insert_start_count = 0">WARNING 4: 2 preceding-sibling insert end PIs are lost</xsl:when>
				<!-- G. when there are 3+ insert start/insert end PIs on the preceding-sibling axis -->
				<xsl:when test="$preceding-sibling_insert_start_count >= 2 and $preceding-sibling_insert_end_count >= 1">
					<xsl:choose>
						<!-- when the immediately precedding sibling PI is insert start and there is at least 1 end insert PI on the following sibling axis -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and following-sibling::processing-instruction('oxy_insert_end')">insert D</xsl:when>
						<!-- when the immediately precedding sibling PI is insert start and the immediately following PI on the following sibling axis is also insert start-->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and following-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start']">Warning 5: Either the preceding insert start or the following insert start is lost</xsl:when>
						<!-- when the immediately precedding sibling PI is insert start and there are no insert end PIs on the following sibling axis -->
						<xsl:when test="preceding-sibling::processing-instruction()[position() = 1][name() = 'oxy_insert_start'] and not(following-sibling::processing-instruction()[name() = 'oxy_insert_end'])">Warning 6: Either the preceding insert start or the following insert start is lost</xsl:when>
						<xsl:otherwise>WARNING ^^: preceding-sibling insert start PI is lost</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>well, hmm 2</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$determine_insert = 'insert' and ancestor::d4pSimpleEnumerator">
				<text status="new">
					<xsl:value-of select="."/>
				</text>
			</xsl:when>
			<xsl:when test="$determine_insert = 'insert'">
				<ph outputclass="change_wrapper" status="new">
					<xsl:value-of select="."/>
				</ph>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="processing-instruction('oxy_delete')">
		<!-- the delete PI -->
		<xsl:variable name="delete_node" select="."/>
		<!-- the contet of the delete PI following "content=" -->		
		<xsl:variable name="delete_node_content_1" select="substring-after($delete_node, 'content=')"/>
		<!-- does the delete contain an element (or only content)? -->
		<xsl:variable name="element_delete">
			<xsl:choose>
				<xsl:when test="contains($delete_node_content_1, '&amp;lt;')">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--       <xsl:message>First character: <xsl:value-of select="substring($delete_node_content_1, 2, 4)"/></xsl:message>-->
		<!-- does the delete start with an element? -->
		<xsl:variable name="element_delete_begins_with_element_or_not">
			<xsl:choose>
				<xsl:when test="substring($delete_node_content_1, 2, 4) = '&amp;lt;'">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- if the delete starts with an element what is the element name? -->
		<xsl:variable name="delete_node_element_name">
			<xsl:choose>
				<xsl:when test="$element_delete = 'yes'">
					<xsl:value-of select="substring-before(substring-after($delete_node, '&amp;lt;'), ' ')"/>
				</xsl:when>
				<xsl:when test="$element_delete = 'no'">no name</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="delete_node_element_name_length_plus_7" select="string-length($delete_node_element_name) + 7"/>		
		<xsl:variable name="delete_node_attributes">
			<xsl:choose>
				<xsl:when test="$element_delete_begins_with_element_or_not = 'yes'">	
					<xsl:value-of select="substring-before(substring($delete_node_content_1, $delete_node_element_name_length_plus_7), '&amp;quot;&amp;gt;')"/>
				</xsl:when>
				<xsl:when test="$element_delete_begins_with_element_or_not = 'no'">no attributes</xsl:when>
			</xsl:choose>
		</xsl:variable>		
		<!-- the first person who deleted -->
		<xsl:variable name="deleter_1" select="substring-after($delete_node, 'author=')"/>
		<xsl:variable name="deleter_2" select="substring-before(substring-after($deleter_1, $quote), $quote)"/>
		<!-- is there also a comment node along with the delete? -->
		<xsl:variable name="delete_comment">
			<xsl:choose>
				<xsl:when test="contains($delete_node_content_1, 'comment=')">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- first comment node -->
		<xsl:variable name="delete_node_comment_1">
			<xsl:choose>
				<xsl:when test="$delete_comment = 'yes'">
					<xsl:value-of select="substring-after($delete_node, 'comment=')"/>
				</xsl:when>
				<xsl:when test="$delete_comment = 'no'">no comment</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<!-- second comment node -->
		<xsl:variable name="delete_node_comment_2">
			<xsl:choose>
				<xsl:when test="$delete_comment = 'yes'">
					<xsl:value-of select="substring-before(substring-after($delete_node_comment_1, $quote), $quote)"/>
				</xsl:when>
				<xsl:when test="$delete_comment = 'no'">no comment</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<!-- process the deleted content -->
<!--	<xsl:message>Delete content: <xsl:value-of select="$delete_node_content_1"/></xsl:message>
        <xsl:message>Delete element name: <xsl:value-of select="$delete_node_element_name"/></xsl:message>
        <xsl:message>Delete element_delete_begins_with_element_or_not: <xsl:value-of select="$element_delete_begins_with_element_or_not"/></xsl:message>       -->
		<xsl:choose>
			<!-- when the deleted content should be output -->
			<xsl:when test="$retain_deleted_content = 'true'"><xsl:message>123</xsl:message>
						<xsl:choose>
							<!-- when the delete is an element (not content) -->
							<xsl:when test="$element_delete_begins_with_element_or_not = 'yes'"><xsl:message>456</xsl:message>
								<xsl:choose>
									<!-- when the deleted element also has attributes -->
									<xsl:when test="not($delete_node_attributes ='no attributes')"><xsl:message>789</xsl:message>
										<xsl:choose>
											<!-- when the deleted element is not DITA (i.e., is in a namespace) preserve the O2 delete PI -->
											<xsl:when test="contains($delete_node_attributes, 'xmlns:')">
												<xsl:copy-of select="."/>
											</xsl:when>
											<!-- otherwise output the element -->
											<xsl:otherwise><xsl:message>ABC</xsl:message>
												<xsl:call-template name="parse_deleted_element">
													<xsl:with-param name="deleted_element_content" select="$delete_node_content_1"/>
												</xsl:call-template>												
											</xsl:otherwise>
										</xsl:choose>										
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="parse_deleted_element">
											<xsl:with-param name="deleted_element_content" select="$delete_node_content_1"/>
										</xsl:call-template>										
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="parse_deleted_content">
									<xsl:with-param name="delete_node_content" select="$delete_node_content_1"/>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
			</xsl:when>
			<!-- when the deleted content should not be output -->
			<xsl:when test="$retain_deleted_content = 'false'"><xsl:comment>don't keep this</xsl:comment></xsl:when>
		</xsl:choose>
		<xsl:if test="$delete_comment = 'yes'">
			<xsl:element name="{$comment_element}">
				<xsl:value-of select="$deleter_2"/>
				<xsl:text>: </xsl:text>
				<xsl:value-of select="$delete_node_comment_2"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:variable name="ltz">&lt;</xsl:variable>
	<xsl:variable name="gtz">/&amp;gt;</xsl:variable>
	
	<!-- this template handles deleted content (not elements) which may or may not have one or more elemnts nested in it -->
	<xsl:template name="parse_deleted_content">
		<xsl:param name="delete_node_content"/>
		<xsl:param name="wrap_content">true</xsl:param>
		<xsl:param name="following_deleted_content_from_recursion">no_following_deleted_content_from_recursion</xsl:param>
		<xsl:variable name="initial_string" select="substring-before(substring-after($delete_node_content, $quote), $quote)"/>
		<xsl:choose>
			<!-- when deleted content contains element(s) -->
			<xsl:when test="contains($delete_node_content, '&amp;lt;')">
				<xsl:variable name="start_tag_1" select="substring-before(substring-after($initial_string, '&amp;lt;'), ' ')"/>
				<xsl:variable name="start_tag_2" select="substring-before(substring-after($initial_string, '&amp;lt;'), '&amp;gt;')"/>
				<xsl:variable name="start_tag_3" select="substring-before(substring-after($initial_string, '&amp;lt;'), '/&amp;gt;')"/>
				<xsl:variable name="start_tag">
					<xsl:choose>
						<xsl:when test="matches($start_tag_1, '^\c+$')">
							<xsl:value-of select="$start_tag_1"/>
						</xsl:when>
						<xsl:when test="matches($start_tag_2, '^\c+$')">
							<xsl:value-of select="$start_tag_2"/>
						</xsl:when>
						<xsl:when test="matches($start_tag_3, '^\c+$')">c-<xsl:value-of select="$start_tag_3"/></xsl:when>
						<xsl:when test="matches($start_tag_1, '^\C+$')">bad_char_1</xsl:when>
						<xsl:when test="matches($start_tag_2, '^\C+$')">bad_char_2</xsl:when>
						<xsl:when test="matches($start_tag_3, '^\C+$')">bad_char_3</xsl:when>
						<xsl:otherwise>well_hmmmm</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="attributes" select="substring-before(substring-after($initial_string, ' '), '&amp;gt;')"/>
				<!--				<xsl:variable name="rest_of_string" select="substring-after($initial_string, concat('&amp;lt;/', $start_tag_1, '&amp;gt;'))"/>-->
				<xsl:variable name="attributes" select="substring-before(substring-after($initial_string, ' '), '&amp;gt;')"/>
				<xsl:variable name="content_before_deleted_element">
					<xsl:choose>
						<xsl:when test="not(substring($initial_string, 1, 4) = '&amp;lt;')">
							<xsl:value-of select="substring-before($initial_string, '&amp;lt;')"/>
						</xsl:when>
						<xsl:otherwise>no_content_before_deleted_element</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="content_of_deleted_element" select="substring-before(substring-after($initial_string, '&amp;gt;'), concat('&amp;lt;/', $start_tag))"/>
				<xsl:variable name="content_after_deleted_element">
					<xsl:choose>
						<xsl:when test="not($following_deleted_content_from_recursion = 'no_following_deleted_content_from_recursion')">
							<xsl:value-of select="$following_deleted_content_from_recursion"/>
						</xsl:when>
						<xsl:when test="substring-after($initial_string, concat('&amp;lt;/', $start_tag, '&amp;gt;'))">
							<xsl:value-of select="substring-after($initial_string, concat('&amp;lt;/', $start_tag, '&amp;gt;'))"/>
						</xsl:when>
						<xsl:otherwise>no_content_after_deleted_element</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
<!--				<xsl:comment>DC delete node content: <xsl:value-of select="$delete_node_content"/></xsl:comment>
				<xsl:comment>DC initial string: <xsl:value-of select="$initial_string"/></xsl:comment>
				<xsl:comment>DC element: <xsl:value-of select="$start_tag"/></xsl:comment>
				<xsl:comment>DC content before deleted element: <xsl:value-of select="$content_before_deleted_element"/></xsl:comment>
				<xsl:comment>DC content of deleted element: <xsl:value-of select="$content_of_deleted_element"/></xsl:comment>
				<xsl:comment>DC content after deleted element: <xsl:value-of select="$content_after_deleted_element"/></xsl:comment>
				<xsl:comment>DC deleted content contains, but does not begin, with an element</xsl:comment>-->
				<!-- output the content that comes before the element -->
				<xsl:element name="{$change_mark_element}">
					<xsl:attribute name="data">222</xsl:attribute>
					<xsl:attribute name="status">deleted</xsl:attribute>
					<xsl:value-of select="$content_before_deleted_element"/>
				</xsl:element>
				<xsl:variable name="nested_element_name" select="substring-before(substring-after($initial_string, '&amp;lt;'), '&amp;gt')"/>
				<xsl:variable name="nested_element_start_tag" select="concat('&amp;lt;', $nested_element_name, '&amp;gt;')"/>
				<xsl:variable name="nested_element_close_tag" select="concat('&amp;lt;/', $nested_element_name, '&amp;gt;')"/>
				<xsl:variable name="nested_element_content" select="substring-before(substring-after($initial_string, concat('&amp;lt;', $nested_element_name, '&amp;gt;')), '&amp;lt')"/>
	<!--			<xsl:comment>DC nested_element_name: <xsl:value-of select="$nested_element_name"/></xsl:comment>
				<xsl:comment>DC nested_element_start_tag: <xsl:value-of select="$nested_element_start_tag"/></xsl:comment>
				<xsl:comment>DC nested_element_close_tag: <xsl:value-of select="$nested_element_close_tag"/></xsl:comment>
				<xsl:comment>DC nested_element_content: <xsl:value-of select="$nested_element_content"/></xsl:comment>-->
				<xsl:call-template name="parse_deleted_element">
					<xsl:with-param name="deleted_element_content" select="concat($quote, $nested_element_start_tag, $nested_element_content, $nested_element_close_tag, $quote)"/>
				</xsl:call-template>
				<!-- output the content that comes after the element IF any -->
				<xsl:if test="not($content_after_deleted_element = 'no_content_after_deleted_element')">
					<xsl:choose>
						<xsl:when test="contains($content_after_deleted_element, '&amp;lt;')">
							<xsl:choose>
								<xsl:when test="substring($content_after_deleted_element, 1, 4) = '&amp;lt;'">
									<xsl:variable name="nested_element_name" select="substring-before(substring-after($content_after_deleted_element, '&amp;lt;'), '&amp;gt')"/>
									<xsl:variable name="nested_element_start_tag" select="concat('&amp;lt;', $nested_element_name, '&amp;gt;')"/>
									<xsl:variable name="nested_element_close_tag" select="concat('&amp;lt;/', $nested_element_name, '&amp;gt;')"/>
									<xsl:call-template name="parse_deleted_element">
										<xsl:with-param name="deleted_element_content" select="concat($quote, $nested_element_start_tag, $content_after_deleted_element, $nested_element_close_tag, $quote)"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="parse_deleted_content">
										<xsl:with-param name="delete_node_content" select="concat($quote, $content_after_deleted_element, $quote)"/>
									</xsl:call-template>								
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$wrap_content = 'true'">
									<xsl:element name="{$change_mark_element}">
										<xsl:attribute name="outputclass">change_wrapper</xsl:attribute>
										<xsl:attribute name="status">deleted</xsl:attribute>
										<xsl:value-of select="$initial_string"/>
									</xsl:element>						
								</xsl:when>
								<xsl:when test="$wrap_content = 'false'">
									<xsl:value-of select="$initial_string"/>
								</xsl:when>					
							</xsl:choose>							
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:when>
			<!-- when deleted content does not contain elements -->
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$wrap_content = 'true' and ancestor::d4pSimpleEnumerator">
						<xsl:element name="text">
							<!--<xsl:attribute name="outputclass">change_wrapper_4</xsl:attribute>-->							
							<xsl:attribute name="status">deleted</xsl:attribute>
							<xsl:value-of select="$initial_string"/>
						</xsl:element>						
					</xsl:when>
					<xsl:when test="$wrap_content = 'true'">
						<xsl:element name="{$change_mark_element}">
							<xsl:attribute name="outputclass">change_wrapper</xsl:attribute>							
							<xsl:attribute name="status">deleted</xsl:attribute>
							<xsl:value-of select="$initial_string"/>
						</xsl:element>						
					</xsl:when>
					<xsl:when test="$wrap_content = 'false'">
							<xsl:value-of select="$initial_string"/>
					</xsl:when>					
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- this template handles deleted elements (not content) -->
	<xsl:template name="parse_deleted_element">
		<xsl:param name="deleted_element_content"/>
		<xsl:param name="following_deleted_content_from_recursion">no_following_deleted_content_from_recursion</xsl:param>
		<xsl:variable name="deleted_content_quotes_removed">
			<xsl:choose>
				<xsl:when test="starts-with($deleted_element_content, $quote)">
					<xsl:value-of select="substring-before(substring-after($deleted_element_content, $quote), $quote)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$deleted_element_content"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="xml" select="replace($deleted_content_quotes_removed, '&amp;lt;', '&lt;')"/>
		<xsl:variable name="xml2" select="replace($xml, '&amp;gt;', '>')"/>
		<xsl:variable name="xml3" select="replace($xml2, '&amp;quot;', '&quot;')"/>
		<xsl:variable name="xml4" >
			<root><xsl:copy-of select="$xml3"/></root>
		</xsl:variable>
		<xsl:variable name="final" select="saxon:parse(normalize-space($xml4))"/>	
<!--		<xsl:message>00:<xsl:value-of select="substring-before(substring-after($deleted_element_content, $quote), $quote)"/></xsl:message>
		<xsl:message>#! <xsl:value-of select="$deleted_element_content"/></xsl:message>
		<xsl:message>1:<xsl:copy-of select="$xml"/></xsl:message>
		<xsl:message>2:<xsl:copy-of select="$xml2"/></xsl:message>
		<xsl:message>3:<xsl:copy-of select="$xml3"/></xsl:message>
		<xsl:message>4:<xsl:copy-of select="$xml4"/></xsl:message>-->
		<xsl:apply-templates select="$final/*" mode="deleted"/>		
<!--		<xsl:message>^^^<xsl:apply-templates select="$final/*" mode="deleted"/>	</xsl:message>-->
	</xsl:template>
	
	<xsl:template match="*" mode="deleted">
		<xsl:copy>
			<xsl:attribute name="status">deleted</xsl:attribute>
			<xsl:value-of select="."/>
		</xsl:copy>
	</xsl:template>
	
	
<?remove	
	<xsl:template name="parse_deleted_element">
		<xsl:param name="deleted_element_content"/>
		<xsl:param name="following_deleted_content_from_recursion">no_following_deleted_content_from_recursion</xsl:param>
		<xsl:variable name="initial_string" select="substring-before(substring-after($deleted_element_content, $quote), $quote)"/>
		<xsl:comment>DE start parse_deleted_element template</xsl:comment>
		<xsl:variable name="start_tag_1" select="substring-before(substring-after($initial_string, '&amp;lt;'), ' ')"/>
		<xsl:variable name="start_tag_2" select="substring-before(substring-after($initial_string, '&amp;lt;'), '&amp;gt;')"/>
		<xsl:variable name="start_tag_3" select="substring-before(substring-after($initial_string, '&amp;lt;'), '/&amp;gt;')"/>
		<xsl:variable name="deleted_tag">
			<xsl:choose>
				<xsl:when test="matches($start_tag_1, '^\c+$')">
					<xsl:value-of select="$start_tag_1"/>
				</xsl:when>
				<xsl:when test="matches($start_tag_2, '^\c+$')">
					<xsl:value-of select="$start_tag_2"/>
				</xsl:when>
				<xsl:when test="matches($start_tag_3, '^\c+$')">c-<xsl:value-of select="$start_tag_3"/></xsl:when>
				<xsl:when test="matches($start_tag_1, '^\C+$')">bad_char_1</xsl:when>
				<xsl:when test="matches($start_tag_2, '^\C+$')">bad_char_2</xsl:when>
				<xsl:when test="matches($start_tag_3, '^\C+$')">bad_char_3</xsl:when>
				<xsl:otherwise>well_hmmmm</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="attributes" select="substring-before(substring-after($initial_string, ' '), '&amp;gt;')"/>

		<xsl:variable name="content_of_deleted_element" select="substring-before(substring-after($initial_string, '&amp;gt;'), concat('&amp;lt;/', $deleted_tag))"/>
		<xsl:variable name="content_after_deleted_element">
			<xsl:choose>
				<xsl:when test="not($following_deleted_content_from_recursion = 'no_following_deleted_content_from_recursion')">
					<xsl:value-of select="$following_deleted_content_from_recursion"/>
				</xsl:when>
				<xsl:when test="substring-after($initial_string, concat('&amp;lt;/', $deleted_tag, '&amp;gt;'))">
					<xsl:value-of select="substring-after($initial_string, concat('&amp;lt;/', $deleted_tag, '&amp;gt;'))"/>
				</xsl:when>
				<xsl:otherwise>no_content_after_deleted_element</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:comment>deleted_element content: <xsl:value-of select="$deleted_element_content"/></xsl:comment>
		<xsl:comment>initial string: <xsl:value-of select="$initial_string"/></xsl:comment>
		<xsl:comment>element: <xsl:value-of select="$deleted_tag"/></xsl:comment>

		<xsl:comment>content of deleted element: <xsl:value-of select="$content_of_deleted_element"/></xsl:comment>
		<xsl:comment>content after deleted element: <xsl:value-of select="$content_after_deleted_element"/></xsl:comment>
		<xsl:choose>
			<xsl:when test="$deleted_tag = ''">
				<xsl:comment>Empty string for tag:<xsl:value-of select="$deleted_tag"/>NO GOOD</xsl:comment>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="{$deleted_tag}">
					<xsl:attribute name="data">buga</xsl:attribute>					
					<xsl:attribute name="status">deleted</xsl:attribute>
					<xsl:choose>
						<xsl:when test="contains($content_of_deleted_element, '&amp;lt;')">
							<xsl:comment>deleted element contains nested element: YES</xsl:comment>
									<xsl:variable name="nested_element_name" select="substring-before(substring-after($content_of_deleted_element, '&amp;lt;'), '&amp;gt')"/>
									<xsl:variable name="nested_element_start_tag" select="concat('&amp;lt;', $nested_element_name, '&amp;gt;')"/>
									<xsl:variable name="nested_element_close_tag" select="concat('&amp;lt;/', $nested_element_name, '&amp;gt;')"/>
									<xsl:variable name="nested_element_content" select="substring-before(substring-after($content_of_deleted_element, concat('&amp;lt;', $nested_element_name, '&amp;gt;')), '&amp;lt')"/>
							<xsl:comment>X1 nested element content: <xsl:value-of select="$nested_element_content"/></xsl:comment>
								<xsl:choose>
									<xsl:when test="substring($content_of_deleted_element, 1, 4) = '&amp;lt;'">
										<xsl:comment>deleted content begins with element</xsl:comment>
										<xsl:call-template name="parse_deleted_element">
											<xsl:with-param name="deleted_element_content" select="concat($quote, $deleted_element_content, $quote)"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:comment>deleted content DOES NOT begin with element</xsl:comment>		
										<xsl:variable name="content_before_nested_element" select="substring-before($content_of_deleted_element, '&amp;lt;')" />
										<!-- output content of string that comes before first nested element -->
										<xsl:value-of select="$content_before_nested_element"/>							
										<!-- output content of nested element -->
										<!--									<xsl:comment>nested element name: <xsl:value-of select="$nested_element_name"/></xsl:comment>
									<xsl:comment>nested element content: <xsl:value-of select="$nested_element_content"/></xsl:comment>-->
										<xsl:call-template name="parse_deleted_element">
											<xsl:with-param name="deleted_element_content" select="concat($quote, $content_of_deleted_element, $quote)"/>
										</xsl:call-template>		
										<xsl:variable name="content_after_nested_element">
											<xsl:choose>
												<xsl:when test="substring-after($content_of_deleted_element, $nested_element_close_tag)">
													<xsl:value-of select="substring-after($content_of_deleted_element, $nested_element_close_tag)"/>
												</xsl:when>
												<xsl:otherwise>no_content_after_nested_elementABC</xsl:otherwise>
											</xsl:choose>								
										</xsl:variable>
										<xsl:comment>content after nested element: <xsl:value-of select="$content_after_nested_element"/></xsl:comment>			
										<xsl:choose>
											<xsl:when test="contains($content_after_nested_element, '&amp;lt;')"></xsl:when>
											<xsl:otherwise></xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
						<xsl:otherwise>
							<xsl:comment>mmm</xsl:comment>
							<xsl:value-of select="$content_of_deleted_element"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>				
			</xsl:otherwise>
		</xsl:choose>
		<xsl:comment>77 end parse_delete_element template</xsl:comment>
	</xsl:template>
	?>
	<xsl:template match="processing-instruction('oxy_comment_end')">
		<!-- match on the comment end PI because we want to output the comment content at the end of the comment location -->
		<!-- now change XPath context to the preceding comment start PI
            NOTE: can't use preceding-sibling axis here because the comment might span multiple elements -->
		<xsl:for-each select="preceding::processing-instruction()[name() = 'oxy_comment_start'][position() = 1]">
			<xsl:variable name="commenter" select="substring-before(substring-after(., 'author='), ' ')"/>
			<xsl:variable name="commenterlen" select="string-length($commenter) - 2"/>
			<xsl:variable name="commenter_name" select="substring($commenter, 2, $commenterlen)"/>
			<xsl:variable name="comment_content_1" select="substring-after(., 'comment=')"/>
			<xsl:variable name="comment_content_2" select="substring-before(substring-after($comment_content_1, $quote), $quote)"/>
			<xsl:element name="{$comment_element}"><xsl:value-of select="$commenter_name"/>:<xsl:text> </xsl:text><xsl:value-of select="$comment_content_2"/></xsl:element>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:copy>
			<xsl:value-of select="."/>
		</xsl:copy>
	</xsl:template>
	<!--    <xsl:template name="test_matching_insert_PI">
        <xsl:param name="test_for_PI"/>
        <xsl:param name="axis"/>
        
        <xsl:choose>
            <xsl:when test="$axis = 'preceding-sibling' and $test_for_PI = 'oxy_insert_start'">
                <xsl:for-each select="preceding-sibling::processing-instruction()[position()=1]">
                    <xsl:choose>
                        <xsl:when test="name() = 'oxy_insert_start'">match</xsl:when>
                        <xsl:when test="name() = 'oxy_insert_end'">no match</xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="test_matching_insert_PI">
                                <xsl:with-param name="test_for_PI"><xsl:value-of select="$test_for_PI"/></xsl:with-param>
                                <xsl:with-param name="axis"><xsl:value-of select="$axis"/></xsl:with-param>                                
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>                
            </xsl:when>
        </xsl:choose>
    </xsl:template>-->
</xsl:stylesheet>
