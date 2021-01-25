package com.example.service;

import com.example.dto.InterfaceInfo;
import com.example.dto.JavaGeneratorProject;
import com.example.dto.MethodInfo;
import com.example.dto.PojoInfo;
import com.example.utils.FileHelper;
import com.example.utils.IOHelper;
import com.example.utils.StringTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: admin
 * @Date: 2021/1/20 18:22
 */
public class Generator {
    private static final String WEBAPP_GENERATOR_INSERT_LOCATION = "webapp-generator-insert-location";
    private static final String BO_NAME = "${boName}.java";
    private static final String DTO_NAME = "${dtoName}.java";
    private static final String CONTROLLER_NAME = "${controller}.java";
    private Map<String, Object> model = new HashMap<>();
    private JavaGeneratorProject projectRequestBo;

    public Generator(JavaGeneratorProject projectRequestBo) throws Exception {
        model.put("project", projectRequestBo);
        model.putAll(BeanUtils.describe(projectRequestBo));
        this.projectRequestBo = projectRequestBo;
    }

    public void addExtraProperties(Map<String,Object> properties){
        model.putAll(properties);
    }

    public void generateArchetype(String templateDirPath) throws Exception {
        Configuration config = new Configuration(Configuration.VERSION_2_3_28);
        File templateRootDir = new File(templateDirPath).getAbsoluteFile();
        config.setDirectoryForTemplateLoading(templateRootDir);
        config.setNumberFormat("###############");
        config.setBooleanFormat("true,false");

        List files = new ArrayList();
        FileHelper.listFiles(templateRootDir, files);

        for (int i = 0; i < files.size(); i++) {
            File file = (File) files.get(i);
            String templateRelativePath = FileHelper.getRelativePath(templateRootDir, file);
            String outputFilePath = templateRelativePath;

            if (templateRelativePath.trim().equals("")) {
                continue;
            }
            if (file.isDirectory() || file.isHidden()) {
                System.out.println(
                        "[skip]\t\t isDirectory or isHidden template:" + templateRelativePath);
                continue;
            }
            if (BO_NAME.equals(file.getName()) || DTO_NAME.equals(file.getName())
                    || CONTROLLER_NAME.equals(file.getName())) {
                getControllerFile(config, templateRelativePath, outputFilePath, file.getName());
                continue;
            }
            try {
                generateFile(config, templateRelativePath, outputFilePath);
            } catch (Exception e) {
                throw new RuntimeException("generate '" + projectRequestBo.getGroupId()
                        + "' oucur error,template is:" + templateRelativePath, e);
            }
        }
    }

    /**
     * 获取controller和bo文件
     *
     * @param config
     * @param templateRelativePath
     * @param outputFilePath
     * @throws Exception
     */
    private void getControllerFile(Configuration config, String templateRelativePath,
                                   String outputFilePath, String fileName) throws Exception {
        JavaGeneratorProject project = (JavaGeneratorProject) model.get("project");
            if (CONTROLLER_NAME.equals(fileName)) {
                List<InterfaceInfo> interfaceInfos = project.getInterfaceInfos();
                if (interfaceInfos != null) {
                    for (InterfaceInfo interfaceInfo : interfaceInfos) {
                        model.putAll(BeanUtils.describe(interfaceInfo));
                        List<MethodInfo> methods = interfaceInfo.getMethods();
                        model.put("methodinfos", methods);
                        generateFile(config, templateRelativePath, outputFilePath);
                    }
                }
            } else if (BO_NAME.equals(fileName)) {
                List<PojoInfo> boPojos = project.getBoPojos();
                if (boPojos != null && boPojos.size() > 0) {
                    for (PojoInfo pojoInfo : boPojos) {
                        model.put("pojoInfo", pojoInfo);
                        model.put("boName", pojoInfo.getPojoName());
                        model.put("pojoPackageName", pojoInfo.getPojoPackageName());
                        model.put("pojoPackageName_dir", pojoInfo.getPojoPackageName_dir());
                        generateFile(config, templateRelativePath, outputFilePath);
                    }
                }
            } else if (DTO_NAME.equals(fileName)) {
                List<PojoInfo> dtoPojos = project.getDtoPojos();
                if (dtoPojos != null && dtoPojos.size() > 0) {
                    for (PojoInfo pojoInfo : dtoPojos) {
                        model.put("pojoInfo", pojoInfo);
                        model.put("dtoName", pojoInfo.getPojoName());
                        model.put("pojoPackageName", pojoInfo.getPojoPackageName());
                        model.put("pojoPackageName_dir", pojoInfo.getPojoPackageName_dir());
                        generateFile(config, templateRelativePath, outputFilePath);
                    }
                }
            }
    }


