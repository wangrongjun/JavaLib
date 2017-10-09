package com.wangrg.java_lib.demo.file_system;

import com.wangrg.java_lib.swing.JOptionPaneUtil;
import com.wangrg.java_lib.java_util.TextUtil;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileSystemDemo implements TreeSelectionListener {

    private JTree tree;
    private FileSystemModel model;

    public static void main(String a[]) {
        new FileSystemDemo();
    }

    public FileSystemDemo() {

        //初始化数据
        model = new FileSystemModel("treeJson.txt");

        //初始化菜单
        JMenuBar menuBar = initMenuBar();

        //初始化树
        tree = new JTree(toTreeNode(model.getRootNode()));
        tree.addTreeSelectionListener(this);

        //初始化主窗口
        JFrame frame = new JFrame("FileSystemDemo");
        frame.setJMenuBar(menuBar);
        frame.add(tree);
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                model.save();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();


        final JMenu menuCreate = new JMenu("新建");
        JButton btnCreateFile = new JButton("文件");
        btnCreateFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFileUnderSelectedDir(false);
            }
        });
        JButton btnCreateDir = new JButton("文件夹");
        btnCreateDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFileUnderSelectedDir(true);
            }
        });
        menuCreate.add(btnCreateFile);
        menuCreate.add(btnCreateDir);
        menuBar.add(menuCreate);


        JButton btnRename = new JButton("重命名");
        btnRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedFileName();
            }
        });
        menuBar.add(btnRename);


        JButton btnDelete = new JButton("删除");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedFileNode();
            }
        });
        menuBar.add(btnDelete);


        JButton btnExpand = new JButton("展开");
        btnExpand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 10; i++) {
                    expand();
                }
            }
        });
        menuBar.add(btnExpand);


        JButton btnCollapse = new JButton("收缩");
        btnCollapse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapse();
            }
        });
        menuBar.add(btnCollapse);


        return menuBar;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object object = node.getUserObject();
        FileNode selectedFileNode = (FileNode) object;
        model.setSelectedFileNode(selectedFileNode);
    }

    private DefaultMutableTreeNode toTreeNode(FileNode fileNode) {
        if (fileNode == null) {
            return null;
        }
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(fileNode);
        List<FileNode> children = fileNode.getChildren();
        if (children != null && children.size() > 0) {
            for (FileNode childNode : children) {
                treeNode.add(toTreeNode(childNode));
            }
        }
        return treeNode;
    }

    private void updateTree() {
        FileNode rootNode = model.getRootNode();
        tree.setModel(new DefaultTreeModel(toTreeNode(rootNode)));
        for (int i = 0; i < 10; i++) {
            expand();
        }
    }

    private void expand() {
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tree.expandRow(i);
        }
    }

    private void collapse() {
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tree.collapseRow(i);
        }
    }

    private void createNewFileUnderSelectedDir(boolean isDir) {
        String newFileName = JOptionPaneUtil.showInput("输入名称", "");
        if (TextUtil.isEmpty(newFileName)) {
            return;
        }
        String message = model.createNewFileUnderSelectedDir(newFileName, isDir);
        if (TextUtil.isEmpty(message)) {
            updateTree();
        } else {
            JOptionPaneUtil.showError(message);
        }
    }

    private void updateSelectedFileName() {
        String newFileName = JOptionPaneUtil.showInput("修改名称", "");
        if (TextUtil.isEmpty(newFileName)) {
            return;
        }
        String message = model.updateSelectedFileName(newFileName);
        if (TextUtil.isEmpty(message)) {
            updateTree();
        } else {
            JOptionPaneUtil.showError(message);
        }
    }

    public void deleteSelectedFileNode() {
        model.deleteSelectedFileNode();
        updateTree();
    }

}