package com.wangrg.java_lib.java_util;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.wangrg.java_lib.java_util.TextUtil.getTextAfterLastPoint;

public class DebugUtil {

    public static boolean isDebug = true;

    /**
     * 类似System.out.println()，但会返回调用该方法的位置信息
     * <p/>
     * 例如，在WelcomeActivity的onCreate()方法调用了DebugUtil.println("your message");
     * 结果显示：
     * - - (WelcomeActivity-onCreate[29]) - - :
     * your message\n
     */
    public static String println(String message) {
        String s = getDebugMessage(message, 2) + "\n";
        if (isDebug) {
            System.out.println(s);
        }
        return s;
    }

    public static String printlnEntity(Object entity) {
        String s = getDebugMessage(GsonUtil.formatJson(entity), 2) + "\n";
        if (isDebug) {
            System.out.println(s);
        }
        return s;
    }

    public static int getErrorLine(Exception e) {
        int n = 0;
        StackTraceElement[] element = e.getStackTrace();
        if (element != null && element.length > 0) {
            n = element[0].getLineNumber();
        }
        return n;
    }

    /**
     * @param depth 该方法距离需要显示位置的方法的深度。
     *              例如fun1()调用了fun2(),fun2()调用了getDebugMessage。
     *              若设置为1，则显示fun2及其所在位置。
     *              若设置为2，则显示fun1及其所在位置。
     */
    public static String getDebugMessage(String message, int depth) {

        String className = "UnKnowClass";
        String methodName = "unKnowMethod";
        int callLine = 0;//调用getDebugMessage方法所在的行
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            StackTraceElement[] element = e.getStackTrace();
            if (element != null && element.length > depth) {
                className = element[depth].getClassName();
                className = getTextAfterLastPoint(className);
                methodName = element[depth].getMethodName();
                callLine = element[depth].getLineNumber();
            }
        }

        return " - - ( " + className + "-" + methodName + "[" + callLine + "]" + " ) - - :\n" + message;
    }

    /**
     * @param afterClass 显示这个类之后的类的调用位置。如果内不存在，则显示起始调用位置
     */
    public static String getDebugMessage(String message, String afterClass) {
        String className = "UnKnowClass";
        String methodName = "unKnowMethod";
        int callLine = 0;//调用getDebugMessage方法所在的行
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            StackTraceElement[] elementList = e.getStackTrace();
            for (int i = 0; i < elementList.length; i++) {
                System.out.println(elementList[i].getClassName() + " - " + elementList[i].getMethodName());
                String s = getTextAfterLastPoint(elementList[i].getClassName());
                // TODO 如果是afterClass的子类，也算是afterClass，跳过
//                elementList[0].getClass().getGenericSuperclass().getClass().getSimpleName();
                if (s.equals(afterClass) && i < elementList.length - 1) {
                    className = getTextAfterLastPoint(elementList[i + 1].getClassName());
                    methodName = elementList[i + 1].getMethodName();
                    callLine = elementList[i + 1].getLineNumber();
                }
            }
        }
        return " - - ( " + className + "-" + methodName + "[" + callLine + "]" + " ) - - :\n" + message;
    }

    public static String getExceptionStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String s = stringWriter.toString();
        printWriter.close();
        return s;
    }

}
