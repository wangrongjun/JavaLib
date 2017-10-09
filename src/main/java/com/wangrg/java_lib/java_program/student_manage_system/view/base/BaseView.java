package com.wangrg.java_lib.java_program.student_manage_system.view.base;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * by wangrongjun on 2017/9/11.
 */
public abstract class BaseView extends JFrame implements ActionListener {

    public BaseView() {
        super("学生管理系统");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocation(420, 20);
        // 避免还没创建完就调用init，导致子类的成员变量无法赋值
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                init();
                initComponent();
                initLayout();
                setVisible(true);
            }
        }, 10);
    }

    protected void init() {
    }

    protected void initComponent() {
    }

    protected void initLayout() {
    }

    protected void handleAction(ActionEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleAction(e);
    }
}
