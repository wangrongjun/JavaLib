package com.wangrj.java_lib.java_util;

import com.wangrj.java_lib.db3.DbUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import jxl.write.Number;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Boolean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class ExcelUtil {
    /**
     * 导出到excel文件
     */
    public static void excelOut(List entityList, OutputStream os) throws Exception {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(os);
            WritableSheet sheet1 = workbook.createSheet("sheet1", 0);

            if (entityList == null || entityList.size() == 0) {
                return;
            }

            Class cls = entityList.get(0).getClass();
            Field[] fields = cls.getDeclaredFields();

            for (int i = 0; i < entityList.size(); i++) {// i指向每个不同的对象，相当于表格的行
                Object entity = entityList.get(i);
                for (int j = 0; j < fields.length; j++) {// j指向这个对象的某一个属性，相当于表格中的列
                    Field field = fields[j];
                    if (i == 0) {// 若是第一行，则写进类对象的成员变量名及类型名
                        WritableCellFormat format = new WritableCellFormat();
                        format.setBorder(Border.ALL, BorderLineStyle.THICK);
                        String s = field.getName() + " (" + field.getType().getSimpleName() + ")";
                        sheet1.addCell(new Label(j, i, s, format));
                    }

                    // 这一行写进类对象的成员变量名
                    field.setAccessible(true);
                    if (field.get(entity) == null) {// 避免值为空，表格中却出现null字符串
                        continue;
                    }
                    switch (field.getType().getSimpleName()) {
                        case "int":
                        case "Integer":
                        case "long":
                        case "Long":
                        case "float":
                        case "Float":
                        case "double":
                        case "Double":
                            String numberValue = String.valueOf(field.get(entity));
                            sheet1.addCell(new Number(j, i + 1, Double.parseDouble(numberValue)));
                            break;
                        case "boolean":
                        case "Boolean":
                            String booleanValue = String.valueOf(field.get(entity));
                            sheet1.addCell(new jxl.write.Boolean(j, i + 1, Boolean.valueOf(booleanValue)));
                            break;
                        case "String":
                            sheet1.addCell(new Label(j, i + 1, String.valueOf(field.get(entity))));
                            break;
                        case "Date":
                            sheet1.addCell(new DateTime(j, i + 1, (Date) field.get(entity)));
                            break;
                        default:
                            if (field.getAnnotation(ManyToOne.class) != null ||
                                    field.getAnnotation(OneToOne.class) != null) {
                                Field idField = DbUtil.getForeignKeyIdField(field);
                                idField.setAccessible(true);
                                String idValue = String.valueOf(idField.get(entity));
                                sheet1.addCell(new Number(j, i + 1, Long.parseLong(idValue)));
                            }
//                            throw new RuntimeException("can not resolve type: " +
//                                    field.getType().getName() + " " + field.getName());
                    }
                }
            }

            workbook.write();

        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

    }

    public static void excelOut(List entityList, String excelFilePath) throws Exception {
        excelOut(entityList, new FileOutputStream(excelFilePath));
    }

    /**
     * 从excel文件导入
     * 注意：对于字符串类型，导出时如果是""，导入会变为null。对于日期类型，则损失时分秒。
     */
    public static <T> List<T> excelIn(Class<T> entityClass, InputStream is) throws Exception {
        List<T> entityList = new ArrayList<>();
        Workbook workbook = null;

        try {
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            Field[] fields = entityClass.getDeclaredFields();

            for (int i = 1; i < sheet.getRows(); i++) {// 跳过第一行
                T entity = entityClass.newInstance();

                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    String value = sheet.getCell(j, i).getContents();

                    if (TextUtil.isEmpty(value)) {
                        continue;
                    }

                    switch (field.getType().getSimpleName()) {
                        case "int":
                        case "Integer":
                            field.set(entity, Integer.parseInt(value));
                            break;
                        case "long":
                        case "Long":
                            field.set(entity, Long.parseLong(value));
                            break;
                        case "float":
                        case "Float":
                            field.set(entity, Float.parseFloat(value));
                            break;
                        case "double":
                        case "Double":
                            field.set(entity, Double.parseDouble(value));
                            break;
                        case "boolean":
                        case "Boolean":
                            field.set(entity, Boolean.parseBoolean(value));
                            break;
                        case "String":
                            field.set(entity, value);
                            break;
                        case "Date":// value="17-9-28"
                            Calendar calendar = Calendar.getInstance();
                            String[] split = value.split("-");
                            calendar.set(
                                    Integer.parseInt(split[0]),
                                    Integer.parseInt(split[1]),
                                    Integer.parseInt(split[2]),
                                    0, 0, 0
                            );
                            field.set(entity, calendar.getTime());
                            calendar.getTime();
                            break;
                        default:
                            throw new RuntimeException("can not resolve type: " +
                                    field.getType().getName() + " " + field.getName());
                    }
                }

                entityList.add(entity);
            }

        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return entityList;
    }

    public static <T> List<T> excelIn(Class<T> entityClass, String excelFilePath) throws Exception {
        return excelIn(entityClass, new FileInputStream(excelFilePath));
    }

    /**
     * 把Excel中多个单元格复制到的文本进行预处理（把换行符变为字符串"\r\n"）
     * <p>
     * 如果某一行包含正则 /\t["]{1,3,5,7,9}/ 匹配的子串，则依次读取该行接下来的行，只要该行接下来的行不包含\t，就选出来。
     * 之后把该行和接下来所有选出来的行合并，行与行之间用\n作为分隔符，并去除第一行和最后一行首尾的双引号。
     * 去掉双引号：只要是奇数个双引号连在一起的，都要减少一个。如1个变0个，2个不管，3个变2个，4个不管。
     */
    public static String convertExcelText(String excelText) {
        StringBuilder builder = new StringBuilder();
        String[] lines = excelText.split("\r\n");
        int index = 0;
        Pattern pattern = Pattern.compile("\t[\"]([^\"]|[\"]{2})");
        while (index < lines.length) {
            builder.append(lines[index]);
            if (pattern.matcher(lines[index++]).find()) {
                while (index < lines.length && !lines[index++].contains("\t")) {
                    builder.append("\\r\\n").append(lines[index++]);
                }
            }
        }
        // 连在一起的奇数个双引号减少一个
        int count = 0;
        char[] chars = (builder.toString() + "a").toCharArray();// 末尾添加a来辅助，避免对末尾的双引号做单独处理
        builder = new StringBuilder();
        for (char c : chars) {
            if (c == '"') {
                count++;
                builder.append('"');
            } else {
                if (count > 0) {
                    if (count % 2 == 1) {// 奇数个双引号连在一起，需要减少一个
                        builder.deleteCharAt(builder.length() - 1);
                    }
                    count = 0;
                }
                builder.append(c);
            }
        }
        String result = builder.toString();
        result = result.substring(0, result.length() - 1);// 去掉最后那个多余的a
        return result;
    }

}
