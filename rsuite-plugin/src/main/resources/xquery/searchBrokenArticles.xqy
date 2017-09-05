declare namespace r = "http://www.rsuitecms.com/rsuite/ns/metadata";

let $id := '492409'

return <docs id="{$id}">
<current-version>
{for $doc in fn:collection('rsuite:current')/article[front/article]

return
<result rsuiteId="{$doc/@r:rsuiteId}"  articleId="{$doc//article-meta/article-id[@pub-id-type = 'manuscript']}" nestedArticles="{count($doc//article)}" />

}
</current-version>
<materialized-view>
{count(fn:collection('rsuite:mv-current')/article[front/article])}
</materialized-view>
</docs>

