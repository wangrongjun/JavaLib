package com.wangrg.java_util;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * by wangrongjun on 2017/8/15.
 */

public class FreeMakerUtil {

    /**
     * @param root         模版文件所在的根目录
     * @param htmlFileName 模版文件名
     * @param dataModel    数据模型
     * @param outputWriter 结果输出流
     * @throws IOException
     */
    public static void convert(File root, String htmlFileName, Map<String, Object> dataModel,
                               Writer outputWriter) throws IOException, TemplateException {
        // 负责管理FreeMarker模板的Configuration实例
        Configuration cfg = new Configuration();
        // 指定FreeMarker模板文件的位置  
        cfg.setDirectoryForTemplateLoading(new File(""));

        // 设置模版文件的根目录
        cfg.setTemplateLoader(new FileTemplateLoader(root));
        // 获取模板文件
        Template template = cfg.getTemplate(htmlFileName);
        // 获取模板文件的字符编码
//        template.getEncoding();
        // 合并数据模型和模板，并将结果输出到out中
        template.process(dataModel, outputWriter); // 往模板里写数据
    }

}
