package com.wangrj.java_lib.swing;


import javax.swing.*;
import java.awt.*;

public class JOptionPaneUtil {

    public static void showError(Component component, String error) {
        JOptionPane.showMessageDialog(component, error, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String error) {
        showError(null, error);
    }

    public static void showInfo(Component component, String info) {
        JOptionPane.showMessageDialog(component, info);
    }

    public static void showInfo(String info) {
        showInfo(null, info);
    }

    /**
     * @return 如果点击取消，返回null
     */
    public static String showInput(Component component, String msg, String initialValue) {
        return JOptionPane.showInputDialog(component, msg, initialValue);
    }

    /**
     * @return 如果点击取消，返回null
     */
    public static String showInput(String msg, String initialValue) {
        return showInput(null, msg, initialValue);
    }

    public static boolean showConfirm(Component component, String msg) {
        return JOptionPane.showConfirmDialog(component, msg) == JOptionPane.OK_OPTION;
    }

    public static boolean showConfirm(String msg) {
        return showConfirm(null, msg);
    }

}
