package com.wangrj.java_lib.java_util;

import com.wangrj.java_lib.db3.DbUtil;
import com.wangrj.java_lib.db3.DbUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import jxl.write.Number;
import org.junit.Test;

import javax.persistence.Id;
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

    @Test
    public void testExcelOut() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.userId = i;
            user.phone = 1302369100L + i;
            user.gender = i % 2;
            user.idNumber = 440181199202145300L + i;
            user.address = "广州市天河区天心大街" + i + "号";
            user.score1 = 600 + i * 10;
            user.score2 = 600F + i * 10;
            user.score3 = 600 + i * 10;
            user.score4 = 600D + i * 10;
            user.vip1 = i % 2 == 0;
            user.vip2 = i % 2 == 0;
            user.birthday = DateUtil.changeDate(new Date(), i * 10);
            userList.add(user);
        }

        excelOut(userList, "E:/jxl_test.xls");
    }

    @Test
    public void testExcelIn() throws Exception {
        List<User> userList = excelIn(User.class, "E:/jxl_test.xls");
        LogUtil.printEntity(userList);
    }

    static class User {
        Integer userId;
        int gender;
        long idNumber;
        Long phone;
        float score1;
        Float score2;
        double score3;
        Double score4;
        boolean vip1;
        Boolean vip2;
        String address;
        Date birthday;
    }

}
