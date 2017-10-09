package com.wangrg.java_lib.demo.calculator.ui;

import javax.swing.JOptionPane;

public class MyJOptionPane {
    public static void showErrorPane(String s) {
        JOptionPane.showMessageDialog(null, s, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoPane(String s) {
        JOptionPane.showMessageDialog(null, s, "提示",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
