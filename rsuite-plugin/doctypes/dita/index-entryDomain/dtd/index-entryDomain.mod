<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET Index Entry Domain
     
     Defines specialization of <sectiondiv> for specifying index entries.
     
     Copyright (c) 2013 IET
     
     ============================================================= -->

<!-- ============================================================= -->
<!--                   ELEMENT NAME ENTITIES                       -->
<!-- ============================================================= -->

<!ENTITY % index-entry       "index-entry" >


<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!-- IET index-entry contains an index entry. It may be nested to hold multiple levels of index
entries -->
<!ENTITY % index-entry.content
  "(#PCDATA |
   sectiondiv     |
   %d4p_pubcontent-d-sectiondiv; |
   %index-entry; |
    %basic.ph; |
    %basic.block; |
    %txt.incl; | 
    %data.elements.incl; | 
    %foreign.unknown.incl;
  )*"
>
<!ENTITY % index-entry.attributes
"
  name
    NMTOKEN
    'index-entry'
%univ-atts;          
  keyref
    CDATA
    #IMPLIED
  outputclass 
    CDATA
    #IMPLIED     
">
<!ELEMENT index-entry %index-entry.content; >
<!ATTLIST index-entry %index-entry.attributes; >

<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->

<!ATTLIST index-entry         %global-atts;  class CDATA "+ topic/sectiondiv  index-entry-d/index-entry ">


<!-- ================== End IET index-entry Domain ==================== -->