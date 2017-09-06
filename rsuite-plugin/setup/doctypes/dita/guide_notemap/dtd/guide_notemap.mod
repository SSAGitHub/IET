<?xml version="1.0" encoding="utf-8"?>
<!-- =============================================================
     IET DITA For Publishers Guide Note Map module
     
     Map that represents a Guide Note publication.
     
   
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
 

<!ENTITY % guide_notemap      "guide_notemap"                                      >


<!-- ============================================================= -->
<!--                    DOMAINS ATTRIBUTE OVERRIDE                 -->
<!-- ============================================================= -->


<!ENTITY included-domains 
  ""
>


<!-- ============================================================= -->
<!--                    ELEMENT DECLARATIONS                       -->
<!-- ============================================================= -->

<!ENTITY % guide_notemap.content 
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
<!ENTITY % guide_notemap.attributes
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
<!ELEMENT guide_notemap       
  %guide_notemap.content;                  
>
<!ATTLIST guide_notemap
  %guide_notemap.attributes;
  %arch-atts;
  domains    
    CDATA                
    "(map mapgroup-d) (map guide_notemap-d) (map pubmeta-d) (map d4p_enumerationMap-d) &included-domains;"    
>



<!-- ============================================================= -->
<!--                    SPECIALIZATION ATTRIBUTE DECLARATIONS      -->
<!-- ============================================================= -->


<!ATTLIST guide_notemap      %global-atts;  class CDATA "- map/map guide_notemap/guide_notemap ">

<!-- ================== End Guide Note Map Declaration Set  ===================== -->