package com.example.service;

import com.example.dto.InterfaceInfo;
import com.example.dto.JavaGeneratorProject;
import com.example.dto.MethodInfo;
import com.example.dto.ParamInfo;
import com.example.dto.PojoInfo;
import com.example.dto.ProjectInitVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 20:05
 */
@Service
public class ServiceInfoService {
    public JavaGeneratorProject getJavaGeneratorProject(ProjectInitVo bo) {
        JavaGeneratorProject project = new JavaGeneratorProject();
        project.setOutRoot("\\www\\test");
        project.setGroupId("com.example");
        project.setArtifactId("code-generate");
        project.setVersion("1.0.0");
        project.setAppName("code-generate");
        project.setChildartifactId("code-generate-api");
        project.setServerPort(8011);
        project.setPackageName("com.example");
        project.setDtoPojos(getDtoPojos());
        project.setBoPojos(getBoPojos());
        project.setInterfaceInfos(getInterfaceInfos());
        return project;
    }
    private List<PojoInfo> getDtoPojos(){
        List<PojoInfo> list = new ArrayList<>();
        PojoInfo pojoInfo = new PojoInfo();
        pojoInfo.setPojoName("TestDto");
        pojoInfo.setPojoDesc("测试dto");
        pojoInfo.setPojoPackageName("com.example.dto");
        pojoInfo.setPojoParams(getParamInfos());
        list.add(pojoInfo);
        PojoInfo pojoInfo1 = new PojoInfo();
        pojoInfo1.setPojoName("TestDto1");
        pojoInfo1.setPojoDesc("测试dto1");
        pojoInfo1.setPojoPackageName("com.example.dto");
        pojoInfo1.setPojoParams(getParamInfos());
        list.add(pojoInfo1);
        return list;
    }
    private List<PojoInfo> getBoPojos(){
        List<PojoInfo> list = new ArrayList<>();
        PojoInfo pojoInfo = new PojoInfo();
        pojoInfo.setPojoName("TestBo");
        pojoInfo.setPojoDesc("测试bo");
        pojoInfo.setPojoPackageName("com.example.bo");
        pojoInfo.setPojoParams(getParamInfos());
        list.add(pojoInfo);
        PojoInfo pojoInfo1 = new PojoInfo();
        pojoInfo1.setPojoName("TestBo1");
        pojoInfo1.setPojoDesc("测试bo");
        pojoInfo1.setPojoPackageName("com.example.bo");
        pojoInfo1.setPojoParams(getParamInfos());
        list.add(pojoInfo1);
        return list;
    }

    private PojoInfo getBoPojo(){
        PojoInfo pojoInfo = new PojoInfo();
        pojoInfo.setPojoName("TestBo");
        pojoInfo.setPojoDesc("测试bo");
        pojoInfo.setPojoPackageName("com.example.bo");
        pojoInfo.setPojoParams(getParamInfos());
        return pojoInfo;
    }

    private PojoInfo getDtoPojo(){
        PojoInfo pojoInfo = new PojoInfo();
        pojoInfo.setPojoName("TestDto");
        pojoInfo.setPojoDesc("测试dto");
        pojoInfo.setPojoPackageName("com.example.dto");
        pojoInfo.setPojoParams(getParamInfos());
        return pojoInfo;
    }

    private List<ParamInfo> getParamInfos(){
        List<ParamInfo> list = new ArrayList<>();
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setParamKey("order");
        paramInfo.setDataType("BigDecimal");
        paramInfo.setParamDesc("订单号");
        ParamInfo paramInfo1 = new ParamInfo();
        paramInfo1.setParamKey("goods");
        paramInfo1.setDataType("String");
        paramInfo1.setParamDesc("商品");
        ParamInfo paramInfo2 = new ParamInfo();
        paramInfo2.setParamKey("goods");
        paramInfo2.setDataType("String");
        paramInfo2.setParamDesc("商品");
        list.add(paramInfo);
        list.add(paramInfo1);
        list.add(paramInfo2);
        return list;
    }

    private List<InterfaceInfo> getInterfaceInfos(){
        List<InterfaceInfo> list = new ArrayList<>();
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setController("TestController");
        interfaceInfo.setControllerDesc("测试类");
        interfaceInfo.setControllerMapping("/test");
        interfaceInfo.setMethods(getMethods());
        list.add(interfaceInfo);
        return list;
    }

    private List<MethodInfo> getMethods() {
        List<MethodInfo> list = new ArrayList<>();
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethod("testMethod");
        methodInfo.setMethodDesc("测试方法");
        methodInfo.setHttpMethod("Post");
        methodInfo.setMethodMapping("/demo");
        methodInfo.setBoPojo(getBoPojo());
        methodInfo.setDtoPojo(getDtoPojo());
        list.add(methodInfo);
        return list;
    }
}
