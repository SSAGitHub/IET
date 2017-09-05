<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET DITA For Publishers Regulation Map module
     
     Map that represents a regulation publication.
     
   
     Copyright (c) 2013 IET

     ============================================================= -->

<!-- =============================================================
     Non-DITA Namespace declarations: 
     ============================================================= -->

<!-- ============================================================= -->
<!--                   ARCHITECTURE ENTITIES                       -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!--                   SPECIALIZATION OF DECLARED ELEMENTS         -->
<!-- ============================================================= -->




<!-- ============================================================= -->
<!--                   ELEMENT NAME ENTITIES                       -->
<!-- ============================================================= -->
 

<!ENTITY % regmap      "regmap"                                      >


<!-- ============================================================= -->
<!--                    DOMAINS ATTRIBUTE OVERRIDE                 -->
<!-- ============================================================= -->


<!ENTITY included-domains 
  ""
>


<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!ENTITY % regmap.content 
 "((%pubtitle;)?, 
   (%pubmeta;)?,
   (%keydefs;)?,
   (%topicref;)*,
   ((%mapref;) |
    ((%publication;) |
     (%publication-mapref;))|
    ((%covers;)?,
     (%colophon;)?, 
     ((%frontmatter;) |
      (%department;) |
      (%page;))*,
     ((%pubbody;) |
      (%part;) |
      (%chapter;) |
      (%sidebar;) |
      (%subsection;))?, 
     ((%appendixes;) |
      (%appendix;) |
      (%backmatter;) |
      (%page;) |
      (%department;) |
      (%colophon;))*)),
   (%data.elements.incl; |
    %reltable;)*)
 "
>
<!ENTITY % regmap.attributes
 "title 
            CDATA 
                      #IMPLIED
  id 
            ID 
                      #IMPLIED
  %conref-atts;
  anchorref 
            CDATA 
                      #IMPLIED
  outputclass 
            CDATA 
                      #IMPLIED
  %localization-atts;
  %topicref-atts;
  %select-atts;"
>
<!ELEMENT regmap       
  %regmap.content;                  
>
<!ATTLIST regmap
  %regmap.attributes;
  %arch-atts;
  domains    
    CDATA                
    "(map mapgroup-d) (map regmap-d) (map pubmeta-d) (map d4p_enumerationMap-d) &included-domains;"    
>



<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->


<!ATTLIST regmap      %global-atts;  class CDATA "- map/map regmap/regmap ">

<!-- ================== End regmap Declaration Set  ===================== -->