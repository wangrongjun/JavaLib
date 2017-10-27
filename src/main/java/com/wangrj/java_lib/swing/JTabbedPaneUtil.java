package com.wangrj.java_lib.swing;

import javax.swing.*;

/**
 * by wangrongjun on 2017/9/13.
 */
public class JTabbedPaneUtil {

    public static boolean addOrShow(JTabbedPane tabbedPane, String tabName, JPanel tabPanel) {
        return addOrShow(tabbedPane, tabName, tabPanel, null);
    }

    /**
     * @return true创建了新的Tab。false切换Tab
     */
    public static boolean addOrShow(JTabbedPane tabbedPane, String tabName,
                                    JPanel tabContentPanel, OnTabCloseListener listener) {
        if (tabbedPane.indexOfComponent(tabContentPanel) != -1) {// 如果已存在，就切换
            tabbedPane.setSelectedComponent(tabContentPanel);
            return true;
        }

        tabbedPane.addTab(tabName, tabContentPanel);

        JPanel tabHeaderPanel = new JPanel();
        tabHeaderPanel.setBackground(null);// 设置背景透明
        tabHeaderPanel.setOpaque(false);// 设置背景透明
        while (tabName.length() < 10) {// 以防因tab名字太短导致与关闭按钮太近
            tabName += " ";
        }
        tabHeaderPanel.add(new JLabel(tabName));// JLabel默认背景透明，不用设置
        JButton btnClose = new JButton(" X ");
        btnClose.setContentAreaFilled(false);//设置按钮透明
        btnClose.setBorder(null);//设置无边框
        btnClose.addActionListener(e -> {
            tabbedPane.remove(tabContentPanel);
            if (listener != null) {
                listener.onTabClose();
            }
        });// 关闭事件
        tabHeaderPanel.add(btnClose);

        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(tabContentPanel), tabHeaderPanel);
        tabbedPane.setSelectedComponent(tabContentPanel);
        return false;
    }

    public interface OnTabCloseListener {
        void onTabClose();
    }

}
