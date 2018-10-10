package com.wangrj.java_lib.java_util.string_template;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
            put("selectColumns", true);
            put("groupId", "1");
            put("groupName", "group_1");
        }};
        String result = SqlTemplate.process(dataModel, sql);

        System.out.println(sql);
        System.out.println("================================");
        System.out.println(result);
    }

}