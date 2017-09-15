package com.wangrg.java_util;

import com.wangrg.db.Dao;
import com.wangrg.db.SqlEntityUtil;
import com.wangrg.db.basis.TypeAnno;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/3/8.
 */
public class HtmlCreateUtil {

    private static final String headHtml = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"UTF-8\">\n" +
            "\t\t<title></title>\n" +
            "\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "\t\t<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css\">\n" +
            "\t\t<style type=\"text/css\">\n" +
            "\t\t\tbody {\n" +
            "\t\t\t\tpadding: 10px 10px;\n" +
            "\t\t\t}\n" +
            "\t\t</style>\n" +
            "\t</head>\n" +
            "\n" +
            "\t<body>\n";

    public static final String footHtml = "\t\t<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n" +
            "\t</body>\n" +
            "\n" +
            "</html>";

    public static class Table {
        private String title;
        private List<String> headList;
        private List<List<String>> rowList;

        public Table(String title, List<String> headList, List<List<String>> rowList) {
            this.title = title;
            if (headList == null) {
                headList = new ArrayList<>();
            }
            this.headList = headList;
            if (rowList == null) {
                rowList = new ArrayList<>();
            }
            this.rowList = rowList;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getHeadList() {
            return headList;
        }

        public void setHeadList(List<String> headList) {
            this.headList = headList;
        }

        public List<List<String>> getRowList() {
            return rowList;
        }

        public void setRowList(List<List<String>> rowList) {
            this.rowList = rowList;
        }
    }

    public static String createHtml(List<Table> tableList) {
        String html = "";
        for (Table table : tableList) {
            String title = table.getTitle();
            List<String> headList = table.getHeadList();
            List<List<String>> rowList = table.getRowList();

            // 1.定义表格
            html += "<table class=\"table table-bordered table-striped\">\n";

            // 2.声明表格的标题
            if (!TextUtil.isEmpty(title)) {
                html += "<caption>" + title + "</caption>\n";
            }

            // 3.创建表格的首行
            html += "<tr>\n";
            for (String head : headList) {
                html += "<th>" + head + "</th>\n";
            }
            html += "</tr>\n";

            // 4.填入每一行的数据
            for (List<String> row : rowList) {
                if (row == null) {
                    continue;
                }
                html += "<tr>\n";
                for (String value : row) {
                    html += "<td>" + value + "</td>\n";
                }
                html += "</tr>\n";
            }

            // 5.插入表格结束符和换行
            html += "</table>\n<br>\n<br>\n";
        }

        return headHtml + html + footHtml;
    }

    public static Table createHtmlTable(Class entityClass, Connection conn) {
        List<String> headList = new ArrayList<>();
        List<List<String>> rowList = new ArrayList<>();

        try {
            List<String> fieldNameList = new ArrayList<>();
            Field[] fieldList = entityClass.getDeclaredFields();
            for (Field field : fieldList) {
                if (field.getAnnotation(TypeAnno.class) != null) {
                    fieldNameList.add(field.getName());
                }
            }

            String sql = SqlEntityUtil.queryAllSql(entityClass);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List entityList = Dao.getResult(entityClass, rs);
            rs.close();
            ps.close();

            headList = fieldNameList;

            for (int i = 0; i < entityList.size(); i++) {
                List<String> row = new ArrayList<>();
                Object entity = entityList.get(i);
                for (int j = 0; j < fieldNameList.size(); j++) {
                    try {
                        Field field = entityClass.getDeclaredField(fieldNameList.get(j));
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        row.add(value + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                rowList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Table(entityClass.getSimpleName(), headList, rowList);
    }

}
