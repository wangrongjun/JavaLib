package com.wangrj.java_lib.java_program.student_manage_system.view.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentInfoContract;
import com.wangrj.java_lib.java_program.student_manage_system.controller.tab.StudentInfoController;
import com.wangrj.java_lib.swing.JOptionPaneUtil;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/13.
 */
public class StudentInfoPanel extends JPanel implements StudentInfoContract.StudentInfoView {

    private StudentInfoContract.StudentInfoController controller;

    private Box userInfoBox;
    private JLabel loadingHintLabel;

    private Student student;

    public StudentInfoPanel() {
        initComponent();
        controller = new StudentInfoController(this);// TODO Spring
        controller.clickShowStudentInfo();
    }

    private void initComponent() {
        setLayout(new FlowLayout());

        loadingHintLabel = new JLabel("", JLabel.CENTER);
        loadingHintLabel.setVisible(false);
        add(loadingHintLabel);

        userInfoBox = Box.createHorizontalBox();
        userInfoBox.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(userInfoBox);

        JButton btnUpdateName = new JButton("修改姓名");
        JButton btnUpdateLocation = new JButton("修改住址");
        btnUpdateName.addActionListener(e -> updateName());
        btnUpdateLocation.addActionListener(e -> updateLocation());
        add(btnUpdateName);
        add(btnUpdateLocation);
    }

    @Override
    public void loading(String hint) {
        userInfoBox.setVisible(false);
        loadingHintLabel.setText(hint);
        loadingHintLabel.setVisible(true);
    }

    @Override
    public void loadFinish() {
        loadingHintLabel.setVisible(false);
        userInfoBox.setVisible(true);
    }

    private void updateName() {
        String newName = JOptionPaneUtil.showInput(this, "输入新的姓名", student.getStudentName());
        if (newName != null) {
            controller.clickUpdateStudentName(newName);
        }
    }

    private void updateLocation() {

    }

    public void showStudentInfo(Student student) {
        this.student = student;

        Box labelBox = Box.createVerticalBox();
        labelBox.add(new JLabel("学号："));
        labelBox.add(Box.createVerticalStrut(8));
        labelBox.add(new JLabel("姓名："));
        labelBox.add(Box.createVerticalStrut(8));
        labelBox.add(new JLabel("住址："));

        Box msgBox = Box.createVerticalBox();
        msgBox.add(new JLabel(student.getStudentId() + ""));
        msgBox.add(Box.createVerticalStrut(8));
        msgBox.add(new JLabel(student.getStudentName()));
        msgBox.add(Box.createVerticalStrut(8));
        msgBox.add(new JLabel(String.valueOf(student.getLocation())));

        userInfoBox.removeAll();
        userInfoBox.add(labelBox);
        userInfoBox.add(Box.createHorizontalStrut(12));
        userInfoBox.add(msgBox);
        updateUI();
    }

}
