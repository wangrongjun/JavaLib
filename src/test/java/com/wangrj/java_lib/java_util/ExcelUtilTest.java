package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2018/6/24.
 */
public class ExcelUtilTest {

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

        ExcelUtil.excelOut(userList, "E:/Test/jxl_test.xls");
    }

    @Test
    public void testExcelIn() throws Exception {
        List<User> userList = ExcelUtil.excelIn(User.class, "E:/Test/jxl_test.xls");
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

    @Test
    public void test() {
        String excelText = "a\\tb\\tc\\r\\n\"A\\r\\nB\"\\t\"C\\r\\nD\"\\t\"E\\r\\nF\"";
        String pretreatedExcelText = "a\\tb\\tc\\r\\nA\\r\\nB\\tC\\r\\nD\\tE\\r\\nF";
        assertEquals(pretreatedExcelText, ExcelUtil.convertExcelText(excelText));

        excelText = "a\\tb\\r\\n\"1\\r\\n2\"\\t\"\"\"3\\r\\n4\"\"\"";
        pretreatedExcelText = "a\\tb\\r\\n1\\r\\n2\\t\"\"3\\r\\n4\"\"";
        assertEquals(pretreatedExcelText, ExcelUtil.convertExcelText(excelText));

//        excelText = "a\\tb\\tc\\r\\n\"12\"\\t\"3\"\"\\r\\n4\"\\t\"\"\"5\\r\\n6\"\"\"";
//        pretreatedExcelText = "a\\tb\\tc\\r\\n\"12\"\\t\"3\"\\r\\n4\"\\t\"\"5\\r\\n6\"\"";
//        assertEquals(pretreatedExcelText, ExcelUtil.convertExcelText(excelText));
    }

}
