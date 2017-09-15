package com.wangrg.java_program.student_manage_system.view;

import com.wangrg.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrg.java_program.student_manage_system.controller.StudentMainController;
import com.wangrg.swing.JTabbedPaneUtil;
import com.wangrg.java_program.student_manage_system.view.base.BaseView;
import com.wangrg.java_program.student_manage_system.view.tab.StudentCoursePanel;
import com.wangrg.java_program.student_manage_system.view.tab.StudentInfoPanel;
import com.wangrg.java_program.student_manage_system.view.tab.StudentSelectedCoursePanel;
import com.wangrg.java_util.ListUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public class StudentMainView extends BaseView implements StudentMainContract.IStudentMainView {

    private JTree tree;

    private JTabbedPane tabbedPane = new JTabbedPane();
    private List<String> tabNameList = ListUtil.build("个人信息", "所有课程", "已选课程");
    private StudentInfoPanel infoPanel;
    private StudentCoursePanel coursePanel;
    private StudentSelectedCoursePanel selectedCoursePanel;

    private StudentMainContract.IStudentMainController controller;

    public StudentMainView() {
        // TODO 改为Spring.getBean
        controller = new StudentMainController(this);
    }

    @Override
    protected void init() {
        initMenu();
        initTree();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(tree);
        splitPane.add(tabbedPane);
        add(splitPane);
    }

    private void initMenu() {
        JMenu menuOperate = new JMenu("选项");
        JMenuItem miChangeAccount = new JMenuItem("切换帐号");
        JMenuItem miExit = new JMenuItem("退出");
        JMenu menuHelp = new JMenu("帮助");
        JMenuItem miHowToUse = new JMenuItem("如何使用");
        JMenuItem miAbout = new JMenuItem("关于");

        miChangeAccount.addActionListener(e -> controller.clickLogout());
        miExit.addActionListener(e -> controller.clickExit());
        miHowToUse.addActionListener(e -> JOptionPane.showMessageDialog(this, "如何使用"));
        miAbout.addActionListener(e -> JOptionPane.showMessageDialog(this, "关于"));

        JMenuBar menuBar = new JMenuBar();
        menuOperate.add(miChangeAccount);
        menuOperate.add(miExit);
        menuHelp.add(miHowToUse);
        menuHelp.addSeparator();
        menuHelp.add(miAbout);
        menuBar.add(menuOperate);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    private void initTree() {
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(tabNameList.get(0));
        DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(tabNameList.get(1));
        DefaultMutableTreeNode scNode = new DefaultMutableTreeNode(tabNameList.get(2));
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("信息查询");
        root.add(userNode);
        root.add(courseNode);
        root.add(scNode);
        tree = new JTree(root);
        for (int i = 0; i < tree.getRowCount(); i++) {// 展开所有节点
            tree.expandRow(i);
        }

        tree.addTreeSelectionListener(e -> {
            Object node = tree.getLastSelectedPathComponent();
            if (node == userNode) {
                if (infoPanel == null) {
                    infoPanel = new StudentInfoPanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(0), infoPanel, () -> infoPanel = null);
            } else if (node == courseNode) {
                if (coursePanel == null) {
                    coursePanel = new StudentCoursePanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(1), coursePanel, () -> coursePanel = null);
            } else if (node == scNode) {
                if (selectedCoursePanel == null) {
                    selectedCoursePanel = new StudentSelectedCoursePanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(2), selectedCoursePanel,
                        () -> selectedCoursePanel = null);
            }
        });
    }

    @Override
    public void closeAndOpenLoginView(Class viewClass, String account, String password) {
        dispose();
        try {
            LoginView.account = account;// 这里强耦合也没关系
            LoginView.password = password;// 这里强耦合也没关系
            viewClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        System.exit(0);
    }

}
