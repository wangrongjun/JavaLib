package com.wangrg.java_lib.java_program;

import com.wangrg.java_lib.java_util.FileUtil;
import com.wangrg.java_lib.java_util.TextUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * by ���ٿ� on 2016/8/30.
 * <p/>
 * �������������飺�����������Ҫԭ���Ƕ���ʱʹ��FileUtil.read()���������û�����ñ����ʽ��
 * ���Բ����˿����������õ�utf-8��ʽ����ȡ�������ҳ������Ϊgbk�������Ͳ��������ˡ�
 * <p/>
 * ����������ϸ���ƶ�ȡ����ı����ʽΪgbk
 */
public class ReplaceStringWindow extends JFrame implements ActionListener {

    private JTextField tfOldString = new JTextField();
    private JTextField tfNewString = new JTextField();
    private JButton btnReplace = new JButton("�滻");
    private JButton btnClear = new JButton("����");
    private JTextArea taDrag = new JTextArea("���϶��ı��ļ�������\n");

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

        JLabel labelOldString = new JLabel("���ַ���:", JLabel.RIGHT);
        JLabel labelNewString = new JLabel("���ַ���:", JLabel.RIGHT);

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
                                taDrag.append("��ѡ����" + file.getAbsolutePath() + "\n");
                                textFilePath = file.getAbsolutePath();
                            } else {
                                taDrag.append("�쳣����fileList==null!!!");
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
            taDrag.append("��ѡ���ı��ļ���\n");
            return;
        }

        if (!new File(textFilePath).exists()) {
            taDrag.append("�ı��ļ������ڣ�\n");
            return;
        }

        String oldString = tfOldString.getText();
        String newString = tfNewString.getText();

        if (TextUtil.isEmpty(oldString, newString)) {
            taDrag.append("�������¾��ַ�����\n");
            return;
        }

        try {
            String text = read(textFilePath);
            text = text.replace(oldString, newString);

            save(textFilePath, text);
            taDrag.append("�滻�ɹ�������\n");

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
