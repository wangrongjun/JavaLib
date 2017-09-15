package com.wangrg.java_util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class ExcelUtil {

//    pub static final String postName = "xls";

    public static void excelOut(List objects, String excelPath) {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(new File(excelPath));
            WritableSheet sheet1 = workbook.createSheet("sheet1", 0);

//            i指向每个不同的对象，相当于表格的行
            for (int i = 0; i < objects.size(); i++) {
                Object object = objects.get(i);
                Class clazz = object.getClass();
                Field[] fields = clazz.getDeclaredFields();

//                j指向这个对象的某一个属性，相当于表格中的列
                for (int j = 0; j < fields.length; j++) {
                    if (i == 0) {//若这一行是第一行，则写进类对象的成员变量值
                        sheet1.addCell(new Label(j, i, fields[j].getName()));
                    }

//                    这一行写进类对象的成员变量名
                    fields[j].setAccessible(true);
                    Object value = fields[j].get(object);
                    if (value != null) {
                        sheet1.addCell(new Label(j, i + 1, value.toString()));
                    }
                }
            }

            workbook.write();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static <T> List<T> excelIn(Class<T> entityClass, String excelPath) {

        List<T> entities = new ArrayList<>();
        Workbook workbook = null;

        try {
            workbook = Workbook.getWorkbook(new File(excelPath));
            Sheet sheet = workbook.getSheet(0);
            Field[] fields = entityClass.getDeclaredFields();

            for (int i = 0; i < sheet.getRows() - 1; i++) {
                T entity = entityClass.newInstance();

                for (int j = 0; j < fields.length; j++) {

                    fields[j].setAccessible(true);
                    String type = fields[j].getType().getSimpleName();
                    String value = sheet.getCell(j, i + 1).getContents();

                    if (TextUtil.isEmpty(value)) {
                        continue;
                    }

                    if ("int".equals(type)) {
                        fields[j].setInt(entity, Integer.parseInt(value));

                    } else if ("double".equals(type)) {
                        fields[j].setDouble(entity, Double.parseDouble(value));

                    } else if ("String".equals(type)) {
                        fields[j].set(entity, value);
                    }
                }

                entities.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return entities;
    }


}