    private void generateFile(Configuration config, String templateRelativePath,
                              String outputFilePath) throws IOException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, TemplateException {

        Template template = config.getTemplate(templateRelativePath);
        String targetFilename = getTargetFilename(outputFilePath);
        File absoluteOutputFilePath = getAbsoluteOutputFilePath(targetFilename);
        if (OutputFileIsExist(config, templateRelativePath, targetFilename,
                absoluteOutputFilePath)) {
            return;
        }

        System.out
                .println("[generate]\t template:" + templateRelativePath + " to " + targetFilename);
        saveNewOutputFileContent(template, model, absoluteOutputFilePath);
    }

    private String getTargetFilename(String templateFilepath)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String targetFilename = resolveFile(templateFilepath, model);
        return targetFilename;
    }

    private boolean OutputFileIsExist(Configuration config, String templateRelativePath,
                                      String targetFilename, File absoluteOutputFilePath)
            throws IOException, TemplateException {
        if (absoluteOutputFilePath.exists()) {
            // 文件已经存在
            File file = new File(templateRelativePath);
            if (CONTROLLER_NAME.equals(file.getName())) {
                // 需要追加方法
                StringWriter newFileContentCollector = new StringWriter();
                String includeTemplatePath = templateRelativePath.replace(".java", ".include");
                Template includeTemplate = config.getTemplate(includeTemplatePath);
                if (isFoundInsertLocation(includeTemplate, absoluteOutputFilePath,
                        newFileContentCollector)) {
                    IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private File getAbsoluteOutputFilePath(String targetFilename) {
        String outRoot = getOutRootDir();
        File outputFile = new File(outRoot, targetFilename);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    private void saveNewOutputFileContent(Template template, Map model, File outputFile)
            throws IOException, TemplateException {
        FileWriter out = new FileWriter(outputFile);
        template.process(model, out);
        out.close();
    }

    private String resolveFile(String templateFilepath, Map fileModel) {
        return new StringTemplate(templateFilepath, fileModel).toString();
    }

    private String getOutRootDir() {
        return model.get("outRoot").toString();
    }

    public void cleanZip(String path) throws IOException {
        File file = new File(path + ".zip");
        if (file.exists()) {
            file.delete();
            System.out.println("[Delete File]    " + path + ".zip");
        }
    }

    public void cleanDirectory(String path) throws IOException {
        FileUtils.deleteDirectory(new File(path));
        System.out.println("[Delete Dir]    " + path);
    }

    /**
     * 将字符串转化成unicode码
     *
     * @author shuai.ding
     * @param string
     * @return
     */
    private String string2Unicode(String string) {

        if (string == null || string.length() == 0) {
            return null;
        }

        char[] bytes = string.toCharArray();
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            char c = bytes[i];

            // 标准ASCII范围内的字符，直接输出
            if (c >= 0 && c <= 127) {
                unicode.append(c);
                continue;
            }
            String hexString = Integer.toHexString(bytes[i]);

            unicode.append("\\u");

            // 不够四位进行补0操作
            if (hexString.length() < 4) {
                unicode.append("0000".substring(hexString.length(), 4));
            }
            unicode.append(hexString);
        }
        return unicode.toString();
    }

    /**
     * 根据模板做部分追加
     *
     * @param template
     * @param outputFile
     * @param newFileContent
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    private boolean isFoundInsertLocation(Template template, File outputFile,
                                          StringWriter newFileContent) throws IOException, TemplateException {
        LineNumberReader reader = new LineNumberReader(new FileReader(outputFile));
        String line = null;
        boolean isFoundInsertLocation = false;

        PrintWriter writer = new PrintWriter(newFileContent);
        while ((line = reader.readLine()) != null) {
            writer.println(line);
            // only insert once
            if (!isFoundInsertLocation && line.indexOf(WEBAPP_GENERATOR_INSERT_LOCATION) >= 0) {
                template.process(model, writer);
                writer.println();
                isFoundInsertLocation = true;
            }
        }

        writer.close();
        reader.close();
        return isFoundInsertLocation;
    }
}
