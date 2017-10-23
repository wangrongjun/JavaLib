package com.wangrg.java_lib.web;

import com.wangrg.java_lib.java_util.DateUtil;
import com.wangrg.java_lib.java_util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CopyClass {

    public static final String modelName = "server";
    public static String webappName = "ebook";
    public static String servletPackage = "servlet";

    public static final String buildClassDir = modelName + "\\eq\\classes\\main\\";
    public static String WEB_INF_DIR =
            "C:/IDE/apache-tomcat-7.0.67/webapps/" + webappName + "/WEB-INF/";

    public static void main(String[] args) throws Exception {

        // 1.把所有.class文件复制到WEB-INF/classes下
        FileUtil.copyDir(new File(buildClassDir), new File(WEB_INF_DIR));
        FileUtil.deleteDir(new File(WEB_INF_DIR, "classes"));
        boolean success = new File(WEB_INF_DIR, "main").renameTo(new File(WEB_INF_DIR, "classes"));
        if (!success) {
            System.out.println("rename failed!!!");
            return;
        }

        // 2.把java_lib的java_lib.jar复制到WEB-INF/lib下
        String fromDir = "C:\\IDE\\android-studio-project\\MyLib\\java_lib\\eq\\libs\\java_lib.jar";
        String toDir = WEB_INF_DIR + "lib/java_lib.jar";
        success = new File(toDir).delete();
        if (!success) {
            System.out.println("delete failed!!!");
            return;
        }
        FileUtil.copy(fromDir, toDir);

        // 3.更新WEB-INF下的web.xml
        String servletClassDir = buildClassDir + servletPackage + "\\";
        String[] servletNameList = new File(servletClassDir).list();
        List<WebXmlFileCreator.Info> infoList = new ArrayList<>();
        for (String servletName : servletNameList) {
            servletName = servletName.replace(".class", "");// 去掉后缀
            String firstLetter = (servletName.charAt(0) + "").toLowerCase();
            String urlPattern = "/" + firstLetter + servletName.substring(1);
            WebXmlFileCreator.Info info = new WebXmlFileCreator.Info(
                    servletName, servletPackage + "." + servletName, urlPattern);
            infoList.add(info);
        }
        String xml = WebXmlFileCreator.create(infoList);
        FileUtil.write(xml, WEB_INF_DIR + "web.xml");

        // 4.Web项目发布成功！
        System.out.println(DateUtil.getCurrentTime());
        System.out.println("Web update succeed!");
    }

}
