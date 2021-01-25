package com.example.controller;

import com.example.dto.JavaGeneratorProject;
import com.example.dto.ProjectInitVo;
import com.example.service.InitInfoService;
import com.example.service.ServiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 19:46
 */
@RestController
public class ProjectController {

    @Autowired
    private ServiceInfoService serviceInfoService;

    @Autowired
    private InitInfoService initInfoService;

    public ProjectController() {
    }

    @RequestMapping(value = "/projectInit", produces = "application/json")
    public <T> ResponseEntity<T> projectInit(@RequestBody ProjectInitVo bo) {
        JavaGeneratorProject project = serviceInfoService.getJavaGeneratorProject(bo);
        ResponseEntity<T> result;
        try {
            result = initInfoService.initGeneratorProject(project);
        } catch (IOException e) {
            e.printStackTrace();
            return (ResponseEntity<T>) ResponseEntity.status(502)
                    .header("Content-Type", "application/text").body("构建失败");
        }
        return result;
    }

}
