package com.wangrg.swing;


import javax.swing.*;
import java.awt.*;

public class JOptionPaneUtil {

    public static void showError(Component component, String error) {
        JOptionPane.showMessageDialog(component, error);
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

    public static String showInput(Component component, String msg, String initialValue) {
        return JOptionPane.showInputDialog(component, msg, initialValue);
    }

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
