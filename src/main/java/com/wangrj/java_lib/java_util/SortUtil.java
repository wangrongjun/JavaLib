package com.wangrj.java_lib.java_util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/5/31.
 */
public class SortUtil {

    /**
     * 对Bean对象数组的对象的某个成员变量进行中文排序，如ArrayList<User>的username成员变量
     *
     * @param sortField 排序所依据的成员变量
     * @param desc      是否从大到小
     * @return 返回新的数组，原数组不受影响
     */
    public static <T> List<T> sortChina(List<T> entityList, String sortField, boolean desc) {

        List<T> newEntityList = new ArrayList<>();
        for (T entity : entityList) {
            newEntityList.add(entity);
        }

        int len = newEntityList.size();
        for (int i = 0; i < len - 1; i++) {
            int k = i;
            for (int j = i + 1; j < len; j++) {
                if (desc) {
                    if (compare(newEntityList.get(k), newEntityList.get(j), sortField) == -1) {
                        k = j;
                    }
                } else {
                    if (compare(newEntityList.get(k), newEntityList.get(j), sortField) == 1) {
                        k = j;
                    }
                }
            }
            if (k != i) {
                T temp = newEntityList.get(k);
                newEntityList.set(k, newEntityList.get(i));
                newEntityList.set(i, temp);
            }
        }

        return newEntityList;

    }

    private static int compare(Object object1, Object object2, String compareField) {
        try {
            Field field = object1.getClass().getDeclaredField(compareField);
            field.setAccessible(true);
            String china1 = (String) field.get(object1);
            String china2 = (String) field.get(object2);
            return TextUtil.compareChinaPinYin(china1, china2);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
