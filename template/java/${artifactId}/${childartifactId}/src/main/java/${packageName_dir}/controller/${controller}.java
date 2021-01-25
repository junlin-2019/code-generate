package ${packageName}.controller;

<#assign boNames='' />
<#assign requestBody=false>
<#list methodinfos as methodinfo>
    <#if methodinfo.boPojo??>
        <#assign requestBody=true>
        <#if !boNames?contains(',' +methodinfo.boPojo.pojoName + ',')>
            <#assign boNames = boNames + ',' + methodinfo.boPojo.pojoName + ','>
import ${methodinfo.boPojo.pojoPackageName}.${methodinfo.boPojo.pojoName};
        </#if>
    </#if>
    <#if methodinfo.dtoPojo??>        
        <#if !boNames?contains(',' +methodinfo.dtoPojo.pojoName + ',')>
        <#assign boNames = boNames + ',' + methodinfo.dtoPojo.pojoName + ','>
import ${methodinfo.dtoPojo.pojoPackageName}.${methodinfo.dtoPojo.pojoName};
        </#if>
    </#if>
</#list>

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if requestBody>
import org.springframework.web.bind.annotation.RequestBody;
</#if>

/**
 * ${controllerDesc}
 */
@RestController
@RequestMapping("${controllerMapping}")
public class ${controller} {
    
<#list methodinfos as methodinfo>
    public ${methodinfo.dtoPojo.pojoName} ${methodinfo.method}(@RequestBody ${methodinfo.boPojo.pojoName} bo) {
        ${methodinfo.dtoPojo.pojoName} dto = new ${methodinfo.dtoPojo.pojoName}();
        return dto;
    }
    
</#list>
}
