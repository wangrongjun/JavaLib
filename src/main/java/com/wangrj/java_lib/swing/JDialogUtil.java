package com.wangrj.java_lib.swing;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/15.
 */
public class JDialogUtil {

    private static JDialog loadingDialog;

    public static void showLoadingDialog(JPanel owner, String msg) {
        showLoadingDialog(JOptionPane.getFrameForComponent(owner), msg, false);
    }

    public static void showLoadingDialog(Frame owner, String msg, boolean allowCancel) {
        loadingDialog = new JDialog(owner, "提示", true);
        loadingDialog.setSize(250, 100);
        loadingDialog.setLayout(new FlowLayout());
        loadingDialog.add(new JLabel(msg));
        if (allowCancel) {
            loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JButton button = new JButton("取消");
            button.addActionListener(e -> disposeLoadingDialog());
            loadingDialog.add(button);
        } else {
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        }

        // 设置坐标使其居中于owner组件
        Container myParent = loadingDialog.getParent();
        Point topLeft = myParent.getLocationOnScreen();
        Dimension parentSize = myParent.getSize();
        Dimension dialogSize = loadingDialog.getSize();
        int x, y;
        x = parentSize.width > dialogSize.width ?
                ((parentSize.width - dialogSize.width) / 2) + topLeft.x :
                topLeft.x;
        y = parentSize.height > dialogSize.height ?
                ((parentSize.height - dialogSize.height) / 2) + topLeft.y :
                topLeft.y;
        loadingDialog.setLocation(x, y);

        loadingDialog.setVisible(true);
    }

    public static void disposeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
            loadingDialog = null;
        }
    }

}
