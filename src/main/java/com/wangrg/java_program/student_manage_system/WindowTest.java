package com.wangrg.java_program.student_manage_system;

import com.wangrg.swing.JDialogUtil;
import com.wangrg.swing.StyleUtil;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * by wangrongjun on 2017/9/13.
 */
public class WindowTest extends JFrame {

    public WindowTest() {
        super("Test");
        setSize(800, 500);
        init();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        setLayout(new BorderLayout());
        JButton button = new JButton("显示");
        button.addActionListener(e -> JDialogUtil.showLoadingDialog(this, "正在加载"));
        add(button);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                JDialogUtil.disposeLoadingDialog();
            }
        }, 3000);
    }

    class MyDialog extends JDialog {

    }

    public static void main(String[] args) {
        StyleUtil.InitGlobalFont(new Font("alias", Font.PLAIN, 14));
        new WindowTest();
    }

}
