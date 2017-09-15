package com.wangrg.example;

import com.wangrg.java_util.DateUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
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

public class JFrameExample extends JFrame implements ActionListener {

    private static final String TITLE = "Wang";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    JComboBox<Object> cbLicenceType = new JComboBox<>(new Object[]{"PROFESSIONAL", "STANDARD", "BLUE", "SPRING"});
    JTextField tfUsercode = new JTextField();
    JTextField tfSystemId = new JTextField();
    JButton jbGenerateSi = new JButton("SystemId...");
    JButton jbActive = new JButton("Active");
    final JTextArea taInfo = new JTextArea("info");

    JMenuItem openFileMenu = new JMenuItem("OpenFile");
    JMenuItem savePropertiesMenu = new JMenuItem("SaveProperties");
    JMenuItem exitMenu = new JMenuItem("Exit");
    JMenuItem useMenu = new JMenuItem("How to use");
    JMenuItem aboutMenu = new JMenuItem("About");

    public JFrameExample() {
        super(TITLE);

        initMenu();
        initTopPanel();
        initCenterPanel();

//        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Operate");
        JMenu menu2 = new JMenu("Help");

        menu1.add(openFileMenu);
        menu1.add(savePropertiesMenu);
        menu1.addSeparator();
        menu1.add(exitMenu);
        menu2.add(useMenu);
        menu2.addSeparator();
        menu2.add(aboutMenu);
        openFileMenu.addActionListener(this);
        savePropertiesMenu.addActionListener(this);
        exitMenu.addActionListener(this);
        useMenu.addActionListener(this);
        aboutMenu.addActionListener(this);

        menuBar.add(menu1);
        menuBar.add(menu2);
        setJMenuBar(menuBar);
    }

    public void initCenterPanel() {
        JScrollPane scrollPane = new JScrollPane(taInfo);
        scrollPane.setBounds(100, 90, 450, 300);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        JLabel lUserCode = new JLabel("Usercode:", JLabel.RIGHT);
        JLabel lSystemId = new JLabel("SystemId:", JLabel.RIGHT);

        lUserCode.setBounds(10, 10, 85, 30);
        tfUsercode.setBounds(100, 10, 300, 30);
        cbLicenceType.setBounds(410, 10, 170, 30);
        lSystemId.setBounds(10, 50, 85, 25);
        tfSystemId.setBounds(100, 50, 300, 30);
        jbGenerateSi.setBounds(410, 50, 100, 30);
        jbActive.setBounds(500, 50, 80, 30);

        jbGenerateSi.addActionListener(this);
        jbActive.addActionListener(this);

        topPanel.add(lUserCode);
        topPanel.add(tfUsercode);
        topPanel.add(cbLicenceType);
        topPanel.add(lSystemId);
        topPanel.add(tfSystemId);
        topPanel.add(jbGenerateSi);
        topPanel.add(jbActive);

        JPanel tmpPanel = new JPanel();
        tmpPanel.add(topPanel, BorderLayout.CENTER);

        topPanel.setPreferredSize(new Dimension(600, 90));
        add(tmpPanel, BorderLayout.NORTH);

    }

    public void actionPerformed(ActionEvent paramActionEvent) {
        Object source = paramActionEvent.getSource();

        if (source == openFileMenu) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Please Choose file or directory");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setMultiSelectionEnabled(false);
//            fileChooser.setCurrentDirectory(new File("E:/"));
//            fileChooser.setFileFilter(null);
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                taInfo.append("\n\n" + DateUtil.getCurrentTime() + "\n" + file.getAbsolutePath());
            } else if (source == cbLicenceType) {
                cbLicenceType.getSelectedIndex();
                cbLicenceType.getSelectedItem();
            }

        } else if (source == exitMenu) {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        new JFrameExample();
    }

}
