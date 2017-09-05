<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xmlns="http://www.crossref.org/schema/4.3.6">

    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="sqeunce_number" select="1"/>
    
    <xsl:param name="registrant" select="'IET'"/>
    <xsl:param name="depositor_name" select="'Sue Portwood'"/>
    <xsl:param name="email_address" select="'info@iet.tv'"/>
    <xsl:param name="parent_doi" select="'10.1049/iet-tv'"/>
    

    <xsl:template match="Video">
        <doi_batch version="4.3.6" xmlns="http://www.crossref.org/schema/4.3.6"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.crossref.org/schema/4.3.6 http://www.crossref.org/schema/crossref4.3.6.xsd">
            <xsl:call-template name="create-head-element"/>
            <xsl:call-template name="create-body-element"/>
        </doi_batch>
    </xsl:template>

    <xsl:template name="create-head-element">
        <xsl:variable name="now" select="current-dateTime()"/>
        <xsl:variable name="video_year" select="substring(/Video/BasicInfo/VideoCreatedDate,1,4)"/>
        <head>
            <doi_batch_id>
                <xsl:value-of
                    select="concat('IET-tv_Components_', $video_year, '_', $sqeunce_number)"/>
            </doi_batch_id>
            <timestamp>
                <xsl:value-of
                    select="format-dateTime($now, '[Y0001][M01][D01][M01][H01][m01][s01]')"/>
            </timestamp>
            <depositor>
                <depositor_name><xsl:value-of select="$depositor_name"/></depositor_name>
                <email_address><xsl:value-of select="$email_address"/></email_address>
            </depositor>
            <registrant><xsl:value-of select="$registrant"/></registrant>
        </head>
    </xsl:template>

    <xsl:template name="create-body-element">
        <xsl:variable name="speaker-name" select="/Video/Speakers/Person[1]/Name"/>

        <body>
            <sa_component parent_doi="{$parent_doi}">
                <component_list>
                    <component parent_relation="isPartOf">
                    <xsl:if test="$speaker-name">
                        <contributors>
                            <person_name sequence="first" contributor_role="author">
                                <given_name>
                                    <xsl:value-of select="$speaker-name/Given-Name[1]"/>
                                </given_name>
                                <surname>
                                    <xsl:value-of select="$speaker-name/Surname"/>
                                </surname>
                            </person_name>
                        </contributors>
                        </xsl:if>
                        <description>
                            <xsl:value-of select="/Video/BasicInfo/Title"/>
                        </description>
                        <format mime_type="multipart/mixed"/>
                        <doi_data>
                            <doi>
                                <xsl:value-of select="/Video/PublishInfo/VideoPublish/DOI"/>
                            </doi>
                            <resource>
                                <xsl:value-of select="/Video/VideoURL"/>
                            </resource>
                        </doi_data>
                    </component>
                </component_list>
            </sa_component>
        </body>
    </xsl:template>

</xsl:stylesheet>
