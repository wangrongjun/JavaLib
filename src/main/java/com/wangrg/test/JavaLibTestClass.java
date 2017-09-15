package com.wangrg.test;

import javax.swing.*;

public class JavaLibTestClass {

    public static void main(String[] args) {
        int i = JOptionPane.showConfirmDialog(null, "aaa", "title", JOptionPane.YES_NO_OPTION);

        System.out.println(i==JOptionPane.OK_OPTION);
    }

}
