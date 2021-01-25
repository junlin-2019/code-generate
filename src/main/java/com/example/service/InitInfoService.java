package com.example.service;

import com.example.dto.JavaGeneratorProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.ZipFileSet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/19 20:06
 */
@Service
public class InitInfoService {
    public <T> ResponseEntity<T> initGeneratorProject(JavaGeneratorProject project) throws IOException {
        String path = project.getOutRoot() + File.separatorChar + project.getArtifactId();
        String templateDir = "template/java";
        Generator g;
        try {
            g = new Generator(project);
            // 删除生成项目
            g.cleanDirectory(path);
            g.cleanZip(path);
            g.generateArchetype(templateDir);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<T>) ResponseEntity.status(502)
                    .header("Content-Type", "application/text").body("构建失败");
        }
        File dir = new File(path);
        File download = new File(dir.getParent(), dir.getName() + ".zip");
        Zip zip = new Zip();
        zip.setProject(new Project());
        zip.setDefaultexcludes(false);
        ZipFileSet set = new ZipFileSet();
        set.setDir(dir);
        set.setIncludes("**,");
        set.setDefaultexcludes(false);
        zip.addFileset(set);
        zip.setDestFile(download.getCanonicalFile());
        zip.execute();
        ResponseEntity<T> result = (ResponseEntity<T>) upload(download, null, download.getName(),
                "application/zip");
        // 删除生成项目
        g.cleanDirectory(path);
        return result;
    }

    private ResponseEntity<byte[]> upload(File download, File dir, String fileName,
                                          String contentType) throws IOException {

        InputStream in = new FileInputStream(download);
        byte[] bytes = new byte[(int) download.length()];
        in.read(bytes);
        in.close();
        ResponseEntity<byte[]> result = createResponseEntity(bytes, contentType, fileName);
        if (dir != null) {
            cleanTempFiles(dir);
        }
        return result;
    }
    private ResponseEntity<byte[]> createResponseEntity(byte[] content, String contentType,
                                                        String fileName) {
        String contentDispositionValue = "attachment; filename=\"" + fileName + "\"";
        return ResponseEntity.ok().header("Content-Type", contentType)
                .header("Content-Disposition", contentDispositionValue).body(content);
    }

    private void cleanTempFiles(File dir) {
        File[] files = dir.listFiles();
        if (files.length>0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileSystemUtils.deleteRecursively(file);
                }
                else if (file.exists()) {
                    file.delete();
                }
            }
        }
    }
}
