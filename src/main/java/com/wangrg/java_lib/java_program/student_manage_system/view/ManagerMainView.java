package com.wangrg.java_lib.java_program.student_manage_system.view;

import com.wangrg.java_lib.java_program.student_manage_system.contract.ManagerMainContract;
import com.wangrg.java_lib.java_program.student_manage_system.controller.ManagerMainController;
import com.wangrg.java_lib.java_program.student_manage_system.view.base.BaseView;
import com.wangrg.java_lib.java_program.student_manage_system.view.tab.*;
import com.wangrg.java_lib.java_util.ListUtil;
import com.wangrg.java_lib.swing.JTabbedPaneUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public class ManagerMainView extends BaseView implements ManagerMainContract.IManagerMainView {

    private JTree tree;

    private JTabbedPane tabbedPane = new JTabbedPane();
    private List<String> tabNameList = ListUtil.build("个人信息", "所有课程", "所有学生");
    private ManagerInfoPanel infoPanel;
    private ManagerCoursePanel coursePanel;
    private ManagerStudentPanel studentPanel;

    private ManagerMainContract.IManagerMainController controller;

    public ManagerMainView() {
        // TODO 改为Spring.getBean
        controller = new ManagerMainController(this);
    }

    @Override
    protected void init() {
        setTitle("学生管理系统 - 管理端");
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
        DefaultMutableTreeNode userInfoNode = new DefaultMutableTreeNode(tabNameList.get(0));
        DefaultMutableTreeNode courseNode = new DefaultMutableTreeNode(tabNameList.get(1));
        DefaultMutableTreeNode studentNode = new DefaultMutableTreeNode(tabNameList.get(2));
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("管理");
        root.add(userInfoNode);
        root.add(courseNode);
        root.add(studentNode);
        tree = new JTree(root);
        for (int i = 0; i < tree.getRowCount(); i++) {// 展开所有节点
            tree.expandRow(i);
        }

        tree.addTreeSelectionListener(e -> {
            Object node = tree.getLastSelectedPathComponent();
            if (node == userInfoNode) {
                if (infoPanel == null) {
                    infoPanel = new ManagerInfoPanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(0), infoPanel, () -> infoPanel = null);
            } else if (node == courseNode) {
                if (coursePanel == null) {
                    coursePanel = new ManagerCoursePanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(1), coursePanel, () -> coursePanel = null);
            } else if (node == studentNode) {
                if (studentPanel == null) {
                    studentPanel = new ManagerStudentPanel();
                }
                JTabbedPaneUtil.addOrShow(tabbedPane, tabNameList.get(2), studentPanel, () -> studentPanel = null);
            }
        });
    }

    @Override
    public void closeAndOpenLoginView(Class viewClass, String account, String password) {
        dispose();
        try {
            LoginView.account = account;// 这里强耦合也没关系
            LoginView.password = password;// 这里强耦合也没关系
            LoginView.identity = 1;// 这里强耦合也没关系
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
