xquery version "1.0-ml";
declare namespace r = "http://www.rsuitecms.com/rsuite/ns/metadata";
declare namespace mv="http://www.rsuitecms.com/rsuite/ns/materialized-view";
           
for $x in fn:collection('rsuite:mv-current')/article/front/article-meta[mv:metadata/mv:aliases]/mv:metadata
return $x/mv:system/mv:id/text()