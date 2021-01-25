package com.example.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 20:01
 */
@Data
public class MethodInfo {

    private String method;

    private String methodMapping;

    private String methodDesc;

    private String httpMethod;

    private PojoInfo boPojo;

    private PojoInfo dtoPojo;
}
