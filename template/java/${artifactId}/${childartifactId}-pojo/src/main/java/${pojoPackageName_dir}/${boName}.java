package $ import lombok.Data;

{pojoPackageName};

<#assign pojoNames=''/>
<#assign bigDecimal=false>
<#assign listUtile=false>
<#assign mapUtile=false>
<#assign dateUtile=false>

<#if pojoInfo.pojoParams??>
<#list pojoInfo.pojoParams as paraminfo>
<#if paraminfo.dataType?contains('BigDecimal')>
<#assign bigDecimal=true>
</#if>
<#if paraminfo.dataType?contains('List')>
<#assign listUtile=true>
</#if>
<#if paraminfo.dataType?contains('Map')>
<#assign mapUtile=true>
</#if>
<#if paraminfo.dataType?contains('Date')>
<#assign dateUtile=true>
</#if>
</#list>
</#if>
<#if bigDecimal>
</#if>
<#if listUtile>
</#if>
<#if mapUtile>
</#if>
<#if dateUtile>
</#if>

@Data
public class ${boName}{

<#if pojoInfo.pojoParams??>
<#list pojoInfo.pojoParams as paramInfo>
    private ${paramInfo.dataType} ${paramInfo.paramKey};
</#list>
</#if>
}