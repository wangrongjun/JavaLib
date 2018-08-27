package com.wangrj.java_lib.java_util;

import java.io.*;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * by wangrongjun on 2017/8/15.
 */

public class FreeMarkerUtil {

    public static void create(String fltText, Map<String, Object> dataModel, Writer writer)
            throws IOException, TemplateException {
        Template template = new Template(null, new StringReader(fltText), new Configuration());
        template.process(dataModel, writer);
    }

    public static void create(File fltFile, Map<String, Object> dataModel, Writer writer)
            throws IOException, TemplateException {
        Template template = new Template(null, new FileReader(fltFile), new Configuration());
        template.process(dataModel, writer);
    }

    /**
     * 使用示例：
     * FreeMakerUtil.convert(new File("E:/"), "index.ftl",
     * dataModel, new FileWriter("E:/output.html"));
     *
     * @param root         模版文件所在的根目录
     * @param fileName     模版文件名
     * @param dataModel    数据模型
     * @param outputWriter 结果输出流
     */
    public static void convert(File root, String fileName, Map<String, Object> dataModel,
                               Writer outputWriter) throws IOException, TemplateException {
        // 负责管理FreeMarker模板的Configuration实例
        Configuration cfg = new Configuration();
        // 指定FreeMarker模板文件的位置  
        cfg.setDirectoryForTemplateLoading(new File(""));

        // 设置模版文件的根目录
        cfg.setTemplateLoader(new FileTemplateLoader(root));
        // 获取模板文件
        Template template = cfg.getTemplate(fileName);
        // 获取模板文件的字符编码
//        template.getEncoding();
        // 合并数据模型和模板，并将结果输出到out中
        template.process(dataModel, outputWriter); // 往模板里写数据
    }

}
