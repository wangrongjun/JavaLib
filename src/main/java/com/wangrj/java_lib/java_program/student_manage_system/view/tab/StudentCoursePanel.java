package com.wangrj.java_lib.java_program.student_manage_system.view.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentCourseContract;
import com.wangrj.java_lib.java_program.student_manage_system.controller.tab.StudentCourseController;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.swing.JOptionPaneUtil;
import com.wangrj.java_lib.swing.JTablePanel;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentCourseContract;
import com.wangrj.java_lib.java_program.student_manage_system.controller.tab.StudentCourseController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * by wangrongjun on 2017/9/13.
 */
public class StudentCoursePanel extends JPanel implements StudentCourseContract.IStudentCourseView {

    private StudentCourseContract.IStudentCourseController controller;

    private JTablePanel tablePanel;
    private JRadioButton rbSortByCourseName = new JRadioButton("按课程名称排序");
    private JRadioButton rbSortBySelectedCount = new JRadioButton("按选课人数排序");
    private JButton btnSelect = new JButton("选课");
    private JButton btnCancel = new JButton("退课");

    private int selectedRowIndex;

    public StudentCoursePanel() {
        setLayout(new BorderLayout());
        initComponent();

        controller = new StudentCourseController(this);// TODO Spring
        controller.clickShowCourseList(0, getSortType());
    }

    private void initComponent() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbSortByCourseName);
        buttonGroup.add(rbSortBySelectedCount);
        rbSortByCourseName.setSelected(true);

        Box sortBox = Box.createVerticalBox();
        sortBox.add(rbSortByCourseName);
        sortBox.add(rbSortBySelectedCount);

        tablePanel = new JTablePanel();
        tablePanel.addComponentToHeader(sortBox);
        tablePanel.addComponentToHeader(btnSelect);
        tablePanel.addComponentToHeader(btnCancel);
        tablePanel.setOnCellSelectedListener(selectedRow -> {
            selectedRowIndex = selectedRow;
            String selected = String.valueOf(tablePanel.getTable().getValueAt(selectedRow, 4));
            btnSelect.setEnabled(!"已经选课".equals(selected));
            btnCancel.setEnabled("已经选课".equals(selected));
        });
        tablePanel.setOnClickRefreshPageListener(() -> controller.clickRefreshCourseList(getSortType()));
        tablePanel.setOnClickChangePageListener(pageIndex -> controller.clickShowCourseList(pageIndex, getSortType()));
        add(tablePanel, BorderLayout.CENTER);

        btnSelect.addActionListener(e -> selectCourse());
        btnCancel.addActionListener(e -> cancelCourse());
    }

    @Override
    public void loading(String hint) {
        btnSelect.setEnabled(false);
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
    public void showCourseList(List<Course> courseList, int pageIndex, int totalPageCount) {
        // 页面刷新后需要选择了某一行才可以按，防止刷新前的页面选择的行影响到现在的页面
        btnSelect.setEnabled(false);
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

    // 能执行这个方法，selectedRowIndex 一定指向未选的课程，所以没必要再判断，大胆的执行选课操作吧！
    private void selectCourse() {
        String courseIdString = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 0));
        String courseName = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 1));
        if (JOptionPaneUtil.showConfirm(this, "确定要选择以下课程吗？\r\n    " + courseName)) {
            int courseId = Integer.parseInt(courseIdString);
            controller.clickSelectCourse(courseId);
        }
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

    private CourseSortType getSortType() {
        return rbSortByCourseName.isSelected() ?
                CourseSortType.SORT_BY_COURSE_ID :
                CourseSortType.SORT_BY_SELECTED_COUNT;
    }

}
