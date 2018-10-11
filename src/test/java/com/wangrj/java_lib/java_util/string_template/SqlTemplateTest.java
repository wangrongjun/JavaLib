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
            "  --#if groupName\n" +
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
            SqlTemplate.process(dataModel, sql);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attribute 'selectColumns' in template is not defined in dataModel", e.getMessage());
        }

        dataModel.put("selectColumns", true);
        String result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("SELECT"));
        assertTrue(!result.contains("group_id"));
        assertTrue(!result.contains("group_name"));

        dataModel.put("groupId", 1);
        result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("group_id"));
        assertTrue(!result.contains("group_name"));

        dataModel.put("groupName", "group_1");
        result = SqlTemplate.process(dataModel, sql);
        assertTrue(result.contains("group_name"));

        System.out.println(sql);
        System.out.println("================================");
        System.out.println(result);
    }

    static class BaseEntity {
        private LocalDateTime createdOn;

        public LocalDateTime getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
        }
    }

    public static class UserEntity extends BaseEntity {
        private Boolean selectColumns;
        private Integer groupId;
        private String groupName;
    }

    @Test
    public void processEntity() throws Exception {
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);
        String result = SqlTemplate.process(user, sql);
        assertTrue(result.contains("created_on"));

        user.setCreatedOn(null);
        result = SqlTemplate.process(user, sql);
        assertTrue(!result.contains("created_on"));
    }

    @Test
    public void testSpeed() throws Exception {
        UserEntity user = new EntityExampleCreator().containsSuperClassFields(true).create(UserEntity.class).get(0);

        long time1 = System.currentTimeMillis();
        SqlTemplate.process(user, sql);
        long time2 = System.currentTimeMillis();
        SqlTemplate.process(user, sql);
        long time3 = System.currentTimeMillis();

        System.out.println(time2 - time1);
        System.out.println(time3 - time2);
    }

}