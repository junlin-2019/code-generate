package com.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    private static final String DEF_SAVE_PATH = "image/";

    public static String download(String strUrl, String fileName, String savePath)
            throws Exception {
        HttpURLConnection conn = null;
        URL url = new URL(strUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-agent", userAgent);
        conn.setUseCaches(false);
        conn.setConnectTimeout(DEF_CONN_TIMEOUT);
        conn.setReadTimeout(DEF_READ_TIMEOUT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        InputStream is = conn.getInputStream();// 连接得到输入流内容

        File file = new File(savePath);
        if (!file.exists()) {// 保存文件夹不存在则建立
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(new File(savePath, fileName));
        int intRead = 0;
        byte[] buf = new byte[1024 * 2];// 生成2K 大小的容器用于接收图片字节流
        while ((intRead = is.read(buf)) != -1) {
            fos.write(buf, 0, intRead);// 文件输出流到指定路径文件
            fos.flush();
        }
        fos.close();// 关闭输出流
        if (conn != null) {
            conn.disconnect();// 关闭连接
        }

        return savePath.endsWith("/") ? savePath + fileName : savePath + "/" + fileName;
    }

    public static String download(String strUrl) throws Exception {
        return download(strUrl, strUrl.substring(strUrl.lastIndexOf("/") + 1));// 获取网址中的图片名
    }

    public static String download(String strUrl, String fileName) throws Exception {
        return download(strUrl, fileName, DEF_SAVE_PATH);
    }

    public static void deleteFile(File file){
        if (file.isFile()){//判断是否为文件，是，则删除
            System.out.println(file.getAbsoluteFile());//打印路径
            file.delete();
        }else{//不为文件，则为文件夹
            String[] childFilePath = file.list();//获取文件夹下所有文件相对路径
            for (String path:childFilePath){
                File childFile= new File(file.getAbsoluteFile()+"/"+path);
                deleteFile(childFile);//递归，对每个都进行判断
            }
            System.out.println(file.getAbsoluteFile());
            file.delete();
        }
    }

    /**
     * 压缩文件
     *
     * @param srcfile
     */
    public static void zipFiles(File srcfile, OutputStream outputStream) {

        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(outputStream);
            if(srcfile.isFile()){
                zipFile(srcfile, out, "");
            } else{
                File[] list = srcfile.listFiles();
                for (int i = 0; i < list.length; i++) {
                    compress(list[i], out, "");
                }
            }

            System.out.println("压缩完毕");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
	                out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩单个文件
     *
     * @param srcfile
     */
    public static void zipFile(File srcfile, ZipOutputStream out, String basedir) {
        if (!srcfile.exists()) {
	        return;
        }

        byte[] buf = new byte[1024];
        FileInputStream in = null;

        try {
            int len;
            in = new FileInputStream(srcfile);
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
	                out.closeEntry();
                }
                if (in != null) {
	                in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件夹里的文件
     * 起初不知道是文件还是文件夹--- 统一调用该方法
     * @param file
     * @param out
     * @param basedir
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }
    /**
     * 压缩文件夹
     * @param dir
     * @param out
     * @param basedir
     */
    public static void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
	        return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }


    public static void main(String[] args) throws Exception {
    }


}
