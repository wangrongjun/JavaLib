package com.wangrj.java_lib.java_util;

import java.util.Arrays;
import java.util.List;


/**
 * by wangrongjun on 2017/6/21.
 */

public class LogUtil {

    public static String print(String msg, String... ignoreClass) {
        String s = get(msg, 2, Arrays.asList(ignoreClass));
        System.out.println(s);
        return s;
    }

    public static String printError(String msg, String... ignoreClass) {
        String s = get(msg, 2, Arrays.asList(ignoreClass));
        System.err.println(s);
        return s;
    }

    public static String printEntity(Object entity) {
        String s = get(GsonUtil.formatJson(entity), 2, null);
        System.out.println(s);
        return s;
    }

    public static String printEntity(Object entity, List<String> ignoreField) {
        String s = get(GsonUtil.formatJson(entity, ignoreField), 2, null);
        System.out.println(s);
        return s;
    }

    public static String printEntity(Object entity, String... ignoreField) {
        String s = get(GsonUtil.formatJson(entity, Arrays.asList(ignoreField)), 2, null);
        System.out.println(s);
        return s;
    }

    /**
     * @param ignoreClass 对于ignoreClass列表的任意一个类，遍历方法调用栈时都需要跳过该类及其子类。
     *                    例如，有继承关系：BaseDao<-OADao<-EmployeeDaoImpl。其中LogUtil.print()
     *                    方法在BaseDao的printSql()中调用。
     *                    假设OADaoTest的test方法调用了EmployeeDaoImpl的query()方法，相当于间接地
     *                    调用了printSql()方法。正常来说会打印printSql()方法所在的位置。但如果设
     *                    置了ignoreClass={"BaseDao"}，则BaseDao，OADao，EmployeeDaoImpl全部跳过，
     *                    最终打印OADaoTest的test方法调用query()所在的位置。
     */
    public static String get(String msg, String... ignoreClass) {
        return get(msg, 2, Arrays.asList(ignoreClass));
    }

    private static String get(String msg, int depth, List<String> ignoreClassList) {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        int position = depth;

        if (ignoreClassList != null && ignoreClassList.size() >= 0) {
            for (position = depth; position < elements.length; position++) {
                StackTraceElement element = elements[position];
                /*
                    当前的element的className如果是ignoreClassList中的任意
                    一个ignoreClass的类（或子类），那么ignore=true
                 */
                boolean ignore = false;
                for (String ignoreClass : ignoreClassList) {
                    try {
                        boolean extend = ReflectUtil.checkExtends(Class.forName(ignoreClass),
                                Class.forName(element.getClassName()));
                        if (extend) {
                            ignore = true;
                        }
                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();//如果ignoreClassList有错，那就直接返回当前位置吧！
                    }
                }
                if (!ignore) {
                    break;
                }
            }
        }

        String className = TextUtil.getTextAfterLastPoint(elements[position].getClassName());
        String methodName = elements[position].getMethodName();
        int callLine = elements[position].getLineNumber();//调用LogUtil.*方法所在的行
        return "-----( " + className + "-" + methodName + "[" + callLine + "]" + " )-----:\n" + msg;
    }

}
