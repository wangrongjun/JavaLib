package com.wangrj.java_lib.java_util;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangrj.java_lib.db3.example.bean.Dept;
import org.junit.Test;

/**
 * by 王荣俊 on 2016/5/19.
 */
public class GsonUtil {


    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static String toPrettyJson(Object object, String... ignoreFields) {
        return toJsonIgnoreFields(object, true, null, ignoreFields);
    }

    public static String printPrettyJson(Object object, String... ignoreFields) {
        String json = toPrettyJson(object, ignoreFields);
        System.out.println(json);
        return json;
    }

    /**
     * 防止使用Gson将对象类转成json时=转换为\u003d的问题
     */
    public static String formatJsonHtmlEscaping(Object object) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return JsonFormatUtil.formatJson(gson.toJson(object));
    }

    @Test
    public void testToJsonIgnoreFields() {
        Dept dept = Dept.getTestData();
        System.out.println(toJsonIgnoreFields(dept, true, null, new String[]{"Pos.dept", "Emp.pos"}));
    }

    /**
     * Gson解析时忽略某些属性
     *
     * @param ignoreFieldNameList such as "goods" or "GoodsImage.goods"
     */
    public static String toJsonIgnoreFields(Object object, boolean pretty, Class[] ignoreClassList, String[] ignoreFieldNameList) {
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (ignoreFieldNameList == null || ignoreFieldNameList.length == 0) {
                    return false;
                }
                for (String ignoreFieldName : ignoreFieldNameList) {
                    if (ignoreFieldName.contains(".")) {
                        String[] split = ignoreFieldName.split("\\.");
                        String declaringClassName = split[0];
                        ignoreFieldName = split[1];
                        if (f.getDeclaringClass().getSimpleName().equals(declaringClassName) &&
                                f.getName().equals(ignoreFieldName)) {
                            return true;
                        }
                    } else {
                        if (f.getName().equals(ignoreFieldName)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if (ignoreClassList == null || ignoreClassList.length == 0) {
                    return false;
                }
                for (Class<?> ignoreClass : ignoreClassList) {
                    if (ignoreClass.isAssignableFrom(clazz)) {
                        return true;
                    }
                }
                return false;
            }
        };

        String result;
        if (pretty) {
            result = new GsonBuilder().
                    setPrettyPrinting().
                    setExclusionStrategies(exclusionStrategy).
                    create().
                    toJson(object);
        } else {
            result = new GsonBuilder().
                    setExclusionStrategies(exclusionStrategy).
                    create().
                    toJson(object);
        }
        return result;
    }

}
