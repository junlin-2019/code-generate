package com.example.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 20:00
 */
@Data
public class InterfaceInfo {

    private String controller;

    private String controllerMapping;

    private String controllerDesc;

    private List<MethodInfo> methods;
}
