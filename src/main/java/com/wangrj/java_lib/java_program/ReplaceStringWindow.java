package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * by 王荣俊 on 2016/8/30.
 * <p/>
 * 关于乱码解决经验：出现乱码的主要原因是读入时使用FileUtil.read()，这个方法没有设置编码格式，
 * 所以采用了开发环境设置的utf-8格式来读取。而这个页面设置为gbk，这样就产生乱码了。
 * <p/>
 * 解决方案：严格控制读取保存的编码格式为gbk
 */
public class ReplaceStringWindow extends JFrame implements ActionListener {

    private JTextField tfOldString = new JTextField();
    private JTextField tfNewString = new JTextField();
    private JButton btnReplace = new JButton("替换");
    private JButton btnClear = new JButton("清屏");
    private JTextArea taDrag = new JTextArea("请拖动文本文件到这里\n");

    private String textFilePath;

    public static void main(String[] aa) {
        new ReplaceStringWindow();
    }

    public ReplaceStringWindow() {
        setTitle("replaceString");
        setSize(500, 400);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initTop();
        initTextArea();
        setVisible(true);
    }

    private void initTop() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);

        JLabel labelOldString = new JLabel("旧字符串:", JLabel.RIGHT);
        JLabel labelNewString = new JLabel("新字符串:", JLabel.RIGHT);

        labelOldString.setBounds(0, 0, 100, 30);
        tfOldString.setBounds(110, 0, 240, 30);
        labelNewString.setBounds(0, 40, 100, 30);
        tfNewString.setBounds(110, 40, 240, 30);
        btnReplace.setBounds(360, 10, 60, 50);
        btnClear.setBounds(430, 10, 60, 50);

        btnReplace.addActionListener(this);
        btnClear.addActionListener(this);

        topPanel.add(labelOldString);
        topPanel.add(tfOldString);
        topPanel.add(labelNewString);
        topPanel.add(tfNewString);
        topPanel.add(btnReplace);
        topPanel.add(btnClear);

        topPanel.setPreferredSize(new Dimension(500, 80));

        JPanel panel = new JPanel();
        panel.add(topPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
    }

    private void initTextArea() {

        new DropTarget(taDrag, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);

                Transferable transferable = event.getTransferable();
                DataFlavor[] flavors = transferable.getTransferDataFlavors();
                for (DataFlavor flavor : flavors) {
                    if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                        try {

                            List fileList = (List) transferable.getTransferData(flavor);
                            if (fileList != null && fileList.size() > 0) {
                                File file = (File) fileList.get(0);
                                taDrag.append("已选定：" + file.getAbsolutePath() + "\n");
                                textFilePath = file.getAbsolutePath();
                            } else {
                                taDrag.append("异常错误：fileList==null!!!");
                            }

                        } catch (UnsupportedFlavorException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        taDrag.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taDrag);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClear) {
            taDrag.setText("");
        } else if (e.getSource() == btnReplace) {
            replace();
        }
    }

    private void replace() {

        if (TextUtil.isEmpty(textFilePath)) {
            taDrag.append("请选择文本文件！\n");
            return;
        }

        if (!new File(textFilePath).exists()) {
            taDrag.append("文本文件不存在！\n");
            return;
        }

        String oldString = tfOldString.getText();
        String newString = tfNewString.getText();

        if (TextUtil.isEmpty(oldString, newString)) {
            taDrag.append("请输入新旧字符串！\n");
            return;
        }

        try {
            String text = read(textFilePath);
            text = text.replace(oldString, newString);

            save(textFilePath, text);
            taDrag.append("替换成功！！！\n");

        } catch (Exception e) {
            e.printStackTrace();
            taDrag.append(e.toString() + "\n");
            e.printStackTrace();
        }

    }

    public static void save(String textFilePath, String text) {
        try {
            FileUtil.write(text, textFilePath, "gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(String textFilePath) {
        return FileUtil.read(textFilePath, "gbk");
    }

}
