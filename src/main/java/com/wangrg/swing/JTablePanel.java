package com.wangrg.swing;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/16.
 */
public class JTablePanel extends JPanel {

    private JLabel loadingHintLabel;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton btnRefresh = new JButton("刷新");
    private JButton btnPrevious = new JButton("上一页");
    private JButton btnNext = new JButton("下一页");
    private JLabel changePageLabel = new JLabel("    0 / 0  跳转到：");
    private JTextField tfNewPage = new JTextField(3);
    private JButton btnChangePage = new JButton("跳转");

    private int pageIndex;
    private int totalPageCount;

    public JTablePanel() {
        setLayout(new BorderLayout());
    }

}
