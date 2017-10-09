package com.wangrg.java_lib.java_util;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

}
