<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET Citation Margin Domain
     
     Defines specialization of data for specifying margin citations
     in IET Guidanace Note content.
     
     Copyright (c) 2013 IET
     
     ============================================================= -->

<!-- ============================================================= -->
<!--                   ELEMENT NAME ENTITIES                       -->
<!-- ============================================================= -->

<!ENTITY % cite_margin       "cite_margin" >


<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!-- IET cite_margin specializes <p> and contains an <xref> that provides a navigation link to an IET regulation. -->
<!ENTITY % cite_margin.content
"
  (#PCDATA | %xref;)*
">
<!ENTITY % cite_margin.attributes
"
%univ-atts;          
  outputclass 
    CDATA
    #IMPLIED 
  name
    NMTOKEN
    'cite_margin'
">
<!ELEMENT cite_margin %cite_margin.content; >
<!ATTLIST cite_margin %cite_margin.attributes; >

<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->

<!ATTLIST cite_margin         %global-atts;  class CDATA "+ topic/p  cite_margin-d/cite_margin ">


<!-- ================== End IET Citation Margin Topic Domain ==================== -->