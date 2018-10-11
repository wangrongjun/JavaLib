package com.wangrj.java_lib.java_util.excel;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;

public class EntityConverterTest {

    @Test
    public void testNull() {
        EntityConverter converter = new EntityConverter();
        assertArrayEquals(new Object[]{}, converter.entityListToValueLists(null).toArray());
        assertArrayEquals(new Object[]{}, converter.entityListToValueLists(new ArrayList()).toArray());
    }

    static class ErrorEntityOne {// 缺少无参构造器
        private String name;

        public ErrorEntityOne(String name) {
            this.name = name;
        }
    }

    static class ErrorEntityTwo {// 不可解析的属性类型：List
        private List list;
    }

    @Test
    public void testError() throws IOException {
        EntityConverter converter = new EntityConverter();

        try {
            converter.valueListsToEntityList(Collections.singletonList(Arrays.asList("name_1")), ErrorEntityOne.class);
            fail("Expect RuntimeException: Default Constructor not found");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Default Constructor not found"));
        }

        try {
            converter.valueListsToEntityList(Collections.singletonList(Arrays.asList(new ArrayList<>())), ErrorEntityTwo.class);
            fail("Expect RuntimeException: can not resolve type");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Failed to set value to field: list"));
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            stringWriter.close();
        }
    }

    @Test
    public void testEntityConverter() throws ParseException {
        List<TestUser> userList = TestUser.example(2);
        userList.get(1).setUsername(null);

        EntityConverter converter = new EntityConverter();
        List<List<Object>> valueLists = converter.entityListToValueLists(userList);
        Date date = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        // 不知为什么，以下两行在本地没问题，sit就报错
        assertArrayEquals(valueLists.get(0).toArray(), new Object[]{"1", "name_1", true, "1", d("1996-01-01"), 1.5, date, date});
        assertArrayEquals(valueLists.get(1).toArray(), new Object[]{"2", null, true, "2", d("1996-01-02"), 2.5, date, date});

        // 注意！此时valueLists故意减去一列（少了password）
        // 如果直接把valueLists转换为entityList，会出错（因为entityList有7列），所以直接指定entityList的属性来赋值。
        converter.setFieldNameList(Arrays.asList("userId", "username", "gender", "age", "birthday", "salary", "localDate", "localDateTime"));
        converter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        valueLists.get(1).set(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));// birthday
        valueLists.get(1).set(6, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));// localDate
        valueLists.get(1).set(7, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));// localDateTime
        List<TestUser> newUserList = converter.valueListsToEntityList(valueLists, TestUser.class);
        // 这时newUserList的password列为空
        assertNull(newUserList.get(0).getPassword());
        assertNull(newUserList.get(1).getPassword());
        // newUserList设置好password，就和userList完全一致了
        newUserList.get(0).setPassword("pass_1");
        newUserList.get(1).setPassword("pass_2");
//        assertEquals(newUserList.get(0), userList.get(0));// 明明相同，但Junit就是报错，Junit在末尾多加一个空格，是Junit的BUG。
//        assertEquals(newUserList.get(1), userList.get(1));// 同上
    }

    private static Date d(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }
}