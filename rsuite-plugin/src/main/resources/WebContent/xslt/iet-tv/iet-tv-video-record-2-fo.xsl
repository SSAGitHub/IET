<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

    <xsl:output method="xml" version="1.0" indent="yes"/>

    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master page-height="297mm" page-width="210mm"
                    margin="5mm 25mm 5mm 25mm" master-name="PageMaster">
                    <fo:region-body margin="20mm 0mm 20mm 0mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="PageMaster">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="Video" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="KeyWordInfo
        | Speakers
        | Person
        | VideoPublish
        | Events
        | IETDigitalLibrary
        | Affiliation"
        priority="10">
        <xsl:call-template name="setHead"/>
    </xsl:template>

    
    <xsl:template
        match="BasicInfo
        | PublishInfo
        | SeriesList
        | VideoInspec
        | UploadVideo"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>


    <xsl:template
        match="BasicInfo/Title
        | BasicInfo/VideoCategory
        | BasicInfo/ShortDescription
        | BasicInfo/ChannelMapping
        "
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="KeyWordInfo/ChannelKeywordList
        | KeyWordInfo/CustomKeywordList
        "
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Events/Event" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="SeriesList/Series" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="VideoInspec/Kwd-group
        | VideoInspec/Inspec
        " priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Kwd-group/Kwd" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Inspec/AccessionNumber
        | Inspec/AccessionNumberForPublishedArticle"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="IETDigitalLibrary/EventNo
        | IETDigitalLibrary/DOI"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Person/Title
        | Person/Name
        | Person/Affiliations
        | Person/Organization"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Organization/OrganizationName
        | Organization/Department
        | Organization/Description
        | Organization/Address"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Department/DepartmentName" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Affiliation/Organization" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Address/Addr-Line
        | Address/Postcode
        | Address/City
        | Address/State
        | Address/Country
        | Address/Email"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Name/Given-Name
        | Name/Surname
        | Name/Suffix" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Location/LocationName
        | Location/LocationDate
        | Location/LocationDescription
        | Location/Address"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Event/EventName
        | Event/Location
        | Event/Venue
        | Event/StartDate
        | Event/StartTime"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Series/SeriesName
        | Series/Description" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="ChannelKeywordList/Channel" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="CustomKeywordList/CustomKeyword" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="Channel/ChannelName
        | Channel/Category
        | Channel/DOI
        | Channel/URL
        | Channel/KeywordList"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="KeywordList/DefaultKeyword" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template
        match="LivePublish/LiveRecordStartDate
        | LivePublish/LiveFinalStartDate
        | LivePublish/URL"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

<!--    <xsl:template match="Affiliations/Affiliation" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>
-->
    <xsl:template
        match="VideoPublish/RecordStartDate
        | VideoPublish/FinalStartDate
        | VideoPublish/URL
        | VideoPublish/DOI"
        priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="ChannelMapping/Channel" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="LocationDetails/Location" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="UploadVideo/File" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="UploadVideo/File/Duration" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="AdvanceInfo" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="AdvanceInfo/SubTitles" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="AdvanceInfo/SubTitles/Subtitle" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="AdvanceInfo/Transcripts" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="AdvanceInfo/Transcripts/Transcript" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="Language" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>

    <xsl:template match="InspecAbstract" priority="10">
        <xsl:call-template name="setBlock"/>
    </xsl:template>
    
    <!-- ************************************************************* -->

   
    <xsl:template name="setHead">
        <!--<xsl:variable name="nesting" select="count(ancestor::*) * 20"/>-->
        <xsl:choose>
            <xsl:when test="node()">
                <!--<xsl:variable name="nesting" select="count(ancestor::*) * 20"/>-->
                <fo:block start-indent="0pt">
                    <fo:inline color="blue">
                        <xsl:value-of select="name()"/>
                    </fo:inline>
                    <xsl:text>: </xsl:text>
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="setBlock">
        <xsl:choose>
            <xsl:when test="normalize-space(text()[1])">
                <!--<xsl:variable name="nesting" select="count(ancestor::*) * 20"/>-->
                <fo:block start-indent="0pt">
                    <fo:inline color="blue">
                        <xsl:value-of select="name()"/>
                    </fo:inline>
                    <xsl:text>: </xsl:text>
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*" priority="-1"/>


</xsl:stylesheet>
