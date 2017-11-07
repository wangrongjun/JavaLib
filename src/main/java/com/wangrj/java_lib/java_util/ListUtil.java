package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2017/8/14.
 */

public class ListUtil {

    @SafeVarargs
    public static <T> List<T> build(T... elementList) {
        List<T> list = new ArrayList<>();
        if (elementList != null && elementList.length > 0) {
            Collections.addAll(list, elementList);
        }
        return list;
    }

    /**
     * 如果有Null，就跳过
     */
    @SafeVarargs
    public static <T> List<T> buildWithoutNull(T... elementList) {
        List<T> list = new ArrayList<>();
        if (elementList != null && elementList.length > 0) {
            for (T t : elementList) {
                if (t != null) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * 浅克隆
     */
    public static <T> List<T> clone(List<T> list) {
//        方法一：
//        List<T> destList = new ArrayList<>();
//        destList.addAll(list);
//        return destList;
//        方法二：
        return new ArrayList<>(list);
    }

    /**
     * 指定范围的浅克隆
     *
     * @param endIndex 当endIndex等于列表长度时，相当于克隆整个列表。注意，不包括endIndex。
     */
    public static <T> List<T> clone(List<T> list, int beginIndex, int endIndex) {
        List<T> newList;
        try {
            newList = list.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            newList = new ArrayList<>();
        }
        for (int i = beginIndex; i < endIndex; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    public static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);// temp=a[i]
        list.set(i, list.get(j));// a[i]=a[j]
        list.set(j, temp);// a[j]=temp
    }

    /**
     * 对列表指定区域进行倒序
     * <p>
     * 例如列表0123456789，若指定2-6，变为01(65432)89。若指定2-7进行倒序，变为01(765432)89。
     * <p>
     * begin=2  end=6  i从2到3  swap(2,6=2+6-2) swap(3,5=2+6-3)
     * begin=2  end=7  i从2到4  swap(2,7=2+7-2) swap(3,6=2+7-3) swap(4,5=2+7-4)
     * <p>
     * 通式：begin到end  i从begin到(end-begin+1)/2+1  swap(i,begin+end-i)
     *
     * @param beginIndex 指定区域的起始位置
     * @param endIndex   指定区域的结束位置
     */
    public static void reverse(List list, int beginIndex, int endIndex) {
        for (int i = beginIndex; i <= (endIndex - beginIndex + 1) / 2 + 1; i++) {
            swap(list, i, beginIndex + endIndex - i);
        }
    }

    @Test
    public void testReverse() {
        List<Integer> oldList = build(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> newList = clone(oldList, 0, 10);
        assertEquals(10, newList.size());

        List<Integer> actual = build(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        reverse(actual, 2, 6);
        List<Integer> expect = build(0, 1, 6, 5, 4, 3, 2, 7, 8, 9);
        assertArrayEquals(expect.toArray(), actual.toArray());

        actual = build(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        reverse(actual, 2, 7);
        expect = build(0, 1, 7, 6, 5, 4, 3, 2, 8, 9);
        assertArrayEquals(expect.toArray(), actual.toArray());
    }

    /**
     * 根据提供的成员变量名及其指定值找到列表中第一个符合的元素。
     *
     * @return 如果找不到，返回Null
     */
    public static <T> T findFirst(List<T> list, String fieldName, Object value) {
        for (T element : list) {
            try {
                Field field = element.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object realValue = field.get(element);
                if (value == null && realValue == null) {
                    return element;
                }
                if (value != null && value.equals(realValue)) {
                    return element;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Test
    public void testFindFirst() {
        class User {
            String name;
            int age;
            boolean sex;

            public User(String name, int age, boolean sex) {
                this.name = name;
                this.age = age;
                this.sex = sex;
            }
        }
        User wang = new User("wang", 20, true);
        User rong = new User("rong", 20, false);
        User jun = new User("jun", 30, true);
        List<User> list = build(wang, rong, jun);
        assertEquals(wang, findFirst(list, "name", "wang"));
        assertEquals(rong, findFirst(list, "name", "rong"));
        assertEquals(wang, findFirst(list, "age", 20));
        assertEquals(jun, findFirst(list, "age", 30));
        assertEquals(wang, findFirst(list, "sex", true));
        assertEquals(rong, findFirst(list, "sex", false));
    }

}
