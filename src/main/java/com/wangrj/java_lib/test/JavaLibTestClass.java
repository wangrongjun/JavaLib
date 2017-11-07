package com.wangrj.java_lib.test;

import com.wangrj.java_lib.db3.db.OracleDatabase;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String[] args) throws IOException, TemplateException {
        List<String> sqlList = new OracleDatabase("abc").dropTableSql(UserInfo.class);
        for (String sql : sqlList) {
            System.out.println(sql);
            System.out.println("-----------------");
        }
    }

}
