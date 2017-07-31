<#import "/lib/common.ftl" as com />
<#import "/lib/common/index.ftl" as lib />
<#escape x as (x)!>
    <#compress>
        <@com.indexHeader projectName="UAS" title="首页 - 用户权限系统" selected="INDEX" />
        <@com.left selected="INDEX" />
        <@lib.content />
        <@com.indexFooter />
    </#compress>
</#escape>