<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  exclude-result-prefixes="xlink mml">


  <!--<xsl:output method="xml" indent="no" encoding="UTF-8"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>-->


  <xsl:output doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd"
    encoding="UTF-8"/>
 
  <xsl:strip-space elements="*"/>

  <!-- Space is preserved in all elements allowing #PCDATA -->
  <xsl:preserve-space
    elements=""/>


 

  
  <!-- ============================================================= -->
  <!--  ROOT TEMPLATE - HANDLES HTML FRAMEWORK                       -->
  <!-- ============================================================= -->

  <xsl:template match="/">
    <html>
   <head>

     <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

     <title>ONIX Preview</title>

<style type="text/css">
 /* ONIX CSS */




            Addressee {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Addressee AddresseeIdentifier:before {
                            content: " Addressee Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Addressee AddresseeName:before {
                            content: oxy_label(text, " Addressee Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                            

                        Addressee AddresseeName:before {
                            content: oxy_label(text, " Addressee Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Addressee ContactName:before {
                            content: oxy_label(text, " Contact Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Addressee EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AddresseeIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        AddresseeIdentifier AddresseeIDType:before {
                            content: oxy_label(text, " Addressee ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AddresseeIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AddresseeIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AddresseeIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AddresseeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Affiliation {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AgentIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        AgentIdentifier AgentIDType:before {
                            content: oxy_label(text, " Agent ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AgentIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AgentIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AgentIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AgentName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AgentRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AlternativeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        AlternativeName NameType:before {
                            content: oxy_label(text, " Name Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NameIdentifier:before {
                            content: " Name Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        AlternativeName PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AlternativeName CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AncillaryContent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        AncillaryContent AncillaryContentType:before {
                            content: oxy_label(text, " Ancillary Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AncillaryContent AncillaryContentDescription:before {
                            content: " Ancillary Content Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        AncillaryContent Number:before {
                            content: oxy_label(text, " Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AncillaryContentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AncillaryContentDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            Audience {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Audience AudienceCodeType:before {
                            content: oxy_label(text, " Audience Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Audience AudienceCodeTypeName:before {
                            content: oxy_label(text, " Audience Code Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Audience AudienceCodeValue:before {
                            content: oxy_label(text, " Audience Code Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AudienceCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceCodeTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceCodeValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            AudienceRange {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        AudienceRange AudienceRangeQualifier:before {
                            content: oxy_label(text, " Audience Range Qualifier:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AudienceRange AudienceRangePrecision:before {
                            content: oxy_label(text, " Audience Range Precision:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AudienceRange AudienceRangePrecision:before {
                            content: oxy_label(text, " Audience Range Precision:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        AudienceRange AudienceRangeValue:before {
                            content: oxy_label(text, " Audience Range Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        AudienceRange AudienceRangeValue:before {
                            content: oxy_label(text, " Audience Range Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            AudienceRangePrecision {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceRangeQualifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            AudienceRangeValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Barcode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Barcode BarcodeType:before {
                            content: oxy_label(text, " Barcode Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Barcode PositionOnProduct:before {
                            content: oxy_label(text, " Position On Product:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            BarcodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BatchBonus {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        BatchBonus BatchQuantity:before {
                            content: oxy_label(text, " Batch Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        BatchBonus FreeQuantity:before {
                            content: oxy_label(text, " Free Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            BatchQuantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Bible {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Bible BibleContents:before {
                            content: oxy_label(text, " Bible Contents:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible BibleVersion:before {
                            content: oxy_label(text, " Bible Version:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible StudyBibleType:before {
                            content: oxy_label(text, " Study Bible Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible BiblePurpose:before {
                            content: oxy_label(text, " Bible Purpose:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible BibleTextOrganization:before {
                            content: oxy_label(text, " Bible Text Organization:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible BibleReferenceLocation:before {
                            content: oxy_label(text, " Bible Reference Location:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Bible BibleTextFeature:before {
                            content: oxy_label(text, " Bible Text Feature:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            BibleContents {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BiblePurpose {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BibleReferenceLocation {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BibleTextFeature {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BibleTextOrganization {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BibleVersion {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            BiographicalNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            BookClubAdoption {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            CBO {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CitationNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            CitedContent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CitedContent CitedContentType:before {
                            content: oxy_label(text, " Cited Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CitedContent ContentAudience:before {
                            content: oxy_label(text, " Content Audience:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CitedContent SourceType:before {
                            content: oxy_label(text, " Source Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CitedContent SourceTitle:before {
                            content: oxy_label(text, " Source Title:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CitedContent ListName:before {
                            content: oxy_label(text, " List Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CitedContent PositionOnList:before {
                            content: oxy_label(text, " Position On List:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CitedContent CitationNote:before {
                            content: " Citation Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        CitedContent ResourceLink:before {
                            content: oxy_label(text, " Resource Link:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CitedContent ContentDate:before {
                            content: " Content Date ";
                            font-weight: bold;
                            
                            }
                        
                         
            CitedContentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CityOfPublication {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CollateralDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CollateralDetail TextContent:before {
                            content: " Text Content ";
                            font-weight: bold;
                            
                            }
                        
                        CollateralDetail CitedContent:before {
                            content: " Cited Content ";
                            font-weight: bold;
                            
                            }
                        
                        CollateralDetail SupportingResource:before {
                            content: " Supporting Resource ";
                            font-weight: bold;
                            
                            }
                        
                        CollateralDetail Prize:before {
                            content: " Prize ";
                            font-weight: bold;
                            
                            }
                        
                         
            Collection {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Collection CollectionType:before {
                            content: oxy_label(text, " Collection Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Collection SourceName:before {
                            content: oxy_label(text, " Source Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Collection CollectionIdentifier:before {
                            content: " Collection Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Collection CollectionSequence:before {
                            content: " Collection Sequence ";
                            font-weight: bold;
                            
                            }
                        
                        Collection TitleDetail:before {
                            content: " Title Detail ";
                            font-weight: bold;
                            
                            }
                        
                        Collection Contributor:before {
                            content: " Contributor ";
                            font-weight: bold;
                            
                            }
                        
                        Collection ContributorStatement:before {
                            content: " Contributor Statement: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            CollectionIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CollectionIdentifier CollectionIDType:before {
                            content: oxy_label(text, " Collection ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CollectionIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CollectionIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            CollectionIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CollectionSequence {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CollectionSequence CollectionSequenceType:before {
                            content: oxy_label(text, " Collection Sequence Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CollectionSequence CollectionSequenceTypeName:before {
                            content: oxy_label(text, " Collection Sequence Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CollectionSequence CollectionSequenceNumber:before {
                            content: oxy_label(text, " Collection Sequence Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            CollectionSequenceNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CollectionSequenceType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CollectionSequenceTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CollectionType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ComparisonProductPrice {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ComparisonProductPrice ProductIdentifier:before {
                            content: " Product Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        ComparisonProductPrice PriceType:before {
                            content: oxy_label(text, " Price Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ComparisonProductPrice PriceAmount:before {
                            content: oxy_label(text, " Price Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ComparisonProductPrice CurrencyCode:before {
                            content: oxy_label(text, " Currency Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            Complexity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Complexity ComplexitySchemeIdentifier:before {
                            content: oxy_label(text, " Complexity Scheme Identifier:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Complexity ComplexityCode:before {
                            content: oxy_label(text, " Complexity Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ComplexityCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ComplexitySchemeIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ComponentNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ComponentTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Conference {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Conference ConferenceRole:before {
                            content: oxy_label(text, " Conference Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Conference ConferenceName:before {
                            content: " Conference Name: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Conference ConferenceAcronym:before {
                            content: oxy_label(text, " Conference Acronym:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Conference ConferenceNumber:before {
                            content: oxy_label(text, " Conference Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Conference ConferenceTheme:before {
                            content: " Conference Theme: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Conference ConferenceDate:before {
                            content: oxy_label(text, " Conference Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Conference ConferencePlace:before {
                            content: oxy_label(text, " Conference Place:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Conference ConferenceSponsor:before {
                            content: " Conference Sponsor ";
                            font-weight: bold;
                            
                            }
                        
                        Conference Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                         
            ConferenceAcronym {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferenceDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferenceName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            ConferenceNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferencePlace {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferenceRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferenceSponsor {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ConferenceSponsor ConferenceSponsorIdentifier:before {
                            content: " Conference Sponsor Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        ConferenceSponsor PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ConferenceSponsor PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ConferenceSponsor CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ConferenceSponsor CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ConferenceSponsorIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ConferenceSponsorIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ConferenceSponsorIdentifier ConferenceSponsorIDType:before {
                            content: oxy_label(text, " Conference Sponsor ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ConferenceSponsorIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ConferenceSponsorIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ConferenceTheme {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            ContactName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContentAudience {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContentDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ContentDate ContentDateRole:before {
                            content: oxy_label(text, " Content Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContentDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContentDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ContentDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContentDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ContentDetail ContentItem:before {
                            content: " Content Item ";
                            font-weight: bold;
                            
                            }
                        
                         
            ContentItem {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ContentItem LevelSequenceNumber:before {
                            content: oxy_label(text, " Level Sequence Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ContentItem TextItem:before {
                            content: " Text Item ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem ComponentTypeName:before {
                            content: oxy_label(text, " Component Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ContentItem ComponentNumber:before {
                            content: oxy_label(text, " Component Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ContentItem TitleDetail:before {
                            content: " Title Detail ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem Contributor:before {
                            content: " Contributor ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem Subject:before {
                            content: " Subject ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem NameAsSubject:before {
                            content: " Name As Subject ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem TextContent:before {
                            content: " Text Content ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem CitedContent:before {
                            content: " Cited Content ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem SupportingResource:before {
                            content: " Supporting Resource ";
                            font-weight: bold;
                            
                            }
                        
                        ContentItem RelatedWork:before {
                            content: " Related Work ";
                            font-weight: bold;
                            
                            }
                        
                         
            Contributor {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Contributor SequenceNumber:before {
                            content: oxy_label(text, " Sequence Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor ContributorRole:before {
                            content: oxy_label(text, " Contributor Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Contributor FromLanguage:before {
                            content: oxy_label(text, " From Language:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Contributor ToLanguage:before {
                            content: oxy_label(text, " To Language:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Contributor NameType:before {
                            content: oxy_label(text, " Name Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Contributor NameIdentifier:before {
                            content: " Name Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Contributor AlternativeName:before {
                            content: " Alternative Name ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor ContributorDate:before {
                            content: " Contributor Date ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor ContributorDate:before {
                            content: " Contributor Date ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor ProfessionalAffiliation:before {
                            content: " Professional Affiliation ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor ProfessionalAffiliation:before {
                            content: " Professional Affiliation ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor BiographicalNote:before {
                            content: " Biographical Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Contributor BiographicalNote:before {
                            content: " Biographical Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Contributor Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                        Contributor ContributorDescription:before {
                            content: " Contributor Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Contributor ContributorDescription:before {
                            content: " Contributor Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Contributor UnnamedPersons:before {
                            content: oxy_label(text, " Unnamed Persons:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Contributor ContributorPlace:before {
                            content: " Contributor Place ";
                            font-weight: bold;
                            
                            }
                        
                         
            ContributorDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ContributorDate ContributorDateRole:before {
                            content: oxy_label(text, " Contributor Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContributorDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContributorDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ContributorDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContributorDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            ContributorPlace {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ContributorPlace ContributorPlaceRelator:before {
                            content: oxy_label(text, " Contributor Place Relator:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContributorPlace CountryCode:before {
                            content: oxy_label(text, " Country Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContributorPlace RegionCode:before {
                            content: oxy_label(text, " Region Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ContributorPlace RegionCode:before {
                            content: oxy_label(text, " Region Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            ContributorPlaceRelator {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContributorRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ContributorStatement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            CopiesSold {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            CopyrightOwner {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CopyrightOwner CopyrightOwnerIdentifier:before {
                            content: " Copyright Owner Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        CopyrightOwner PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CopyrightOwner PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CopyrightOwner CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CopyrightOwner CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            CopyrightOwnerIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CopyrightOwnerIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CopyrightOwnerIdentifier CopyrightOwnerIDType:before {
                            content: oxy_label(text, " Copyright Owner ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        CopyrightOwnerIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CopyrightOwnerIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            CopyrightStatement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        CopyrightStatement CopyrightYear:before {
                            content: oxy_label(text, " Copyright Year:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        CopyrightStatement CopyrightOwner:before {
                            content: " Copyright Owner ";
                            font-weight: bold;
                            
                            }
                        
                         
            CopyrightYear {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CorporateName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CorporateNameInverted {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CountriesIncluded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CountriesExcluded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CountryCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CountryOfManufacture {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CountryOfPublication {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CurrencyCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            CurrencyZone {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Date {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DateFormat {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DefaultCurrencyCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DefaultLanguageOfText {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DefaultPriceType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DeletionText {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DescriptiveDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        DescriptiveDetail ProductComposition:before {
                            content: oxy_label(text, " Product Composition:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductForm:before {
                            content: oxy_label(text, " Product Form:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductFormDetail:before {
                            content: oxy_label(text, " Product Form Detail:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductFormFeature:before {
                            content: " Product Form Feature ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail ProductPackaging:before {
                            content: oxy_label(text, " Product Packaging:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductFormDescription:before {
                            content: oxy_label(text, " Product Form Description:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail TradeCategory:before {
                            content: oxy_label(text, " Trade Category:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail PrimaryContentType:before {
                            content: oxy_label(text, " Primary Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductContentType:before {
                            content: oxy_label(text, " Product Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail Measure:before {
                            content: " Measure ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail CountryOfManufacture:before {
                            content: oxy_label(text, " Country Of Manufacture:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail EpubTechnicalProtection:before {
                            content: oxy_label(text, " Epub Technical Protection:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail EpubUsageConstraint:before {
                            content: " Epub Usage Constraint ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail MapScale:before {
                            content: oxy_label(text, " Map Scale:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ProductClassification:before {
                            content: " Product Classification ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail ProductPart:before {
                            content: " Product Part ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Collection:before {
                            content: " Collection ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail NoCollection:before {
                            content: " No Collection ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail TitleDetail:before {
                            content: " Title Detail ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail ThesisType:before {
                            content: oxy_label(text, " Thesis Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ThesisPresentedTo:before {
                            content: oxy_label(text, " Thesis Presented To:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail ThesisYear:before {
                            content: oxy_label(text, " Thesis Year:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail Contributor:before {
                            content: " Contributor ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail ContributorStatement:before {
                            content: " Contributor Statement: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        DescriptiveDetail NoContributor:before {
                            content: " No Contributor ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Conference:before {
                            content: " Conference ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail EditionType:before {
                            content: oxy_label(text, " Edition Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail EditionNumber:before {
                            content: oxy_label(text, " Edition Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail EditionVersionNumber:before {
                            content: oxy_label(text, " Edition Version Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail EditionStatement:before {
                            content: oxy_label(text, " Edition Statement:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail NoEdition:before {
                            content: " No Edition ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail ReligiousText:before {
                            content: " Religious Text ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Language:before {
                            content: " Language ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Extent:before {
                            content: " Extent ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Illustrated:before {
                            content: oxy_label(text, " Illustrated:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail NumberOfIllustrations:before {
                            content: oxy_label(text, " Number Of Illustrations:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail IllustrationsNote:before {
                            content: " Illustrations Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        DescriptiveDetail AncillaryContent:before {
                            content: " Ancillary Content ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail Subject:before {
                            content: " Subject ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail NameAsSubject:before {
                            content: " Name As Subject ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail AudienceCode:before {
                            content: oxy_label(text, " Audience Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DescriptiveDetail Audience:before {
                            content: " Audience ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail AudienceRange:before {
                            content: " Audience Range ";
                            font-weight: bold;
                            
                            }
                        
                        DescriptiveDetail AudienceDescription:before {
                            content: " Audience Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        DescriptiveDetail Complexity:before {
                            content: " Complexity ";
                            font-weight: bold;
                            
                            }
                        
                         
            Discount {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Discount DiscountType:before {
                            content: oxy_label(text, " Discount Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Discount Quantity:before {
                            content: oxy_label(text, " Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Discount DiscountPercent:before {
                            content: oxy_label(text, " Discount Percent:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Discount DiscountAmount:before {
                            content: oxy_label(text, " Discount Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Discount DiscountAmount:before {
                            content: oxy_label(text, " Discount Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            DiscountAmount {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountCodeTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountCoded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        DiscountCoded DiscountCodeType:before {
                            content: oxy_label(text, " Discount Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        DiscountCoded DiscountCodeTypeName:before {
                            content: oxy_label(text, " Discount Code Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        DiscountCoded DiscountCode:before {
                            content: oxy_label(text, " Discount Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            EpubTechnicalProtection {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EditionNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EditionStatement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EditionType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EditionVersionNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EmailAddress {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EndDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EpubUsageConstraint {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        EpubUsageConstraint EpubUsageType:before {
                            content: oxy_label(text, " Epub Usage Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        EpubUsageConstraint EpubUsageStatus:before {
                            content: oxy_label(text, " Epub Usage Status:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        EpubUsageConstraint EpubUsageLimit:before {
                            content: " Epub Usage Limit ";
                            font-weight: bold;
                            
                            }
                        
                         
            EpubUsageLimit {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        EpubUsageLimit Quantity:before {
                            content: oxy_label(text, " Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        EpubUsageLimit EpubUsageUnit:before {
                            content: oxy_label(text, " Epub Usage Unit:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            EpubUsageStatus {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EpubUsageType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            EpubUsageUnit {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ExpectedDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Extent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Extent ExtentType:before {
                            content: oxy_label(text, " Extent Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Extent ExtentValue:before {
                            content: oxy_label(text, " Extent Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Extent ExtentValueRoman:before {
                            content: oxy_label(text, " Extent Value Roman:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Extent ExtentValueRoman:before {
                            content: oxy_label(text, " Extent Value Roman:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Extent ExtentUnit:before {
                            content: oxy_label(text, " Extent Unit:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            ExtentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ExtentUnit {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ExtentValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ExtentValueRoman {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            FaxNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            FeatureNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            FeatureValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            FirstPageNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            FreeQuantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            FromLanguage {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Header {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
            font-size: 18pt;                                        
            margin-top: 6pt; 
            margin-bottom: 18pt;                    
            border: thin solid black;
            font-style: italic;
            background-color: #FFFFEE;}
        Header Sender:before {
                            content: " Sender ";
                            font-weight: bold;
                            
                            }
                        
                        Header Addressee:before {
                            content: " Addressee ";
                            font-weight: bold;
                            
                            }
                        
                        Header MessageNumber:before {
                            content: oxy_label(text, " Message Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Header MessageRepeat:before {
                            content: oxy_label(text, " Message Repeat:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Header SentDateTime:before {
                            content: oxy_label(text, " Sent Date Time:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Header MessageNote:before {
                            content: oxy_label(text, " Message Note:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Header DefaultLanguageOfText:before {
                            content: oxy_label(text, " Default Language Of Text:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Header DefaultPriceType:before {
                            content: oxy_label(text, " Default Price Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Header DefaultCurrencyCode:before {
                            content: oxy_label(text, " Default Currency Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            IDTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            IDValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Illustrated {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            IllustrationsNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            Imprint {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Imprint ImprintIdentifier:before {
                            content: " Imprint Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Imprint ImprintName:before {
                            content: oxy_label(text, " Imprint Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Imprint ImprintName:before {
                            content: oxy_label(text, " Imprint Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ImprintIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ImprintIdentifier ImprintIDType:before {
                            content: oxy_label(text, " Imprint ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ImprintIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ImprintIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ImprintIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ImprintName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            InitialPrintRun {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            KeyNames {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Language {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Language LanguageRole:before {
                            content: oxy_label(text, " Language Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Language LanguageCode:before {
                            content: oxy_label(text, " Language Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Language CountryCode:before {
                            content: oxy_label(text, " Country Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Language ScriptCode:before {
                            content: oxy_label(text, " Script Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            LanguageCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LanguageRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LastPageNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LatestReprintNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LettersAfterNames {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LevelSequenceNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ListName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LocationIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        LocationIdentifier LocationIDType:before {
                            content: oxy_label(text, " Location ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        LocationIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        LocationIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            LocationIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            LocationName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MainSubject {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            }
         
            MapScale {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Market {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Market Territory:before {
                            content: " Territory ";
                            font-weight: bold;
                            
                            }
                        
                        Market SalesRestriction:before {
                            content: " Sales Restriction ";
                            font-weight: bold;
                            
                            }
                        
                         
            MarketDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        MarketDate MarketDateRole:before {
                            content: oxy_label(text, " Market Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        MarketDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        MarketDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            MarketDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MarketPublishingDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        MarketPublishingDetail PublisherRepresentative:before {
                            content: " Publisher Representative ";
                            font-weight: bold;
                            
                            }
                        
                        MarketPublishingDetail ProductContact:before {
                            content: " Product Contact ";
                            font-weight: bold;
                            
                            }
                        
                        MarketPublishingDetail MarketPublishingStatus:before {
                            content: oxy_label(text, " Market Publishing Status:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        MarketPublishingDetail MarketPublishingStatusNote:before {
                            content: " Market Publishing Status Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        MarketPublishingDetail MarketDate:before {
                            content: " Market Date ";
                            font-weight: bold;
                            
                            }
                        
                        MarketPublishingDetail PromotionCampaign:before {
                            content: " Promotion Campaign: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        MarketPublishingDetail PromotionContact:before {
                            content: " Promotion Contact: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        MarketPublishingDetail InitialPrintRun:before {
                            content: oxy_label(text, " Initial Print Run:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        MarketPublishingDetail ReprintDetail:before {
                            content: " Reprint Detail: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        MarketPublishingDetail CopiesSold:before {
                            content: " Copies Sold: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        MarketPublishingDetail BookClubAdoption:before {
                            content: " Book Club Adoption: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            MarketPublishingStatus {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MarketPublishingStatusNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            Measure {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Measure MeasureType:before {
                            content: oxy_label(text, " Measure Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Measure Measurement:before {
                            content: oxy_label(text, " Measurement:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Measure MeasureUnitCode:before {
                            content: oxy_label(text, " Measure Unit Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            Measurement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MeasureType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MeasureUnitCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MessageNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MessageNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MessageRepeat {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            MinimumOrderQuantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NameAsSubject {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        NameAsSubject NameType:before {
                            content: oxy_label(text, " Name Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NameIdentifier:before {
                            content: " Name Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        NameAsSubject PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PersonName:before {
                            content: oxy_label(text, " Person Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PersonNameInverted:before {
                            content: oxy_label(text, " Person Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesBeforeNames:before {
                            content: oxy_label(text, " Titles Before Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesBeforeKey:before {
                            content: oxy_label(text, " Names Before Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject PrefixToKey:before {
                            content: oxy_label(text, " Prefix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject KeyNames:before {
                            content: oxy_label(text, " Key Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject NamesAfterKey:before {
                            content: oxy_label(text, " Names After Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject SuffixToKey:before {
                            content: oxy_label(text, " Suffix To Key:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject LettersAfterNames:before {
                            content: oxy_label(text, " Letters After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject TitlesAfterNames:before {
                            content: oxy_label(text, " Titles After Names:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateName:before {
                            content: oxy_label(text, " Corporate Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameAsSubject CorporateNameInverted:before {
                            content: oxy_label(text, " Corporate Name Inverted:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            NameIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        NameIdentifier NameIDType:before {
                            content: oxy_label(text, " Name ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        NameIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NameIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            NameIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NamesAfterKey {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NamesBeforeKey {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NameType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NewSupplier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        NewSupplier SupplierIdentifier:before {
                            content: " Supplier Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        NewSupplier SupplierName:before {
                            content: oxy_label(text, " Supplier Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NewSupplier SupplierName:before {
                            content: oxy_label(text, " Supplier Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NewSupplier TelephoneNumber:before {
                            content: oxy_label(text, " Telephone Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NewSupplier FaxNumber:before {
                            content: oxy_label(text, " Fax Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        NewSupplier EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            NoCollection {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            }
         
            NoContributor {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            }
         
            NoEdition {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            }
         
            NotificationType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Number {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NumberOfCopies {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NumberOfIllustrations {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NumberOfItemsOfThisForm {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            NumberOfPages {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
        ONIXMessage:before {
            content: "ONIX Message 3.0 Reference";
            font-weight: bold;
            font-style: italic;
            font-size: 28pt;
            padding-bottom:20px;
            }
                         
            ONIXMessage {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
            font-weight: bold;
            font-style: italic;
            font-size: 28pt;
            font-style: italic;
            background-image: url(images/IET_logo.jpg);
            background-repeat: no-repeat;
            background-position: right top;}
        ONIXMessage Header:before {
                            content: " Header ";
                            font-weight: bold;
                            
                            }
                        
                        ONIXMessage Product:before {
                            content: " Product ";
                            font-weight: bold;
                            
                            }
                        
                         
            OnHand {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            OnOrder {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            OnOrderDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        OnOrderDetail OnOrder:before {
                            content: oxy_label(text, " On Order:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        OnOrderDetail ExpectedDate:before {
                            content: oxy_label(text, " Expected Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            OrderTime {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PackQuantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PageRun {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PageRun FirstPageNumber:before {
                            content: oxy_label(text, " First Page Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PageRun LastPageNumber:before {
                            content: oxy_label(text, " Last Page Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            PartNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Percent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountPercent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PersonName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PersonNameInverted {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PositionOnList {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PositionOnProduct {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrefixToKey {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Price {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Price PriceType:before {
                            content: oxy_label(text, " Price Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price PriceQualifier:before {
                            content: oxy_label(text, " Price Qualifier:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price PriceTypeDescription:before {
                            content: oxy_label(text, " Price Type Description:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Price PricePer:before {
                            content: oxy_label(text, " Price Per:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price PriceCondition:before {
                            content: " Price Condition ";
                            font-weight: bold;
                            
                            }
                        
                        Price MinimumOrderQuantity:before {
                            content: oxy_label(text, " Minimum Order Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Price BatchBonus:before {
                            content: " Batch Bonus ";
                            font-weight: bold;
                            
                            }
                        
                        Price DiscountCoded:before {
                            content: " Discount Coded ";
                            font-weight: bold;
                            
                            }
                        
                        Price Discount:before {
                            content: " Discount ";
                            font-weight: bold;
                            
                            }
                        
                        Price PriceStatus:before {
                            content: oxy_label(text, " Price Status:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price PriceAmount:before {
                            content: oxy_label(text, " Price Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Price PriceCoded:before {
                            content: " Price Coded ";
                            font-weight: bold;
                            
                            }
                        
                        Price Tax:before {
                            content: " Tax ";
                            font-weight: bold;
                            
                            }
                        
                        Price CurrencyCode:before {
                            content: oxy_label(text, " Currency Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price Territory:before {
                            content: " Territory ";
                            font-weight: bold;
                            
                            }
                        
                        Price CurrencyZone:before {
                            content: oxy_label(text, " Currency Zone:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price ComparisonProductPrice:before {
                            content: " Comparison Product Price ";
                            font-weight: bold;
                            
                            }
                        
                        Price PriceDate:before {
                            content: " Price Date ";
                            font-weight: bold;
                            
                            }
                        
                        Price PrintedOnProduct:before {
                            content: oxy_label(text, " Printed On Product:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Price PositionOnProduct:before {
                            content: oxy_label(text, " Position On Product:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            PriceAmount {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceCoded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PriceCoded PriceCodeType:before {
                            content: oxy_label(text, " Price Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PriceCoded PriceCodeTypeName:before {
                            content: oxy_label(text, " Price Code Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PriceCoded PriceCode:before {
                            content: oxy_label(text, " Price Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            PriceCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceCodeTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceCondition {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PriceCondition PriceConditionType:before {
                            content: oxy_label(text, " Price Condition Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PriceCondition PriceConditionQuantity:before {
                            content: " Price Condition Quantity ";
                            font-weight: bold;
                            
                            }
                        
                         
            PriceConditionQuantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PriceConditionQuantity PriceConditionQuantityType:before {
                            content: oxy_label(text, " Price Condition Quantity Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PriceConditionQuantity Quantity:before {
                            content: oxy_label(text, " Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PriceConditionQuantity QuantityUnit:before {
                            content: oxy_label(text, " Quantity Unit:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            PriceConditionQuantityType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceConditionType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PriceDate PriceDateRole:before {
                            content: oxy_label(text, " Price Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PriceDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PriceDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            PriceDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PricePer {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceQualifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceStatus {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PriceTypeDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrimaryContentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrimaryPart {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            }
         
            PrintedOnProduct {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Prize {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Prize PrizeName:before {
                            content: oxy_label(text, " Prize Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Prize PrizeYear:before {
                            content: oxy_label(text, " Prize Year:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Prize PrizeCountry:before {
                            content: oxy_label(text, " Prize Country:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Prize PrizeCode:before {
                            content: oxy_label(text, " Prize Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Prize PrizeJury:before {
                            content: " Prize Jury: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            PrizeCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrizeCountry {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrizeJury {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            PrizeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PrizeYear {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Product {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
            font-size: 18pt;                                        
            margin-top: 6pt; 
            margin-bottom: 18pt;                    
            border: thin solid black;
            font-style: italic;
            background-color: #EEEEEE;}
        Product RecordReference:before {
                            content: oxy_label(text, " Record Reference:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Product NotificationType:before {
                            content: oxy_label(text, " Notification Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Product DeletionText:before {
                            content: oxy_label(text, " Deletion Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Product RecordSourceType:before {
                            content: oxy_label(text, " Record Source Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Product RecordSourceIdentifier:before {
                            content: " Record Source Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Product RecordSourceName:before {
                            content: oxy_label(text, " Record Source Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Product ProductIdentifier:before {
                            content: " Product Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Product Barcode:before {
                            content: " Barcode ";
                            font-weight: bold;
                            
                            }
                        
                        Product DescriptiveDetail:before {
                            content: " Descriptive Detail ";
                            font-weight: bold;
                            
                            }
                        
                        Product CollateralDetail:before {
                            content: " Collateral Detail ";
                            font-weight: bold;
                            
                            }
                        
                        Product ContentDetail:before {
                            content: " Content Detail ";
                            font-weight: bold;
                            
                            }
                        
                        Product PublishingDetail:before {
                            content: " Publishing Detail ";
                            font-weight: bold;
                            
                            }
                        
                        Product RelatedMaterial:before {
                            content: " Related Material ";
                            font-weight: bold;
                            
                            }
                        
                        Product ProductSupply:before {
                            content: " Product Supply ";
                            font-weight: bold;
                            
                            }
                        
                         
            ProductAvailability {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductClassification {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductClassification ProductClassificationType:before {
                            content: oxy_label(text, " Product Classification Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductClassification ProductClassificationCode:before {
                            content: oxy_label(text, " Product Classification Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductClassification Percent:before {
                            content: oxy_label(text, " Percent:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProductClassificationCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductClassificationType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductComposition {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductContact {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductContact ProductContactRole:before {
                            content: oxy_label(text, " Product Contact Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductContact ProductContactIdentifier:before {
                            content: " Product Contact Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        ProductContact ProductContactName:before {
                            content: oxy_label(text, " Product Contact Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductContact ProductContactName:before {
                            content: oxy_label(text, " Product Contact Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductContact ContactName:before {
                            content: oxy_label(text, " Contact Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductContact EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProductContactIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductContactIdentifier ProductContactIDType:before {
                            content: oxy_label(text, " Product Contact ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductContactIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductContactIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProductContactIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductContactName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductContactRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductContentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductForm {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductFormDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductFormDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductFormFeature {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductFormFeature ProductFormFeatureType:before {
                            content: oxy_label(text, " Product Form Feature Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductFormFeature ProductFormFeatureValue:before {
                            content: oxy_label(text, " Product Form Feature Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductFormFeature ProductFormFeatureDescription:before {
                            content: oxy_label(text, " Product Form Feature Description:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProductFormFeatureDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductFormFeatureType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductFormFeatureValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductIdentifier ProductIDType:before {
                            content: oxy_label(text, " Product ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProductPackaging {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductPart {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductPart PrimaryPart:before {
                            content: " Primary Part ";
                            font-weight: bold;
                            
                            }
                        
                        ProductPart ProductIdentifier:before {
                            content: " Product Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        ProductPart ProductForm:before {
                            content: oxy_label(text, " Product Form:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductPart ProductFormDetail:before {
                            content: oxy_label(text, " Product Form Detail:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductPart ProductFormFeature:before {
                            content: " Product Form Feature ";
                            font-weight: bold;
                            
                            }
                        
                        ProductPart ProductFormDescription:before {
                            content: oxy_label(text, " Product Form Description:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductPart ProductContentType:before {
                            content: oxy_label(text, " Product Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ProductPart NumberOfItemsOfThisForm:before {
                            content: oxy_label(text, " Number Of Items Of This Form:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductPart NumberOfCopies:before {
                            content: oxy_label(text, " Number Of Copies:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductPart NumberOfCopies:before {
                            content: oxy_label(text, " Number Of Copies:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProductPart CountryOfManufacture:before {
                            content: oxy_label(text, " Country Of Manufacture:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            ProductRelationCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ProductSupply {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProductSupply Market:before {
                            content: " Market ";
                            font-weight: bold;
                            
                            }
                        
                        ProductSupply MarketPublishingDetail:before {
                            content: " Market Publishing Detail ";
                            font-weight: bold;
                            
                            }
                        
                        ProductSupply SupplyDetail:before {
                            content: " Supply Detail ";
                            font-weight: bold;
                            
                            }
                        
                         
            ProfessionalAffiliation {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ProfessionalAffiliation ProfessionalPosition:before {
                            content: oxy_label(text, " Professional Position:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProfessionalAffiliation Affiliation:before {
                            content: oxy_label(text, " Affiliation:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ProfessionalAffiliation Affiliation:before {
                            content: oxy_label(text, " Affiliation:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            ProfessionalPosition {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PromotionCampaign {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            PromotionContact {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            Publisher {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Publisher PublishingRole:before {
                            content: oxy_label(text, " Publishing Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Publisher PublisherIdentifier:before {
                            content: " Publisher Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Publisher PublisherName:before {
                            content: oxy_label(text, " Publisher Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Publisher PublisherName:before {
                            content: oxy_label(text, " Publisher Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Publisher Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                         
            PublisherIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PublisherIdentifier PublisherIDType:before {
                            content: oxy_label(text, " Publisher ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublisherIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            PublisherIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PublisherName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PublisherRepresentative {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PublisherRepresentative AgentRole:before {
                            content: oxy_label(text, " Agent Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative AgentIdentifier:before {
                            content: " Agent Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        PublisherRepresentative AgentName:before {
                            content: oxy_label(text, " Agent Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative AgentName:before {
                            content: oxy_label(text, " Agent Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative TelephoneNumber:before {
                            content: oxy_label(text, " Telephone Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative FaxNumber:before {
                            content: oxy_label(text, " Fax Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublisherRepresentative Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                         
            PublishingDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PublishingDate PublishingDateRole:before {
                            content: oxy_label(text, " Publishing Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublishingDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublishingDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            PublishingDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PublishingDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        PublishingDetail Imprint:before {
                            content: " Imprint ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail Publisher:before {
                            content: " Publisher ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail Publisher:before {
                            content: " Publisher ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail CityOfPublication:before {
                            content: oxy_label(text, " City Of Publication:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublishingDetail CountryOfPublication:before {
                            content: oxy_label(text, " Country Of Publication:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublishingDetail ProductContact:before {
                            content: " Product Contact ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail PublishingStatus:before {
                            content: oxy_label(text, " Publishing Status:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublishingDetail PublishingStatusNote:before {
                            content: " Publishing Status Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        PublishingDetail PublishingDate:before {
                            content: " Publishing Date ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail LatestReprintNumber:before {
                            content: oxy_label(text, " Latest Reprint Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        PublishingDetail CopyrightStatement:before {
                            content: " Copyright Statement ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail SalesRights:before {
                            content: " Sales Rights ";
                            font-weight: bold;
                            
                            }
                        
                        PublishingDetail ROWSalesRightsType:before {
                            content: oxy_label(text, " ROW Sales Rights Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        PublishingDetail SalesRestriction:before {
                            content: " Sales Restriction ";
                            font-weight: bold;
                            
                            }
                        
                         
            PublishingRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PublishingStatus {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            PublishingStatusNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            Quantity {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            QuantityUnit {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            DiscountType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RecordReference {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RecordSourceIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        RecordSourceIdentifier RecordSourceIDType:before {
                            content: oxy_label(text, " Record Source ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        RecordSourceIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        RecordSourceIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            RecordSourceIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RecordSourceName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RecordSourceType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RegionCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RegionsIncluded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            RegionsExcluded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Reissue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Reissue ReissueDate:before {
                            content: oxy_label(text, " Reissue Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Reissue ReissueDescription:before {
                            content: " Reissue Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Reissue Price:before {
                            content: " Price ";
                            font-weight: bold;
                            
                            }
                        
                        Reissue SupportingResource:before {
                            content: " Supporting Resource ";
                            font-weight: bold;
                            
                            }
                        
                         
            ReissueDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReissueDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            RelatedMaterial {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        RelatedMaterial RelatedWork:before {
                            content: " Related Work ";
                            font-weight: bold;
                            
                            }
                        
                        RelatedMaterial RelatedProduct:before {
                            content: " Related Product ";
                            font-weight: bold;
                            
                            }
                        
                         
            RelatedProduct {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        RelatedProduct ProductRelationCode:before {
                            content: oxy_label(text, " Product Relation Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        RelatedProduct ProductIdentifier:before {
                            content: " Product Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        RelatedProduct ProductForm:before {
                            content: oxy_label(text, " Product Form:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        RelatedProduct ProductFormDetail:before {
                            content: oxy_label(text, " Product Form Detail:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            RelatedWork {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        RelatedWork WorkRelationCode:before {
                            content: oxy_label(text, " Work Relation Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        RelatedWork WorkIdentifier:before {
                            content: " Work Identifier ";
                            font-weight: bold;
                            
                            }
                        
                         
            ReligiousText {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ReligiousText Bible:before {
                            content: " Bible ";
                            font-weight: bold;
                            
                            }
                        
                        ReligiousText ReligiousTextIdentifier:before {
                            content: oxy_label(text, " Religious Text Identifier:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ReligiousText ReligiousTextFeature:before {
                            content: " Religious Text Feature ";
                            font-weight: bold;
                            
                            }
                        
                         
            ReligiousTextFeature {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ReligiousTextFeature ReligiousTextFeatureType:before {
                            content: oxy_label(text, " Religious Text Feature Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ReligiousTextFeature ReligiousTextFeatureCode:before {
                            content: oxy_label(text, " Religious Text Feature Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ReligiousTextFeature ReligiousTextFeatureDescription:before {
                            content: " Religious Text Feature Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            ReligiousTextFeatureCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReligiousTextFeatureDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            ReligiousTextFeatureType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReligiousTextIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReprintDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            ResourceContentType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ResourceFeature {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ResourceFeature ResourceFeatureType:before {
                            content: oxy_label(text, " Resource Feature Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ResourceFeature FeatureValue:before {
                            content: oxy_label(text, " Feature Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ResourceFeature FeatureNote:before {
                            content: " Feature Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            ResourceFeatureType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ResourceForm {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ResourceLink {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ResourceMode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ResourceVersion {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ResourceVersion ResourceForm:before {
                            content: oxy_label(text, " Resource Form:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ResourceVersion ResourceVersionFeature:before {
                            content: " Resource Version Feature ";
                            font-weight: bold;
                            
                            }
                        
                        ResourceVersion ResourceLink:before {
                            content: oxy_label(text, " Resource Link:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ResourceVersion ContentDate:before {
                            content: " Content Date ";
                            font-weight: bold;
                            
                            }
                        
                         
            ResourceVersionFeature {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ResourceVersionFeature ResourceVersionFeatureType:before {
                            content: oxy_label(text, " Resource Version Feature Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ResourceVersionFeature FeatureValue:before {
                            content: oxy_label(text, " Feature Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ResourceVersionFeature FeatureNote:before {
                            content: " Feature Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            ResourceVersionFeatureType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ROWSalesRightsType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReturnsCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReturnsCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReturnsCodeTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ReturnsConditions {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        ReturnsConditions ReturnsCodeType:before {
                            content: oxy_label(text, " Returns Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        ReturnsConditions ReturnsCodeTypeName:before {
                            content: oxy_label(text, " Returns Code Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        ReturnsConditions ReturnsCode:before {
                            content: oxy_label(text, " Returns Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SalesRights {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SalesRights SalesRightsType:before {
                            content: oxy_label(text, " Sales Rights Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SalesRights Territory:before {
                            content: " Territory ";
                            font-weight: bold;
                            
                            }
                        
                        SalesRights ProductIdentifier:before {
                            content: " Product Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        SalesRights PublisherName:before {
                            content: oxy_label(text, " Publisher Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SalesRightsType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SalesOutlet {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SalesOutlet SalesOutletIdentifier:before {
                            content: " Sales Outlet Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        SalesOutlet SalesOutletName:before {
                            content: oxy_label(text, " Sales Outlet Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SalesOutlet SalesOutletName:before {
                            content: oxy_label(text, " Sales Outlet Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SalesOutletIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SalesOutletIdentifier SalesOutletIDType:before {
                            content: oxy_label(text, " Sales Outlet ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SalesOutletIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SalesOutletIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SalesOutletIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SalesOutletName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SalesRestriction {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SalesRestriction SalesRestrictionType:before {
                            content: oxy_label(text, " Sales Restriction Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SalesRestriction SalesOutlet:before {
                            content: " Sales Outlet ";
                            font-weight: bold;
                            
                            }
                        
                        SalesRestriction SalesRestrictionNote:before {
                            content: " Sales Restriction Note: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        SalesRestriction StartDate:before {
                            content: oxy_label(text, " Start Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SalesRestriction EndDate:before {
                            content: oxy_label(text, " End Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SalesRestrictionNote {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            SalesRestrictionType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ScriptCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Sender {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Sender SenderIdentifier:before {
                            content: " Sender Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Sender SenderName:before {
                            content: oxy_label(text, " Sender Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Sender SenderName:before {
                            content: oxy_label(text, " Sender Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Sender ContactName:before {
                            content: oxy_label(text, " Contact Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Sender EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SenderIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SenderIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SenderIdentifier SenderIDType:before {
                            content: oxy_label(text, " Sender ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SenderIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SenderIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SenderName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SentDateTime {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SequenceNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SourceName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SourceTitle {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SourceType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            StartDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Stock {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Stock LocationIdentifier:before {
                            content: " Location Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Stock LocationName:before {
                            content: oxy_label(text, " Location Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Stock StockQuantityCoded:before {
                            content: " Stock Quantity Coded ";
                            font-weight: bold;
                            
                            }
                        
                        Stock OnHand:before {
                            content: oxy_label(text, " On Hand:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Stock OnOrder:before {
                            content: oxy_label(text, " On Order:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Stock CBO:before {
                            content: oxy_label(text, " CBO:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Stock OnOrderDetail:before {
                            content: " On Order Detail ";
                            font-weight: bold;
                            
                            }
                        
                         
            StockQuantityCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            StockQuantityCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            StockQuantityCodeTypeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            StockQuantityCoded {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        StockQuantityCoded StockQuantityCodeType:before {
                            content: oxy_label(text, " Stock Quantity Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        StockQuantityCoded StockQuantityCodeTypeName:before {
                            content: oxy_label(text, " Stock Quantity Code Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        StockQuantityCoded StockQuantityCode:before {
                            content: oxy_label(text, " Stock Quantity Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            StudyBibleType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Subject {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Subject MainSubject:before {
                            content: " Main Subject ";
                            font-weight: bold;
                            
                            }
                        
                        Subject SubjectSchemeIdentifier:before {
                            content: oxy_label(text, " Subject Scheme Identifier:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Subject SubjectSchemeName:before {
                            content: oxy_label(text, " Subject Scheme Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Subject SubjectSchemeVersion:before {
                            content: oxy_label(text, " Subject Scheme Version:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Subject SubjectCode:before {
                            content: oxy_label(text, " Subject Code:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Subject SubjectHeadingText:before {
                            content: oxy_label(text, " Subject Heading Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Subject SubjectHeadingText:before {
                            content: oxy_label(text, " Subject Heading Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SubjectCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SubjectHeadingText {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SubjectSchemeIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SubjectSchemeName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SubjectSchemeVersion {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Subtitle {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SuffixToKey {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Supplier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Supplier SupplierRole:before {
                            content: oxy_label(text, " Supplier Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Supplier SupplierIdentifier:before {
                            content: " Supplier Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        Supplier SupplierName:before {
                            content: oxy_label(text, " Supplier Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Supplier SupplierName:before {
                            content: oxy_label(text, " Supplier Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Supplier TelephoneNumber:before {
                            content: oxy_label(text, " Telephone Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Supplier FaxNumber:before {
                            content: oxy_label(text, " Fax Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Supplier EmailAddress:before {
                            content: oxy_label(text, " Email Address:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Supplier Website:before {
                            content: " Website ";
                            font-weight: bold;
                            
                            }
                        
                         
            SupplierCodeType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplierCodeValue {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplierIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SupplierIdentifier SupplierIDType:before {
                            content: oxy_label(text, " Supplier ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplierIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SupplierIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SupplierIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplierName {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplierOwnCoding {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SupplierOwnCoding SupplierCodeType:before {
                            content: oxy_label(text, " Supplier Code Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplierOwnCoding SupplierCodeValue:before {
                            content: oxy_label(text, " Supplier Code Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SupplierRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplyDate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SupplyDate SupplyDateRole:before {
                            content: oxy_label(text, " Supply Date Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplyDate DateFormat:before {
                            content: oxy_label(text, " Date Format:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplyDate Date:before {
                            content: oxy_label(text, " Date:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            SupplyDateRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            SupplyDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SupplyDetail Supplier:before {
                            content: " Supplier ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail SupplierOwnCoding:before {
                            content: " Supplier Own Coding ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail ReturnsConditions:before {
                            content: " Returns Conditions ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail ProductAvailability:before {
                            content: oxy_label(text, " Product Availability:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplyDetail SupplyDate:before {
                            content: " Supply Date ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail OrderTime:before {
                            content: oxy_label(text, " Order Time:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SupplyDetail NewSupplier:before {
                            content: " New Supplier ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail Stock:before {
                            content: " Stock ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail PackQuantity:before {
                            content: oxy_label(text, " Pack Quantity:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        SupplyDetail UnpricedItemType:before {
                            content: oxy_label(text, " Unpriced Item Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupplyDetail Price:before {
                            content: " Price ";
                            font-weight: bold;
                            
                            }
                        
                        SupplyDetail Reissue:before {
                            content: " Reissue ";
                            font-weight: bold;
                            
                            }
                        
                         
            SupportingResource {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        SupportingResource ResourceContentType:before {
                            content: oxy_label(text, " Resource Content Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupportingResource ContentAudience:before {
                            content: oxy_label(text, " Content Audience:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupportingResource ResourceMode:before {
                            content: oxy_label(text, " Resource Mode:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        SupportingResource ResourceFeature:before {
                            content: " Resource Feature ";
                            font-weight: bold;
                            
                            }
                        
                        SupportingResource ResourceVersion:before {
                            content: " Resource Version ";
                            font-weight: bold;
                            
                            }
                        
                         
            Tax {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Tax TaxType:before {
                            content: oxy_label(text, " Tax Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Tax TaxRateCode:before {
                            content: oxy_label(text, " Tax Rate Code:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Tax TaxRatePercent:before {
                            content: oxy_label(text, " Tax Rate Percent:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Tax TaxableAmount:before {
                            content: oxy_label(text, " Taxable Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Tax TaxableAmount:before {
                            content: oxy_label(text, " Taxable Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Tax TaxAmount:before {
                            content: oxy_label(text, " Tax Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        Tax TaxAmount:before {
                            content: oxy_label(text, " Tax Amount:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            TaxAmount {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TaxRateCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TaxRatePercent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TaxableAmount {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TaxType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TelephoneNumber {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Territory {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Territory CountriesIncluded:before {
                            content: oxy_label(text, " Countries Included:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Territory RegionsIncluded:before {
                            content: oxy_label(text, " Regions Included:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Territory RegionsIncluded:before {
                            content: oxy_label(text, " Regions Included:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Territory CountriesExcluded:before {
                            content: oxy_label(text, " Countries Excluded:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Territory CountriesExcluded:before {
                            content: oxy_label(text, " Countries Excluded:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Territory RegionsExcluded:before {
                            content: oxy_label(text, " Regions Excluded:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                         
            Text {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            TextAuthor {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TextContent {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        TextContent TextType:before {
                            content: oxy_label(text, " Text Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TextContent ContentAudience:before {
                            content: oxy_label(text, " Content Audience:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TextContent Text:before {
                            content: " Text: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        TextContent TextAuthor:before {
                            content: oxy_label(text, " Text Author:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TextContent TextSourceCorporate:before {
                            content: oxy_label(text, " Text Source Corporate:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TextContent SourceTitle:before {
                            content: oxy_label(text, " Source Title:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TextContent ContentDate:before {
                            content: " Content Date ";
                            font-weight: bold;
                            
                            }
                        
                         
            TextItem {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        TextItem TextItemType:before {
                            content: oxy_label(text, " Text Item Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TextItem TextItemIdentifier:before {
                            content: " Text Item Identifier ";
                            font-weight: bold;
                            
                            }
                        
                        TextItem PageRun:before {
                            content: " Page Run ";
                            font-weight: bold;
                            
                            }
                        
                        TextItem NumberOfPages:before {
                            content: oxy_label(text, " Number Of Pages:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            TextItemIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TextItemIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        TextItemIdentifier TextItemIDType:before {
                            content: oxy_label(text, " Text Item ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TextItemIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TextItemIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            TextItemType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TextSourceCorporate {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TextType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ThesisPresentedTo {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ThesisType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ThesisYear {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitleDetail {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        TitleDetail TitleType:before {
                            content: oxy_label(text, " Title Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TitleDetail TitleElement:before {
                            content: " Title Element ";
                            font-weight: bold;
                            
                            }
                        
                        TitleDetail TitleStatement:before {
                            content: " Title Statement: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                         
            TitleElement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        TitleElement SequenceNumber:before {
                            content: oxy_label(text, " Sequence Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleElementLevel:before {
                            content: oxy_label(text, " Title Element Level:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        TitleElement PartNumber:before {
                            content: oxy_label(text, " Part Number:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement YearOfAnnual:before {
                            content: oxy_label(text, " Year Of Annual:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement YearOfAnnual:before {
                            content: oxy_label(text, " Year Of Annual:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleText:before {
                            content: oxy_label(text, " Title Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleText:before {
                            content: oxy_label(text, " Title Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleText:before {
                            content: oxy_label(text, " Title Text:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitlePrefix:before {
                            content: oxy_label(text, " Title Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitlePrefix:before {
                            content: oxy_label(text, " Title Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitlePrefix:before {
                            content: oxy_label(text, " Title Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleWithoutPrefix:before {
                            content: oxy_label(text, " Title Without Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleWithoutPrefix:before {
                            content: oxy_label(text, " Title Without Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement TitleWithoutPrefix:before {
                            content: oxy_label(text, " Title Without Prefix:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        TitleElement Subtitle:before {
                            content: oxy_label(text, " Subtitle:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            TitleElementLevel {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitlePrefix {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitlesAfterNames {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitlesBeforeNames {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitleStatement {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            TitleText {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitleType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TitleWithoutPrefix {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            ToLanguage {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            TradeCategory {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            UnnamedPersons {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            UnpricedItemType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            Website {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        Website WebsiteRole:before {
                            content: oxy_label(text, " Website Role:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        Website WebsiteDescription:before {
                            content: " Website Description: ";
                            font-weight: bold;
                            font-style: normal;
                            }
                        
                        Website WebsiteLink:before {
                            content: oxy_label(text, " Website Link:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            WebsiteDescription {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            background-color: #DDDDDD;}
         
            WebsiteLink {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            WebsiteRole {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            WorkIdentifier {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: italic;
            border-left: 2pt solid gray;
            }
        WorkIdentifier WorkIDType:before {
                            content: oxy_label(text, " Work ID Type:  ", width, 12em)
                            oxy_editor(type, combo, edit, "#text" );                            
                            font-weight: bold;
                            }
                        
                        WorkIdentifier IDTypeName:before {
                            content: oxy_label(text, " ID Type Name:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                        WorkIdentifier IDValue:before {
                            content: oxy_label(text, " ID Value:  ", width, 12em)
                            oxy_editor(type, text, edit, "#text" , columns, 60);                            
                            font-weight: bold;
                            }
                        
                         
            WorkIDType {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            WorkRelationCode {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
         
            YearOfAnnual {
            display: block; 
            margin-left: 1em;
            padding-left: 6pt;
            padding-bottom: 2pt;
                font-size: 12pt;
            font-style: normal;
            font-weight: normal;
            border-left: 2pt solid gray;
            visibility:-oxy-collapse-text;}
        

a {
    display:inline;
    text-decoration:underline;
    color:navy;
    background-color:inherit;
}

a[name]:after,
a[id]:after { 
    content: url("img/a_name.gif"); 
    vertical-align: text-top;
}


abbr,

acronym {
    display:inline;
    font-weight:bold;
}


address {
    display: block;
    font-style: italic; 
    margin-top: 1.1ex ;
}


applet:before{
    content: "Applet: width=\"" attr(width) "\", height=\"" attr(height) "\"";
    color:gray;
    background-color: inherit;
    font-weight:bold;
}
object:before{
    content: "Object: width=\"" attr(width) "\", height=\"" attr(height) "\"";
    color:gray;
    background-color: inherit;
    font-weight:bold;
}

applet,
object{
    display: block;
    color: inherit;
    background-color:#FFFFDD;
    border:1px solid gray;
    padding: 2px;
    margin-top:2px;
    margin-bottom:2px;
}
applet > param:before,
object > param:before
{
    content: "Parameter name=\"" attr(name) "\", value=\"" attr(value) "\"";
    color:gray;
    background-color:inherit;    
}
applet > param,
object > param{
    display:list-item;
    list-style-type:square;
    margin-left:2em;
}


b {
    font-weight:bold;
}


big {
    font-size:larger;
}


blockquote{
    display:block;
    margin:1em 4em;
}
blockquote:before,
blockquote:after{
    color:gray;
    background-color:inherit;
    font-size: 1.2em;
}
blockquote:before{
    content:open-quote;
}
blockquote:after{
    content:close-quote;
}

base,
link,
script{
    font-family:monospace;
    font-size:smaller;
    display:block;
    background-color:#EEEEFF;
    color:inherit;
    border:1px solid #CCCCEE;
    margin:1px 0;
    
}

base:before{
    content:"Base: href=" attr(href);
    font-weight:bold;
}


link[href]:before{
    content:"Link: href=" attr(href);
    font-weight:bold;
}

link[href][style]:before{
    content:"Link: href=" attr(href) " style=" attr(style);
    font-weight:bold;
}

script[type]:before{
    content:"Script: type=" attr(type);
    font-weight:bold;
}
script[type][src]:before{
    content:"Script: type=" attr(type) " src=" attr(src);
    font-weight:bold;
}
style[type]:before{
    content:"Style: type=" attr(type);
    font-weight:bold;
}



body {
    display:block;       
}

body[background] {
    background-image: attr(background, url);
}

body[bgcolor] {
    background-color: attr(bgcolor, color);
}

body[text] {
    color: attr(text, color);
}



basefont { 
    content: "basefont=\"" attr(size)  "\"";
    font-size: small;
    color: gray;
    background-color:inherit;
}


bdo:before,
bdo:after{
    display:inline;
    background-color:#CCEECC;
    border:1px solid #CCFFCC;
    color:inherit;
    padding:2px;
}
bdo:before {
    content: "[bdo " attr(rtl);
}
bdo:after {
    content: "]";
}


br {
    display:block;
}
br:after{
    content:"";
}


button {
    display: inline;
    text-align: center;
    border:2px outset silver;
    background-color: #DDDDDD;
    color:black;
    padding:2px;
}



center {
    display: block;
    text-align:center;
}


cite {
    display:inline;
    font-style:italic;
}


code {
    font-family:monospace;
    font-size:smaller;
}

col {
    display: table-column;
}


colgroup {
    display: table-column-group;
}


dd{
    display:block;
    margin: 0.5em 3em;
}

dl{
    display:block;
}

dt{
    display:block;
    margin: 0.5em 1em;
    font-weight:bold;
}


del{
    display:inline;
    text-decoration:line-through;
}


dfn {
    font-style:italic;
}


dir {
    display:block;
    margin-left:2em;
}
dir > li {
    display:list-item;
    list-style-type:disc;
}


div {
    display:block;
}


em{
    font-style: italic;
}


fieldset {
    display:block;
    border: 1px solid navy;
    padding: 3px;
    margin:0.5em 0;
}


font{
    display:inline;
}
font[color] {
    color: attr(color, color);
}
font[face] {
    font-family: attr(face);
}

font[size="1"] {
    font-size: x-small;
}
font[size="2"] {
    font-size: small;
}
font[size="3"] {
    font-size: medium;
}
font[size="4"] {
    font-size: large;
}
font[size="5"] {
    font-size: x-large;
}
font[size="6"],
font[size="7"] {
    font-size: xx-large;
}
font[size="-3"],
font[size="-2"],
font[size="-1"] {
    font-size: smaller;
}
font[size="+1"],
font[size="+2"],
font[size="+3"] {
    font-size: larger;
}


form {
    display:block;
    margin:1em 0;
}


frame{
    display:block;
    border:1px solid silver;
}
frame:before {
    content: "Frame: href=" attr(href);
    color:gray;
    background-color:inherit;    
}
frameset{
    display:block;
    border:1px solid gray;
    padding:2px;
}


head {
  display: block;
  color: silver;
  background-color: rgb(251, 242, 234);
  border: solid black 1pt;
  margin-bottom: 2em;
  padding-top: 1em;
  padding-left: 1em;
  padding-right: 1em;
  padding-bottom: 1em;
}


h1,
h2,
h3,
h4,
h5,
h6 {    
    display:block;
    font-weight:bold;
    background-color:inherit;
}

h1 {
    font-size: 2em;
    margin:1em 0;
}

h2 {
    font-size: 1.75em;
    margin:0.75em 0;
}

h3 {
    font-size: 1.5em;
    margin:0.5em 0;
}

h4 {
    font-size: 1.25em;
    margin: 0.5em 0;
}

h5 {
    font-size: 1em;
    margin: 0.5em 0;
}

h6 {
    font-size: .75em;
    margin: 0.5em 0;
}


hr {
    display:block;
    border-top:1px inset silver;
    margin:0.5em 0;
    content:"";
}


html {
    display:block;
}



i {
    font-style:italic;
}


iframe:before {
    display:inline;
    content: "Iframe: src=" attr(src);
    color:gray;
    background-color:inherit;
}


img {
    display: inline;
    content: attr(src,url);
    width:attr(width, length);
    height:attr(height, length);
}




input {
    display:inline;
    border:1px solid gray;
    padding:0 3em;
    color:gray;
    background-color:white;
    font-size:smaller;
}
input:before{
    content:attr(value) "  ";
}

input[type=submit],
input[type=button]{
    display:inline;
    border-style:outset;
    background-color:silver;
    color:black;
}

input[type=submit]:before,
input[type=button]:before{
    content:attr(value);
}



ins {
    text-decoration: underline;
}


kbd {
    font-family:monospace;
    font-size:smaller;
}


label {
    display: inline;
    font-weight: bold;
}


legend {
    display: block;
    background-color: silver;
    color:inherit;
    font-weight: bold;
    padding: 2px;
    margin-bottom:2px;
}



map:before {
    content: "Map: id=\"" attr(id) "\"";
    color:gray;
    background-color:inherit;
    font-weight:bold;
}
map[name]:before {
    content: "Map: id=\"" attr(id) "\", name=\"" attr(name) "\"";
    color:gray;
    background-color:inherit;
    font-weight:bold;
}
map {
    display: block;
    color: inherit;
    background-color:#FFFFDD;
    border:1px solid gray;
    padding: 2px;
    margin-top:2px;
    margin-bottom:2px;
}


map > area {
    display:list-item;
    list-style-type:square;
    margin-left: 2em;
}

map > area:before {
    content: "alt=\"" attr(alt) "\"" " href=\"" attr(href) "\"";
    color:gray;
    background-color:inherit;
}


menu {
    display:block;
    margin-left:2em;
}
menu > li {
    display:list-item;
    list-style-type:disc;
}


noframes {
    display:block;
}


noscript {
    display:block;
    background-color:#FFEEEE;
    color:inherit;
}

ol {
    display:block;
    margin-top: 1.33ex;
    margin-bottom: 1.33ex;
}

ol > li {
    display:list-item;
    list-style:decimal;    
    margin-left: 2em;
}


ol[type=a] > li{
  list-style:lower-alpha;
}
ol[type=A] > li{
  list-style:upper-alpha;
}
ol[type=i] > li {
  list-style:lower-roman;
}
ol[type=I] > li {
  list-style:upper-roman;
}

ol ol,
ul ol,
ul ul,
ol ul{
    margin-top:0em;
    margin-bottom:0em;
}


optgroup{
    display:block;
    margin:1ex;
    border:1px solid silver; 
    padding:2px;
}
optgroup:before{
    color:gray;
    background-color:inherit;
    font-size:small;
    content: "Option group label=" attr(label);
}
option{
    display:list-item;
    margin:0 2em;
}
option:after{
    content: " - " attr(value);
    color:gray;
    background-color:inherit;    
    font-size:small;
}
select {
    display:block;
    border-style:ridge;
    border-color:silver;
}


p{ 
    display: block;
    margin: 0.5ex 0;
}
p[align=center]{
    text-align:center;
}
p[align=left]{
    text-align:left;
}
p[align=right]{
    text-align:right;
}



pre {
    font-family:monospace;
    font-size:smaller;
    white-space: pre;
}


q{
    font-style:italic;
}
q:before{
    content:open-quote;
    color:gray;
    background-color:inherit;
}
q:after{
    content:close-quote;
    color:gray;
    background-color:inherit;    
}


small{
    font-size: smaller;
}

strike,
s{
    text-decoration: line-through;
}


samp{
    font-family:monospace;
    font-size:smaller;
}

span{
    display:inline;
}
span:before{
    content:"{";
    color:gray;
    background-color:inherit;
}
span:after{
    content:"}";
    color:gray;
    background-color:inherit;
}


strong { 
    font-weight: bold; 
}


sub {
    vertical-align: sub;
    font-size:small;
}


sup {
    vertical-align: super;
    font-size:small;    
}

textarea {
    font-family:  monospace;
    font-size:smaller;
    display:block;
    margin:1em;
    border:2px inset silver;
    padding:1px;
}





tt{
    font-family: monospace;
    font-size:smaller;
}


title {
    display:block;
    font-weight:bold;
    color:#002244;
    background-color:inherit;
    font-size: 2.2em;
    margin: 1em 0;
    border-bottom: 2px solid #004477;    
}


u{
    text-decoration:underline;
}


ul {
    display:block;
    margin-top: 1.33ex;
    margin-bottom: 1.33ex;
}

ul > li {
    display:list-item;
    list-style-type:disc;
    margin-left: 2em;
}

ul > li ul > li {
    list-style-type:square;
}
ul > li ul > li ul > li {
    list-style-type:circle;
}
ul > li ul > li ul > li ul > li {
    list-style-type:disc;
}


var {
    font-style:italic;
}

font {
    font-family:attr(face);
    font-size:attr(size);
    font-style:attr(style);
}        
</style>	
  </head>
      <body>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>

<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
