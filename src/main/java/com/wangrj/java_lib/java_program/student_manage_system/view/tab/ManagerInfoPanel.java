package com.wangrj.java_lib.java_program.student_manage_system.view.tab;

import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/18.
 * 管理员修改个人信息的界面
 */
public class ManagerInfoPanel extends JPanel {

    private IManagerService service = Spring.getBean(IManagerService.class);

    public ManagerInfoPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("管理员帐号：" + service.getManagerFromLocal().getManagerName());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

}
