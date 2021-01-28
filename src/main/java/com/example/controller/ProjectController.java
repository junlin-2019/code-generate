package com.example.controller;

import com.example.dto.JavaGeneratorProject;
import com.example.dto.ProjectInitVo;
import com.example.service.InitInfoService;
import com.example.service.ServiceInfoService;
import com.example.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    @RequestMapping(value = "/testDownload", produces = "application/json")
    public void testDownload() throws IOException {
        File file = new File("code-generate.zip");
        OutputStream outputStream = new FileOutputStream(file);
        FileUtil.zipFiles(new File("template"),outputStream);

    }

    @PostMapping("/init")
    public <T> ResponseEntity<T> projectInit() {
        ResponseEntity<T> result;
        try {

            File dir = new File("industry-platform-api/template");
            File download = new File(dir.getParent(), dir.getName() + ".zip");
            FileUtil.zipFiles(dir,new FileOutputStream(download));
            result = (ResponseEntity<T>) upload(download, download.getName(),
                    "application/zip");

        } catch (IOException e) {
            e.printStackTrace();
            return (ResponseEntity<T>) ResponseEntity.status(502)
                    .header("Content-Type", "application/text").body("构建失败");
        }
        return result;
    }

    private ResponseEntity<byte[]> upload(File download, String fileName,
                                          String contentType) throws IOException {

        InputStream in = new FileInputStream(download);
        byte[] bytes = new byte[(int) download.length()];
        in.read(bytes);
        in.close();
        ResponseEntity<byte[]> result = createResponseEntity(bytes, contentType, fileName);
        return result;
    }
    private ResponseEntity<byte[]> createResponseEntity(byte[] content, String contentType,
                                                        String fileName) {
        String contentDispositionValue = "attachment; filename=\"" + fileName + "\"";
        return ResponseEntity.ok().header("Content-Type", contentType)
                .header("Content-Disposition", contentDispositionValue).body(content);
    }

}
