package com.wangrj.java_lib.java_util.string_template;

import com.wangrj.java_lib.test.EntityExampleCreator;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * by robin.wang on 2018/10/10.
 */
public class SqlTemplateTest {

    private String sql = "" +
            "--#if selectColumns\n" +
            "SELECT\n" +
            "  *\n" +
            "--#endif\n" +
            "FROM\n" +
            "  \"group\"\n" +
            "WHERE\n" +
            "  1 = 1\n" +
            "  --#if groupId\n" +
            "  AND group_id = :groupId\n" +
            "  --#endif\n" +
            "  --#if !groupName\n" +
            "  AND group_name = :groupName\n" +
            "  --#endif\n" +
            "  --#if createdOn\n" +
            "  AND created_on > :createdOn\n" +
            "--#endif\n" +
            ";";

    @Test
    public void processMap() {
        Map<String, Object> dataModel = new HashMap<String, Object>() {{
            put("selectColumns", null);
            put("groupName", null);
            put("groupId", null);
            put("createdOn", null);
        }};

        dataModel.remove("selectColumns");
        try {
            SqlTemplate.process(sql, dataModel);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attribute 'selectColumns' in template is not defined in any of dataModels", e.getMessage());
        }

        dataModel.put("selectColumns", true);
        String result = SqlTemplate.process(sql, dataModel);
        assertTrue(result.contains("SELECT"));
        assertFalse(result.contains("group_id"));
        assertTrue(result.contains("group_name"));

        dataModel.put("groupId", 1);
        result = SqlTemplate.process(sql, dataModel);
        assertTrue(result.contains("group_id"));
        assertTrue(result.contains("group_name"));

        dataModel.put("groupName", "group_1");
        result = SqlTemplate.process(sql, dataModel);
        assertFalse(result.contains("group_name"));

        System.out.println(sql);
        System.out.println("================================");
        System.out.println(result);
    }

    public static class UserEntity {
        private Boolean selectColumns;
        private Integer groupId;
        private String groupName;
        private LocalDateTime createdOn;

        public LocalDateTime getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
        }
    }

    @Test
    public void processEntity() throws Exception {
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);
        String result = SqlTemplate.process(sql, user);
        assertTrue(result.contains("created_on"));

        user.setCreatedOn(null);
        result = SqlTemplate.process(sql, user);
        assertFalse(result.contains("created_on"));
    }

    @Test
    public void processMultiDataModel() throws Exception {
        String newSql = sql + "\n --#if flag\nABC\n --#endif";
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);
        try {
            SqlTemplate.process(newSql, user);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("flag"));
        }

        Map<String, Object> dataModel = new HashMap<String, Object>() {{
            put("flag", true);
        }};
        String result = SqlTemplate.process(newSql, user, dataModel);
        assertTrue(result.contains("ABC"));
    }

    @Test
    public void testSpeed() throws Exception {
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);

        long time1 = System.currentTimeMillis();
        SqlTemplate.process(sql, user);
        long time2 = System.currentTimeMillis();
        SqlTemplate.process(sql, user);
        long time3 = System.currentTimeMillis();

        System.out.println(time2 - time1);
        System.out.println(time3 - time2);
    }

    @Test
    public void testSyntaxError() throws Exception {
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);
        try {
            SqlTemplate.process(sql.replace("--#endif", "--#end if"), user);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Syntax Error", e.getMessage());
        }
    }

}