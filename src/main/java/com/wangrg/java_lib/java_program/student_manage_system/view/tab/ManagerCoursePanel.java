package com.wangrg.java_lib.java_program.student_manage_system.view.tab;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrg.java_lib.java_program.student_manage_system.contract.tab.ManagerCourseContract;
import com.wangrg.java_lib.java_program.student_manage_system.controller.tab.ManagerCourseController;
import com.wangrg.java_lib.java_util.TextUtil;
import com.wangrg.java_lib.swing.JOptionPaneUtil;
import com.wangrg.java_lib.swing.JTablePanel;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/18.
 * 管理员管理课程的界面
 */
public class ManagerCoursePanel extends JPanel implements ManagerCourseContract.IManagerCourseView {

    protected ManagerCourseContract.IManagerCourseController controller;

    private JTablePanel tablePanel;
    private JRadioButton rbSortByCourseName = new JRadioButton("按课程名称排序");
    private JRadioButton rbSortBySelectedCount = new JRadioButton("按选课人数排序");
    private JButton btnAdd = new JButton("添加");
    private JButton btnDelete = new JButton("删除");

    private int selectedRowIndex;

    public ManagerCoursePanel() {
        setLayout(new BorderLayout());
        initComponent();

        controller = new ManagerCourseController(this);// TODO Spring
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
        tablePanel.addComponentToHeader(btnAdd);
        tablePanel.addComponentToHeader(btnDelete);
        tablePanel.setOnCellSelectedListener(selectedRow -> {
            selectedRowIndex = selectedRow;
            btnDelete.setEnabled(true);
        });
        tablePanel.setOnClickRefreshPageListener(() -> controller.clickRefreshCourseList(getSortType()));
        tablePanel.setOnClickChangePageListener(pageIndex -> controller.clickShowCourseList(pageIndex, getSortType()));
        add(tablePanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addCourse());
        btnDelete.addActionListener(e -> deleteCourse());
    }

    @Override
    public void loading(String hint) {
        btnDelete.setEnabled(false);
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
    public void showCourseList(java.util.List<Course> courseList, int pageIndex, int totalPageCount) {
        // 页面刷新后需要选择了某一行才可以按，防止刷新前的页面选择的行影响到现在的页面
        btnDelete.setEnabled(false);

        String[] columnNameList = new String[]{
                "课程编号",
                "课程名称",
                "选课人数",
                "选课比例",
        };
        String[][] data = new String[courseList.size()][4];
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            String[] row = new String[4];
            row[0] = String.valueOf(course.getCourseId());
            row[1] = course.getCourseName();
            row[2] = course.getSelectedCount() + " 人";
            row[3] = TextUtil.formatDouble(course.getSelectedPercentage() * 100, 1) + "%";
            data[i] = row;
        }

        tablePanel.showTable(columnNameList, data, pageIndex, totalPageCount);
    }

    private void addCourse() {
        String input = JOptionPaneUtil.showInput(this, "请输入新课程的名称", "");
        if (TextUtil.isEmpty(input)) {
            showError("不能为空");
            return;
        }
        controller.clickAddCourse(new Course(input));
    }

    private void deleteCourse() {
        String courseIdString = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 0));
        String courseName = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 1));
        if (JOptionPaneUtil.showConfirm(this, "确实要删除以下课程吗？\r\n       " + courseName)) {
            int courseId = Integer.parseInt(courseIdString);
            controller.clickDeleteCourse(courseId);
        }
    }

    private CourseSortType getSortType() {
        return rbSortByCourseName.isSelected() ?
                CourseSortType.SORT_BY_COURSE_ID :
                CourseSortType.SORT_BY_SELECTED_COUNT;
    }

}
