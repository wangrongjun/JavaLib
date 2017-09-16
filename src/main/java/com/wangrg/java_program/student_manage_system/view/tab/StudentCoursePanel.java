package com.wangrg.java_program.student_manage_system.view.tab;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.contract.tab.StudentCourseContract;
import com.wangrg.java_program.student_manage_system.controller.tab.StudentCourseController;
import com.wangrg.swing.JOptionPaneUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * by wangrongjun on 2017/9/13.
 */
public class StudentCoursePanel extends JPanel implements StudentCourseContract.IStudentCourseView {

    private StudentCourseContract.IStudentCourseController controller;

    private JLabel loadingHintLabel;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton btnRefresh = new JButton("刷新");
    private JButton btnPrevious = new JButton("上一页");
    private JButton btnNext = new JButton("下一页");
    private JLabel changePageLabel = new JLabel("    0 / 0  跳转到：");
    private JTextField tfNewPage = new JTextField(3);
    private JButton btnChangePage = new JButton("跳转");
    private JButton btnSelect = new JButton("选课");

    private int pageIndex;
    private int totalPageCount;
    private int selectedRowIndex = -1;

    public StudentCoursePanel() {
        setLayout(new BorderLayout());
        initHeader();
        controller = new StudentCourseController(this);// TODO Spring
        controller.clickShowCourseList(0);
    }

    private void initHeader() {
        btnRefresh.addActionListener(e -> controller.clickRefreshCourseList());
        btnPrevious.addActionListener(e -> controller.clickShowCourseList(pageIndex - 1));
        btnNext.addActionListener(e -> controller.clickShowCourseList(pageIndex + 1));
        btnChangePage.addActionListener(e -> changePage());
        btnSelect.addActionListener(e -> selectCourse());

        JPanel header = new JPanel(new FlowLayout());
        header.add(btnRefresh);
        header.add(btnPrevious);
        header.add(btnNext);
        header.add(changePageLabel);
        header.add(tfNewPage);
        header.add(btnChangePage);
        header.add(btnSelect);
        add(header, BorderLayout.NORTH);
    }

    @Override
    public void loading(String hint) {
        btnRefresh.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
        btnChangePage.setEnabled(false);
        btnSelect.setEnabled(false);

        if (scrollPane != null) {
            remove(scrollPane);
            scrollPane = null;
        }
        if (loadingHintLabel == null) {
            loadingHintLabel = new JLabel(hint, JLabel.CENTER);
            loadingHintLabel.setBackground(null);
            add(loadingHintLabel, BorderLayout.CENTER);
        }
        loadingHintLabel.setVisible(true);
        updateUI();
    }

    @Override
    public void loadFinish(String hint) {
        loadingHintLabel.setText(hint);
    }

    @Override
    public void showMsg(String msg) {
        JOptionPaneUtil.showInfo(this, msg);
    }

    @Override
    public void showCourseList(List<Course> courseList, int pageIndex, int totalPageCount) {
        this.pageIndex = pageIndex;
        this.totalPageCount = totalPageCount;
        btnRefresh.setEnabled(true);
        btnPrevious.setEnabled(pageIndex > 0);
        btnNext.setEnabled(pageIndex < totalPageCount - 1);
        btnChangePage.setEnabled(true);
        changePageLabel.setText("    " + (pageIndex + 1) + " / " + totalPageCount + "  跳转到：");
        selectedRowIndex = -1;// 防止在之前的页面选择的行影响到现在的页面

        String[] columnNameList = new String[]{
                "课程编号",
                "课程名称",
                "选课人数",
                "我的状态",
        };
        String[][] data = new String[courseList.size()][4];
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            String[] row = new String[4];
            row[0] = String.valueOf(course.getCourseId());
            row[1] = course.getCourseName();
            row[2] = course.getSelectedCount() + " 人";
            row[3] = course.isSelected() ? "已经选课" : "";
            data[i] = row;
        }

        showTable(columnNameList, data);
    }

    private void showTable(String[] columnNameList, String[][] data) {
        loadingHintLabel.setVisible(false);
        if (scrollPane != null) {
            remove(scrollPane);
            scrollPane = null;
        }
        table = new JTable(data, columnNameList) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);// 数据不够时高度仍要占满父布局
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只能单选
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            selectedRowIndex = model.getAnchorSelectionIndex();
            btnSelect.setEnabled(true);
        });

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        updateUI();
    }

    private void changePage() {
        try {
            int newPage = Integer.parseInt(tfNewPage.getText());
            if (newPage < 1 || newPage > totalPageCount) {
                showMsg("超出范围");
            } else {
                controller.clickShowCourseList(newPage - 1);
            }
        } catch (NumberFormatException e1) {
            showMsg("请输入数字");
        }
    }

    private void selectCourse() {
        if (selectedRowIndex < 0 || selectedRowIndex >= table.getRowCount()) {
            showMsg("请选择课程");
        } else {
            String selected = String.valueOf(table.getValueAt(selectedRowIndex, 3));
            if ("已经选课".equals(selected)) {
                showMsg("已经选了本课程");
            } else {
                String courseIdString = String.valueOf(table.getValueAt(selectedRowIndex, 0));
                String courseName = String.valueOf(table.getValueAt(selectedRowIndex, 1));
                if (JOptionPaneUtil.showConfirm(this, "确定要选择课程 " + courseName + " 吗？")) {
                    int courseId = Integer.parseInt(courseIdString);
                    controller.clickSelectCourse(courseId);
                }
            }
        }
    }

}
