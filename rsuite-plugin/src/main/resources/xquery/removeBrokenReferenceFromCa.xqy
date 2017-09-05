let $uri := fn:document-uri(fn:root(fn:collection("rsuite:current")/rs_ca_map/rs_ca[@r:rsuiteId="729945"]))
let $old := fn:doc($uri)
let $bad := $old//rs_canode@r:rsuiteId='2094143'
return xdmp:node-delete($bad)