package com.wangrg.java_util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        String json = JsonFormatUtil.formatJson(new Gson().toJson(object));
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
        return JsonFormatUtil.formatJson(new Gson().toJson(object));
    }

    public static <T> ArrayList<T> fromListJson(String listJson) {
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        return new Gson().fromJson(listJson, type);
    }

    public static Map fromMapJson(Class valueClass, String listJson) {
        Map<String, String> map = new Gson().fromJson(listJson, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

}
