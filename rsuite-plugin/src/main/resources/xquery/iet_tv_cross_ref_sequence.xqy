declare namespace iet-fx="http://www.theiet.orq/rsuite/functions";

declare function iet-fx:create-custom-folder(){
    try {
        xdmp:directory-create("/IET/")
    }catch ($exception){}
};

declare function iet-fx:get-sequence()
    {
    
    let $currentYear := substring(string(fn:current-date()),1,4)
    let $folder := iet-fx:create-custom-folder()
	    let $sequencePath := concat("/IET/", "iet_tv_cross_ref_sequence_", $currentYear, ".xml" ) 	    
    return 
        if ( not( xdmp:exists( doc($sequencePath) ) ) )	 then
          let $result := xdmp:document-insert($sequencePath, <seq>1</seq>)
          return 1
        else
          let $sequence := doc($sequencePath)/seq/text()
          let $newSequence := number($sequence) + 1
          let $result := xdmp:node-replace( doc($sequencePath)/seq, <seq>{$newSequence}</seq>)
          return $newSequence
        
};

iet-fx:get-sequence()
 
