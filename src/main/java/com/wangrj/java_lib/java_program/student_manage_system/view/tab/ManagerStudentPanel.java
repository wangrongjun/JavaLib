package com.wangrj.java_lib.java_program.student_manage_system.view.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.ManagerStudentContract;
import com.wangrj.java_lib.java_program.student_manage_system.controller.tab.ManagerStudentController;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.swing.JOptionPaneUtil;
import com.wangrj.java_lib.swing.JTablePanel;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.ManagerStudentContract;
import com.wangrj.java_lib.java_program.student_manage_system.controller.tab.ManagerStudentController;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.swing.JOptionPaneUtil;
import com.wangrj.java_lib.swing.JTablePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * by wangrongjun on 2017/9/18.
 * 管理员管理学生的界面
 */
public class ManagerStudentPanel extends JPanel implements ManagerStudentContract.IManagerStudentView {

    private ManagerStudentContract.IManagerStudentController controller;

    private JTablePanel tablePanel;
    private JRadioButton rbSortByStudentId = new JRadioButton("按学生学号排序");
    private JRadioButton rbSortByStudentName = new JRadioButton("按学生姓名排序");
    private JRadioButton rbSortBySelecteCount = new JRadioButton("按选课数量排序");
    private JButton btnAdd = new JButton("添加");
    private JButton btnDelete = new JButton("删除");

    private int selectedRowIndex;

    public ManagerStudentPanel() {
        setLayout(new BorderLayout());
        initComponent();

        controller = new ManagerStudentController(this);// TODO Spring
        controller.clickShowStudentList(0, getSortType());
    }

    private void initComponent() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbSortByStudentId);
        buttonGroup.add(rbSortByStudentName);
        buttonGroup.add(rbSortBySelecteCount);
        rbSortByStudentId.setSelected(true);

        Box sortBox = Box.createVerticalBox();
        sortBox.add(rbSortByStudentId);
        sortBox.add(rbSortByStudentName);
        sortBox.add(rbSortBySelecteCount);

        tablePanel = new JTablePanel();
        tablePanel.addComponentToHeader(sortBox);
        tablePanel.addComponentToHeader(btnAdd);
        tablePanel.addComponentToHeader(btnDelete);
        tablePanel.setOnCellSelectedListener(selectedRow -> {
            selectedRowIndex = selectedRow;
            btnDelete.setEnabled(true);
        });
        tablePanel.setOnClickRefreshPageListener(() -> controller.clickRefreshStudentList(getSortType()));
        tablePanel.setOnClickChangePageListener(pageIndex -> controller.clickShowStudentList(pageIndex, getSortType()));
        add(tablePanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addStudent());
        btnDelete.addActionListener(e -> deleteStudent());
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
    public void showStudentList(List<Student> studentList, int pageIndex, int totalPageCount) {
        // 页面刷新后需要选择了某一行才可以按，防止刷新前的页面选择的行影响到现在的页面
        btnDelete.setEnabled(false);

        String[] columnNameList = new String[]{
                "学号",
                "姓名",
                "地址",
                "选课数量",
        };
        String[][] data = new String[studentList.size()][columnNameList.length];
        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            String[] row = new String[4];
            row[0] = String.valueOf(student.getStudentId());
            row[1] = student.getStudentName();
            row[2] = String.valueOf(student.getSelecteCourseCount());
            data[i] = row;
        }

        tablePanel.showTable(columnNameList, data, pageIndex, totalPageCount);
    }

    private void addStudent() {
        String studentIdString = JOptionPaneUtil.showInput(this, "请输入学生编号", "");
        String studentName = JOptionPaneUtil.showInput(this, "请输入学生姓名", "");
        String password = JOptionPaneUtil.showInput(this, "请输入学生登录密码", "");
        if (TextUtil.isEmpty(studentIdString, studentName, password)) {
            showError("不能为空");
        }
        long studentId = 0;
        try {
            studentId = Long.parseLong(studentIdString);
        } catch (NumberFormatException e) {
            showError("学生编号必须是数字");
        }
        Student student = new Student(studentId, studentName, password, null);
        controller.clickAddStudent(student);
    }

    private void deleteStudent() {
        String studentIdString = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 0));
        String studentName = String.valueOf(tablePanel.getTable().getValueAt(selectedRowIndex, 1));
        if (JOptionPaneUtil.showConfirm(this, "确实要删除以下学生吗？\r\n       " + studentName)) {
            int studentId = Integer.parseInt(studentIdString);
            controller.clickDeleteStudent(studentId);
        }
    }

    private StudentSortType getSortType() {
        if (rbSortByStudentId.isSelected()) {
            return StudentSortType.SORT_BY_STUDENT_ID;
        } else if (rbSortByStudentName.isSelected()) {
            return StudentSortType.SORT_BY_STUDENT_NAME;
        } else if (rbSortBySelecteCount.isSelected()) {
            return StudentSortType.SORT_BY_SELECT_COUNT;
        }
        return null;
    }

}
