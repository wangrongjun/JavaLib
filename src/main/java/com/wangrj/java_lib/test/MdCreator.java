package com.wangrj.java_lib.test;

import com.wangrj.java_lib.constant.JavaLibConstant;
import com.wangrj.java_lib.java_util.ExcelUtil;
import com.wangrj.java_lib.java_util.FreeMakerUtil;
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

    static class A {
        void a() {
        }

        static void as() {
        }
    }

    static class B extends A {
        void a() {
            a();
            as();
            bs();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        static void bs() {
        }
    }

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

//    public static class SelectQuestion{
//        String questionHeader;
//        String question;
//        String selectA;
//        String selectB;
//        String selectC;
//        String selectD;
//        String answer;
//    }
//
//    public static class JudgeQuestion{
//        String questionHeader;
//        String question;
//        String selectA;
//    }
//
//    public static class SimpleAskQuestion{
//
//    }

    @Test
    public void create() throws Exception {
        List<Row> rowList = ExcelUtil.excelIn(Row.class, "E:/new.xls");
        createMarkdown(rowList);
    }

    public void createMarkdown(List<Row> rowList) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("rowList", rowList);
        String path = JavaLibConstant.srcDir() + "com/wangrj/java_lib/test/question.ftl";
        FreeMakerUtil.create(new File(path), dataModel, new FileWriter("E:/out.md"));
    }

}
