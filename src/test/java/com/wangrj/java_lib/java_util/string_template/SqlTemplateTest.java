package com.wangrj.java_lib.java_util.string_template;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * by wangrongjun on 2018/10/10.
 */
public class SqlTemplateTest {

    @Test
    public void process() {
        String sql = "" +
                "--#if selectColumns\n" +
                "SELECT\n" +
                "  *\n" +
                "--#endif\n" +
                "FROM\n" +
                "  \"group\"\n" +
                "WHERE\n" +
                "  1 = 1\n" +
                "  --#if groupName\n" +
                "  AND group_name = :groupName\n" +
                "  --#endif\n" +
                "  --#if groupId\n" +
                "  AND group_id = :groupId\n" +
                "--#endif\n" +
                ";";

        Map<String, Object> dataModel = new HashMap<String, Object>() {{
            put("selectColumns", null);
            put("groupName", null);
            put("groupId", null);
        }};

        dataModel.remove("selectColumns");
        try {
            SqlTemplate.process(dataModel, sql);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attribute 'selectColumns' in template is not define in dataModel", e.getMessage());
        }

        dataModel.put("selectColumns", true);
        String result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("SELECT"));
        assertFalse(result.contains("group_name"));
        assertFalse(result.contains("group_id"));

        dataModel.put("groupName", "group_1");
        result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("group_name"));
        assertFalse(result.contains("group_id"));

        dataModel.put("groupId", "1");
        result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("group_id"));

        System.out.println(sql);
        System.out.println("================================");
        System.out.println(result);
    }

}