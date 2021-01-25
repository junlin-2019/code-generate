package com.example.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 19:55
 */
@Data
public class PojoInfo {

    private String pojoName;

    private String pojoDesc;

    private String pojoPackageName;

    //参数
    private List<ParamInfo> pojoParams;

    public String getPojoPackageName_dir() {
        return pojoPackageName.replace('.', '/');
    }

}
