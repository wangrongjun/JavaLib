package com.wangrj.java_lib.test;

import com.wangrj.java_lib.constant.JavaLibConstant;
import com.wangrj.java_lib.java_util.ExcelUtil;
import com.wangrj.java_lib.java_util.FreeMarkerUtil;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/11/6.
 */
public class MdCreator {

    public static class Row {
        String type;
        String question;
        String selectA;
        String selectB;
        String selectC;
        String selectD;
        String answer;

        public String getType() {
            return type;
        }

        public String getQuestion() {
            return question;
        }

        public String getSelectA() {
            return selectA;
        }

        public String getSelectB() {
            return selectB;
        }

        public String getSelectC() {
            return selectC;
        }

        public String getSelectD() {
            return selectD;
        }

        public String getAnswer() {
            return answer;
        }
    }

    @Test
    public void create() throws Exception {
//        String title = "101软件开发工程师（JAVA）部分题库-1";
        String title = "101软件开发工程师（JAVA）部分题库-2";
        List<Row> rowList = ExcelUtil.excelIn(Row.class, "E:/" + title + ".xls");
        createMarkdown(title, rowList);
    }

    public void createMarkdown(String title, List<Row> rowList) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", title);
        dataModel.put("rowList", rowList);
        String path = JavaLibConstant.srcDir() + "com/wangrj/java_lib/test/question.ftl";
        FreeMarkerUtil.create(new File(path), dataModel, new FileWriter("E:/" + title + ".md"));
    }

}
