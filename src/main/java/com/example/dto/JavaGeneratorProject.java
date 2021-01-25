package com.example.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 19:51
 */
@Data
public class JavaGeneratorProject {

    //临时输出地址
    private String outRoot;

    //父pom坐标信息
    private String groupId;
    private String artifactId;
    private String version;

    //子pom信息
    private String childartifactId;

    private Integer serverPort;

    private String packageName;

    private String appName;

    private List<InterfaceInfo> interfaceInfos;

    private List<PojoInfo> boPojos;

    private List<PojoInfo> dtoPojos;

    public String getPackageName_dir() {
        return packageName.replace('.', '/');
    }

}
