<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET Regulation Number Domain
     
     Defines specialization of data for specifying regulation numbers
     in iet-regulation content.
     
     Copyright (c) 2013 IET
     
     ============================================================= -->

<!-- ============================================================= -->
<!--                   ELEMENT NAME ENTITIES                       -->
<!-- ============================================================= -->

<!ENTITY % regnum       "regnum" >


<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!-- IET Regulation Number holds a literal enumerator value that is the regulation number. -->
<!ENTITY % regnum.content
"
  (#PCDATA | %text;)*
">
<!ENTITY % regnum.attributes
"
  name
    NMTOKEN
    'regnum'
">
<!ELEMENT regnum %regnum.content; >
<!ATTLIST regnum %regnum.attributes; >

<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->

<!ATTLIST regnum         %global-atts;  class CDATA "+ topic/data  d4p_enumBase-d/d4pEnumeratorBase     regnum-d/regnum ">


<!-- ================== End IET Regulatin Number Topic Domain ==================== -->