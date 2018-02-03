package com.wangrj.java_lib.java_program.rename;

import com.wangrj.java_lib.java_util.ConfigUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.swing.JOptionPaneUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * by 王荣俊 on 2016/7/26.
 */
public class RenameWindow extends JFrame implements ActionListener {

    private static final String TITLE = "多个文件重命名程序";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private int mode;
    private Renamer renamer;

    JMenuItem exitMenu = new JMenuItem("退出");
    JMenuItem useMenu = new JMenuItem("如何使用");
    JMenuItem aboutMenu = new JMenuItem("关于");

    JButton btnChooseDir = new JButton("选择目录");
    JTextField tfDir = new JTextField();
    JButton btnPreview = new JButton("预览");
    JButton btnPerform = new JButton("执行");
    JComboBox cbMode = new JComboBox<>(new Object[]{"替换", "追加前后缀"});
    JTextField tfPrefix = new JTextField(Config.defaultPrefix);
    JTextField tfSuffix = new JTextField();
    JTextField tfIncrementBegin = new JTextField(Config.defaultIncrementBegin);
    JCheckBox cbIgnoreFileSuffix = new JCheckBox("不修改文件后缀名");

    final JTextArea taRenameText = new JTextArea();
    final JTextArea taPreview = new JTextArea();

    static {
        ConfigUtil.read(Config.class, "renamerConfig.txt", true);
    }

    public RenameWindow() {
        super(TITLE);

        initMenu();
        initTopPanel();
        initCenterPanel();
        setMode(Renamer.MODE_REPLACE);

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("文件");
        JMenu menu2 = new JMenu("帮助");

        menu1.addSeparator();
        menu1.add(exitMenu);
        menu2.add(useMenu);
        menu2.addSeparator();
        menu2.add(aboutMenu);
        exitMenu.addActionListener(this);
        useMenu.addActionListener(this);
        aboutMenu.addActionListener(this);

        menuBar.add(menu1);
        menuBar.add(menu2);
        setJMenuBar(menuBar);
    }

    public void initCenterPanel() {
        add(new JScrollPane(taRenameText), BorderLayout.WEST);
        add(new JScrollPane(taPreview), BorderLayout.CENTER);

        taPreview.setEditable(false);
        taRenameText.setText("重命名文本输入区，一行对应一个文件名的替换");
    }

    public void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);

        JLabel labelMode = new JLabel("重命名模式", JLabel.RIGHT);
        JLabel labelPrefix = new JLabel("前缀", JLabel.RIGHT);
        JLabel labelSuffix = new JLabel("后缀", JLabel.RIGHT);
        JLabel labelIncrement = new JLabel("递增数字匹配符[auto_increment]，起始位置及格式：");

        btnChooseDir.setBounds(0, 0, 90, 30);
        tfDir.setBounds(100, 0, 400, 30);
        btnPreview.setBounds(510, 0, 60, 30);
        btnPerform.setBounds(580, 0, 60, 30);
        btnPerform.setEnabled(false);
        cbIgnoreFileSuffix.setSelected(true);

        labelMode.setBounds(0, 40, 90, 30);
        cbMode.setBounds(100, 40, 90, 30);
        cbIgnoreFileSuffix.setBounds(200, 40, 150, 30);
        labelPrefix.setBounds(360, 40, 60, 30);
        tfPrefix.setBounds(430, 40, 150, 30);
        labelSuffix.setBounds(590, 40, 60, 30);
        tfSuffix.setBounds(660, 40, 150, 30);
        labelIncrement.setBounds(0, 80, 400, 30);
        tfIncrementBegin.setBounds(320, 80, 70, 30);

        topPanel.add(btnChooseDir);
        topPanel.add(tfDir);
        topPanel.add(btnPreview);
        topPanel.add(btnPerform);

        topPanel.add(labelMode);
        topPanel.add(cbMode);
        topPanel.add(labelPrefix);
        topPanel.add(tfPrefix);
        topPanel.add(labelSuffix);
        topPanel.add(tfSuffix);
        topPanel.add(cbIgnoreFileSuffix);
        topPanel.add(labelIncrement);
        topPanel.add(tfIncrementBegin);

        cbMode.addActionListener(this);
        btnChooseDir.addActionListener(this);
        btnPreview.addActionListener(this);
        btnPerform.addActionListener(this);

        JPanel tmpPanel = new JPanel();
        tmpPanel.add(topPanel, "Center");

        topPanel.setPreferredSize(new Dimension(810, 120));
        add(tmpPanel, "North");
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
        Object source = paramActionEvent.getSource();
        if (source == btnChooseDir) {
            chooseDir();
        } else if (source == btnPreview) {
            preview();
        } else if (source == btnPerform) {
            perform();
        } else if (source == cbMode) {
            setMode(cbMode.getSelectedIndex());
        } else if (source == exitMenu) {
            System.exit(0);
        }
    }

    /**
     * 预览
     */
    private void perform() {
        btnPerform.setEnabled(false);
        if (renamer != null) {
            String errorList = renamer.rename();
            if (TextUtil.isEmpty(errorList)) {
                JOptionPaneUtil.showInfo("重命名成功！");
            } else {
                JOptionPaneUtil.showInfo("重命名失败！");
                taPreview.setText(errorList);
            }
        }
    }

    /**
     * 执行命名
     */
    private void preview() {
        String dir = tfDir.getText();
        String renameText = taRenameText.getText();
        boolean ignoreFileSuffix = cbIgnoreFileSuffix.isSelected();
        String prefix = tfPrefix.getText();
        String suffix = tfSuffix.getText();
        String incrementBegin = tfIncrementBegin.getText();
        if (!TextUtil.isEmpty(dir) && (!TextUtil.isEmpty(dir) || (mode != Renamer.MODE_REPLACE))) {
            renamer = new Renamer(
                    dir, renameText, mode, ignoreFileSuffix, prefix, suffix, incrementBegin);
            try {
                String preview = renamer.prepare();
                taPreview.setText(preview);
                btnPerform.setEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
                taPreview.setText("文件为空！！！\n\n" + e.toString());
            }
        } else {
            taPreview.setText("请选择目录");
        }
    }

    private void chooseDir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择html文件");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setCurrentDirectory(new File(Config.defaultDir));
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = fileChooser.getSelectedFile();
            tfDir.setText(dir.getAbsolutePath());
        } else {
            tfDir.setText("");
        }
    }

    private void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case Renamer.MODE_REPLACE:
                taRenameText.setEnabled(true);
                break;
            case Renamer.MODE_APPEND:
                taRenameText.setEnabled(false);
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        new RenameWindow();
    }

}
