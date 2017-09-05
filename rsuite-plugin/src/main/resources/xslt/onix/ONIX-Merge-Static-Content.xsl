<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0" xmlns:ONIX="http://ns.editeur.org/onix/3.0/reference"
	xmlns:r="http://www.rsuitecms.com/rsuite/ns/metadata"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"    
	exclude-result-prefixes="ONIX r">


	<xsl:output method="xml" indent="yes" />

	<xsl:param name="ONIX_default_template_uri" />
	<xsl:param name="ONIX_recipient_template_uri" />
	<xsl:param name="timeStamp" />
	<xsl:param name="messageNumber" />
	
	<xsl:variable name="ONIX_default_template" select="document($ONIX_default_template_uri)"
		as="node()" />
	<xsl:variable name="ONIX_recipient_template"
		select="document($ONIX_recipient_template_uri)" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>



	<xsl:template match="/ONIX:ONIXMessage"
		priority="10">
		<xsl:copy copy-namespaces="yes">
			<xsl:sequence select="@*" />
			<xsl:attribute name="xsi:noNamespaceSchemaLocation">ONIX_BookProduct_3.0_reference.xsd</xsl:attribute>
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/ONIX:ONIXMessage/ONIX:Header/ONIX:Addressee"
		priority="10">
		<xsl:copy copy-namespaces="yes">
			<xsl:sequence select="@*" />
			<xsl:apply-templates
				select="$ONIX_recipient_template/ONIX:ONIXMessage/ONIX:Header/ONIX:Addressee/*" />
		</xsl:copy>
		
		<xsl:if test="not(/ONIX:ONIXMessage/ONIX:Header/ONIX:MessageNumber)">
		<xsl:element name="MessageNumber" namespace="{namespace-uri()}" >
			<xsl:value-of select="$messageNumber"/>
		</xsl:element>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="/ONIX:ONIXMessage/ONIX:Header/ONIX:SentDateTime"
		priority="10">
		<xsl:copy copy-namespaces="yes">
			<xsl:sequence select="@*" />
			<xsl:value-of select="$timeStamp" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/ONIX:ONIXMessage/ONIX:Header/ONIX:Sender"
		priority="10">
		<xsl:copy copy-namespaces="yes">
			<xsl:sequence select="@*" />

			<xsl:apply-templates
				select="$ONIX_default_template/ONIX:ONIXMessage/ONIX:Header/ONIX:Sender/*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/ONIX:ONIXMessage/ONIX:Header/ONIX:MessageNumber"
		priority="10">
		<xsl:copy copy-namespaces="yes">
			<xsl:sequence select="@*" />
			<xsl:value-of select="$messageNumber"/>
		</xsl:copy>
	</xsl:template>


	<xsl:template match="*" priority="2">
		<xsl:element name="{name(.)}">
			<xsl:apply-templates select="@* | node()" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="*[namespace-uri() != '']" priority="5">
		<xsl:element name="{name(.)}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@* | node()" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@* | node()">
		<xsl:sequence select="." />
	</xsl:template>


	 <xsl:template match="processing-instruction()" />

</xsl:stylesheet>
