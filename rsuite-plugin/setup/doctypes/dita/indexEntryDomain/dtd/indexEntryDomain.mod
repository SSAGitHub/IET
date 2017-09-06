<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET Index Entry Domain
     
     Defines specialization of <sectiondiv> for specifying index entries.
     
     Copyright (c) 2013 IET
     
     ============================================================= -->

<!-- ============================================================= -->
<!--                   ELEMENT NAME ENTITIES                       -->
<!-- ============================================================= -->

<!ENTITY % indexEntry       "indexEntry" >

<!ENTITY % indexTerm       "indexTerm" >

<!ENTITY % indexSee       "indexSee" >

<!ENTITY % indexSeeAlso       "indexSeeAlso" >

<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!-- IET indexEntry contains an index entry. It may be nested to hold multiple levels of index
entries -->
<!ENTITY % indexEntry.content
  "(#PCDATA |
   sectiondiv     |
   %d4p_pubcontent-d-sectiondiv; |
   %indexEntry; |
   %indexTerm; |
    %basic.ph; |
    %basic.block; |
    %txt.incl; | 
    %data.elements.incl; | 
    %foreign.unknown.incl;
  )*"
>
<!ENTITY % indexEntry.attributes
"
  name
    NMTOKEN
    'indexEntry'
%univ-atts;          
  keyref
    CDATA
    #IMPLIED
  outputclass 
    CDATA
    #IMPLIED     
">
<!ELEMENT indexEntry %indexEntry.content; >
<!ATTLIST indexEntry %indexEntry.attributes; >

<!-- IET indexTerm contains an index term.  -->
<!ENTITY % indexTerm.content
  "(#PCDATA |
  %indexSee; |
  %indexSeeAlso; |  
    %basic.ph; |
    %basic.block; |
    %txt.incl; | 
    %data.elements.incl; | 
    %foreign.unknown.incl;
  )*"
>
<!ENTITY % indexTerm.attributes
"
  name
    NMTOKEN
    'indexTerm'
%univ-atts;          
  keyref
    CDATA
    #IMPLIED
  outputclass 
    CDATA
    #IMPLIED     
">
<!ELEMENT indexTerm %indexTerm.content; >
<!ATTLIST indexTerm %indexTerm.attributes; >

<!-- IET indexSee contains an index see reference term.  -->
<!ENTITY % indexSee.content
  "(#PCDATA |
    %basic.ph; |
    %txt.incl; | 
    %data.elements.incl; | 
    %foreign.unknown.incl;
  )*"
>
<!ENTITY % indexSee.attributes
"
  name
    NMTOKEN
    'indexSee'
%univ-atts;          
  keyref
    CDATA
    #IMPLIED
  outputclass 
    CDATA
    #IMPLIED     
">
<!ELEMENT indexSee %indexSee.content; >
<!ATTLIST indexSee %indexSee.attributes; >

<!-- IET indexSeeAlso contains an index see reference term.  -->
<!ENTITY % indexSeeAlso.content
  "(#PCDATA |
    %basic.ph; |
    %txt.incl; | 
    %data.elements.incl; | 
    %foreign.unknown.incl;
  )*"
>
<!ENTITY % indexSeeAlso.attributes
"
  name
    NMTOKEN
    'indexSeeAlso'
%univ-atts;          
  keyref
    CDATA
    #IMPLIED
  outputclass 
    CDATA
    #IMPLIED     
">
<!ELEMENT indexSeeAlso %indexSeeAlso.content; >
<!ATTLIST indexSeeAlso %indexSeeAlso.attributes; >

<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->

<!ATTLIST indexEntry         %global-atts;  class CDATA "+ topic/sectiondiv  indexEntry-d/indexEntry ">

<!ATTLIST indexTerm         %global-atts;  class CDATA "+ topic/p  indexEntry-d/indexTerm ">

<!ATTLIST indexSee         %global-atts;  class CDATA "+ topic/ph  indexEntry-d/indexSee ">

<!ATTLIST indexSeeAlso         %global-atts;  class CDATA "+ topic/ph  indexEntry-d/indexSeeAlso ">
<!-- ================== End IET indexEntry Domain ==================== -->