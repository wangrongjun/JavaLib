package com.wangrg.java_lib.java_program.student_manage_system.view.tab;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.contract.tab.StudentSelectedCourseContract;
import com.wangrg.java_lib.java_program.student_manage_system.controller.tab.StudentSelectedCourseController;
import com.wangrg.java_lib.java_util.TextUtil;
import com.wangrg.java_lib.swing.JOptionPaneUtil;
import com.wangrg.java_lib.swing.JTablePanel;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/13.
 */
public class StudentSelectedCoursePanel extends JPanel
        implements StudentSelectedCourseContract.IStudentSelectedCourseView {

    private StudentSelectedCourseContract.IStudentSelectedCourseController controller;

    private JTablePanel tablePanel;
    private JButton btnCancel = new JButton("退课");

    private int selectedRowIndex;

    public StudentSelectedCoursePanel() {
        setLayout(new BorderLayout());
        initComponent();

        controller = new StudentSelectedCourseController(this);// TODO Spring
        controller.clickShowSelectedCourseList(0);
    }

    private void initComponent() {
        tablePanel = new JTablePanel();
        tablePanel.addComponentToHeader(btnCancel);
        tablePanel.setOnCellSelectedListener(selectedRow -> {
            selectedRowIndex = selectedRow;
            String selected = String.valueOf(tablePanel.getTable().getValueAt(selectedRow, 4));
            btnCancel.setEnabled("已经选课".equals(selected));
        });
        tablePanel.setOnClickRefreshPageListener(() -> controller.clickRefreshSelectedCourseList());
        tablePanel.setOnClickChangePageListener(pageIndex -> controller.clickShowSelectedCourseList(pageIndex));
        add(tablePanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> cancelCourse());
    }

    @Override
    public void loading(String hint) {
        btnCancel.setEnabled(false);
        tablePanel.showLoading(hint);
    }

    @Override
    public void loadFinish() {
        tablePanel.disposeLoading();
    }

    @Override
    public void showMsg(String msg) {
        tablePanel.showMsg(msg);
    }

    @Override
    public void showSelectedCourseList(java.util.List<Course> courseList, int pageIndex, int totalPageCount) {
        // 页面刷新后需要选择了某一行才可以按，防止刷新前的页面选择的行影响到现在的页面
        btnCancel.setEnabled(false);

        String[] columnNameList = new String[]{
                "课程编号",
                "课程名称",
                "选课人数",
                "选课比例",
                "我的状态",
        };
        String[][] data = new String[courseList.size()][5];
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            String[] row = new String[5];
            row[0] = String.valueOf(course.getCourseId());
            row[1] = course.getCourseName();
            row[2] = course.getSelectedCount() + " 人";
            row[3] = TextUtil.formatDouble(course.getSelectedPercentage() * 100, 1) + "%";
            row[4] = course.isSelected() ? "已经选课" : "";
            data[i] = row;
        }

        tablePanel.showTable(columnNameList, data, pageIndex, totalPageCount);
    }

    // 能执行这个方法，selectedRowIndex 一定指向已选的课程，所以没必要再判断，大胆的执行选课操作吧！
    private void cancelCourse() {
        String courseIdString = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 0));
        String courseName = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 1));
        if (JOptionPaneUtil.showConfirm(this, "确定要退选以下课程吗？\r\n    " + courseName)) {
            int courseId = Integer.parseInt(courseIdString);
            controller.clickCancelCourse(courseId);
        }
    }

}
