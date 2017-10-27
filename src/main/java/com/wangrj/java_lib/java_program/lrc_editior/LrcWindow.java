package com.wangrj.java_lib.java_program.lrc_editior;

import com.wangrj.java_lib.java_util.ClipboardUtil;
import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.swing.JOptionPaneUtil;
import com.wangrj.java_lib.java_util.ClipboardUtil;
import com.wangrj.java_lib.java_util.DebugUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * by wangrongjun on 2017/5/22.
 */

public class LrcWindow extends JFrame {

    private String krcFilePath;
    private JTextField tfKrcFilePath = new JTextField();
    private JRadioButton rbCharsetGBK = new JRadioButton("gbk");
    private JRadioButton rbCharsetUTF_8 = new JRadioButton("utf-8");
    private JRadioButton rbDeleteLeftWord = new JRadioButton("Yes");
    private JButton btnClear = new JButton("Clear");
    private JButton btnExecute = new JButton("Execute");
    private JSplitPane splitPane;
    private JTextArea taKrcFile = new JTextArea("drag krc file to here");
    private JTextArea taLrcString = new JTextArea();

    public static void main(String[] args) {
        new LrcWindow();
    }

    public LrcWindow() {
        super("ku_gou krc parser and lrc creator");
        initComponent();
        initLayout();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(400, 200, 600, 300);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ClipboardUtil.removeClipboardStringChangeListener();
            }
        });
        setVisible(true);
        splitPane.setDividerLocation(0.5);
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        JRadioButton rbNotDeleteLeftWord = new JRadioButton("No");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbDeleteLeftWord);
        buttonGroup.add(rbNotDeleteLeftWord);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbCharsetUTF_8);
        buttonGroup.add(rbCharsetGBK);

        Panel northTopPanel = new Panel(new BorderLayout());
        northTopPanel.add(new JLabel("krc path:"), BorderLayout.WEST);
        northTopPanel.add(tfKrcFilePath, BorderLayout.CENTER);
        Panel northBottomPanel = new Panel(new FlowLayout());
        northBottomPanel.add(new JLabel("delete left?"));
        northBottomPanel.add(rbDeleteLeftWord);
        northBottomPanel.add(rbNotDeleteLeftWord);
        northBottomPanel.add(new JLabel("charset:"));
        northBottomPanel.add(rbCharsetUTF_8);
        northBottomPanel.add(rbCharsetGBK);
        northBottomPanel.add(btnClear);
        northBottomPanel.add(btnExecute);
        Panel northPanel = new Panel(new BorderLayout());
        northPanel.add(northTopPanel, BorderLayout.NORTH);
        northPanel.add(northBottomPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(taKrcFile),
                new JScrollPane(taLrcString)
        );
        add(splitPane, BorderLayout.CENTER);
    }

    private void initComponent() {
        rbCharsetUTF_8.setSelected(true);
        tfKrcFilePath.setEditable(false);
        rbDeleteLeftWord.setSelected(true);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    execute();
                    JOptionPaneUtil.showInfo("execute successfully!!!");
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPaneUtil.showError(e1.toString());
                }
            }
        });

        new DropTarget(taKrcFile, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = event.getTransferable();
                DataFlavor[] flavors = transferable.getTransferDataFlavors();
                for (DataFlavor flavor : flavors) {
                    if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                        try {
                            List fileList = (List) transferable.getTransferData(flavor);
                            for (Object file : fileList) {
                                File f = (File) file;
                                convertKrcToLrcAndShow(f);
                            }
                        } catch (UnsupportedFlavorException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        ClipboardUtil.setOnClipboardStringChangeListener(
                new ClipboardUtil.OnClipboardStringChangeListener() {
                    @Override
                    public void onClipboardStringChange(String text) {
                        taLrcString.setText(text);
                    }
                });

    }

    private void convertKrcToLrcAndShow(File krcFile) {
        if (!krcFile.getName().endsWith(".krc")) {
            taKrcFile.setText(krcFile.getName() + " not a krc file");
            return;
        }

        krcFilePath = krcFile.getAbsolutePath();
        tfKrcFilePath.setText(krcFilePath);
        try {
            String lrc = KrcToLrc.convert(krcFilePath);
            taKrcFile.setText(lrc);
        } catch (Exception e) {
            e.printStackTrace();
            taKrcFile.setText(DebugUtil.getExceptionStackTrace(e));
        }
    }

    private void clear() {
        tfKrcFilePath.setText("");
        taKrcFile.setText("");
        taLrcString.setText("");
        krcFilePath = "";
    }

    private void execute() throws Exception {
        FileUtil.write(taKrcFile.getText(), "temp.lrc");
        FileUtil.write(taLrcString.getText(), "temp.txt");
        LrcKit.mixtureKuGou(
                new File("temp.lrc"),
                new File("temp.txt"),
                (rbCharsetUTF_8.isSelected() ? "utf-8" : "gbk"),
                rbDeleteLeftWord.isSelected()
        );
        String lrcFilePath = krcFilePath.replace(".krc", ".lrc");
        FileUtil.copy("temp.lrc", lrcFilePath);
        FileUtil.delete("temp.lrc");
        FileUtil.delete("temp.txt");
    }

}
