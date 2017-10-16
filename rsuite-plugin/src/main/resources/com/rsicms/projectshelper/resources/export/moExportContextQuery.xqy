
declare namespace rsifunct = "http://com.rsicms.projectshelper";

declare function rsifunct:getCaContents
  ($caId as xs:string) as element()* {

    let $document := fn:collection("rsuite:current")/rs_ca_map/rs_ca[@r:rsuiteId=$caId]
        return
            <ca id="{$caId}">
            {
                for $x in $document/*
                    let $name := $x/local-name()
                    let $id := $x/@r:rsuiteId
                return
                    if ($name = 'rs_caref') then     
                        rsifunct:getCaContents($x/@href)
                    else if ($name = 'rs_moref') then
                        <mf h="{$x/@href}" i="{$id}"/>
                    else ()     
            }
            </ca>
   };
  

let $initialResult := rsifunct:getCaContents('$caId$')         
let $moRefIds :=  $initialResult//@i                  

let $info := /r:res[./r:md/r:targetRevision and @r:id = $moRefIds]/concat(@r:id, '_', ./r:md/r:targetRevision) 
let $caIds :=  $initialResult//@id[parent::ca]        
let $casDetails := <caInfos>
                    {
                        for $caInfo in /r:res[@r:id = $caIds]
                        return 
                            <ci id="{$caInfo/@r:id}">{$caInfo/r:vt/r:v/r:dn/text()}</ci>
                       }</caInfos>


return        
    <result xmlns:r="http://www.rsuitecms.com/rsuite/ns/metadata">
            <moRevisionInfo>{$info}</moRevisionInfo>
            <caDetails>{$casDetails}</caDetails>
            {$initialResult}
    </result>  
         