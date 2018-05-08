package com.wangrj.java_lib.java_util;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wangrj.java_lib.db3.example.bean.Dept;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * by 王荣俊 on 2016/5/19.
 */
public class GsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static String printFormatJson(Object object) {
        String json = formatJson(object);
        System.out.println(json);
        return json;
    }

    public static String printFormatJson(Object object, List<String> ignoreField) {
        String json = formatJson(object, ignoreField);
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

    public static String formatJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static String formatJson(Object object, List<String> ignoreField) {
        Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (ignoreField == null || ignoreField.size() == 0) {
                    return false;
                }
                for (String s : ignoreField) {
                    if (f.getName().equals(s)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        return gson.toJson(object);
    }

    public static <T> List<T> fromListJson(String listJson) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return new Gson().fromJson(listJson, type);
    }

    public static Map fromMapJson(Class valueClass, String listJson) {
        Map<String, String> map = new Gson().fromJson(listJson, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    @Test
    public void testToJsonIgnoreFields() {
        Dept dept = Dept.getTestData();
        System.out.println(toJsonIgnoreFields(dept, "Pos.dept", "Emp.pos"));
    }

    /**
     * Gson解析时忽略某些属性
     *
     * @param ignoreFieldNameList such as "goods" or "GoodsImage.goods"
     */
    public static String toJsonIgnoreFields(Object object, String... ignoreFieldNameList) {
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (ignoreFieldNameList == null || ignoreFieldNameList.length == 0) {
                    return false;
                }
                // TODO 把循环得出结果用一个Map来缓存
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
                return false;
            }
        };

        return new GsonBuilder().
                setPrettyPrinting().
                setExclusionStrategies(exclusionStrategy).
                create().
                toJson(object);
    }

}
