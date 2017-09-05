declare namespace r = "http://www.rsuitecms.com/rsuite/ns/metadata";

declare variable $moId as xs:string external;

<result>
{
for $ca in fn:collection('rsuite:current')/rs_ca_map/rs_ca[rs_moref/@href = $moId]
return
<ca rsuiteId="{$ca/@r:rsuiteId}"  morefId="{$ca/rs_moref[@href = $moId]/@r:rsuiteId}" />
}
</result>
