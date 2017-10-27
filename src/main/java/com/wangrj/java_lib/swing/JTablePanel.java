package com.wangrj.java_lib.swing;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/16.
 */
public class JTablePanel extends JPanel {

    private JPanel header;
    private JLabel loadingHintLabel;
    private JScrollPane scrollPane;
    private JTable table;

    private JButton btnRefresh = new JButton("刷新");
    private JButton btnPrevious = new JButton("上一页");
    private JButton btnNext = new JButton("下一页");
    private JLabel changePageLabel = new JLabel("    0 / 0  跳转到：");
    private JTextField tfNewPage = new JTextField(3);
    private JButton btnChangePage = new JButton("跳转");

    private OnCellSelectedListener onCellSelectedListener;

    private int pageIndex;
    private int totalPageCount;

    public JTablePanel() {
        setLayout(new BorderLayout());
        initHeader();
    }

    private void initHeader() {
        header = new JPanel(new FlowLayout());
        header.add(btnRefresh);
        header.add(btnPrevious);
        header.add(btnNext);
        header.add(changePageLabel);
        header.add(tfNewPage);
        header.add(btnChangePage);
        add(header, BorderLayout.NORTH);
    }

    public void addComponentToHeader(Component component) {
        header.add(component);
    }

    public void showLoading(String hint) {
        btnRefresh.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
        btnChangePage.setEnabled(false);

        if (scrollPane != null) {
            remove(scrollPane);
            scrollPane = null;
        }
        if (loadingHintLabel == null) {
            loadingHintLabel = new JLabel(hint, JLabel.CENTER);
            loadingHintLabel.setBackground(null);
            add(loadingHintLabel, BorderLayout.CENTER);
        } else {
            loadingHintLabel.setText(hint);
        }
        loadingHintLabel.setVisible(true);
        updateUI();
    }

    public void disposeLoading() {
        btnRefresh.setEnabled(true);
        btnChangePage.setEnabled(true);
        loadingHintLabel.setVisible(false);
    }

    public void showMsg(String msg) {
        loadingHintLabel.setVisible(true);
        loadingHintLabel.setText(msg);
    }

    public void showTable(String[] columnNameList, Object[][] data,
                          int pageIndex, int totalPageCount) {

        this.pageIndex = pageIndex;
        this.totalPageCount = totalPageCount;

        btnPrevious.setEnabled(pageIndex > 0);
        btnNext.setEnabled(pageIndex < totalPageCount - 1);
        changePageLabel.setText("    " + (pageIndex + 1) + " / " + totalPageCount + "  跳转到：");

        if (scrollPane != null) {
            remove(scrollPane);
            scrollPane = null;
        }
        table = new JTable(data, columnNameList) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);// 数据不够时高度仍要占满父布局
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 每行只能单选
        if (onCellSelectedListener != null) {// 监听表格行的选中事件
            table.getSelectionModel().addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
                onCellSelectedListener.onCellSelected(model.getAnchorSelectionIndex());
            });
        }

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        updateUI();
    }

    public void setOnCellSelectedListener(OnCellSelectedListener onCellSelectedListener) {
        this.onCellSelectedListener = onCellSelectedListener;
    }

    public void setOnClickRefreshPageListener(OnClickRefreshPageListener listener) {
        btnRefresh.addActionListener(e -> listener.onClickRefreshPage());
    }

    public void setOnClickChangePageListener(OnClickChangePageListener listener) {
        btnPrevious.addActionListener(e -> listener.onClickChangePage(JTablePanel.this.pageIndex - 1));
        btnNext.addActionListener(e -> listener.onClickChangePage(JTablePanel.this.pageIndex + 1));
        btnChangePage.addActionListener(e -> {
            try {
                int newPage = Integer.parseInt(tfNewPage.getText());
                if (newPage < 1 || newPage > totalPageCount) {
                    JOptionPaneUtil.showError(JTablePanel.this, "超出范围");
                } else {
                    listener.onClickChangePage(newPage - 1);
                }
            } catch (NumberFormatException e1) {
                JOptionPaneUtil.showError(JTablePanel.this, "请输入数字");
            }
        });
    }

    public JTable getTable() {
        return table;
    }

    public interface OnCellSelectedListener {
        void onCellSelected(int selectedRowIndex);
    }

    public interface OnClickRefreshPageListener {
        void onClickRefreshPage();
    }

    public interface OnClickChangePageListener {
        void onClickChangePage(int pageIndex);
    }

}
