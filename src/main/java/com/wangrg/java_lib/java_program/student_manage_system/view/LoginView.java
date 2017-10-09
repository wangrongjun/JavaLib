package com.wangrg.java_lib.java_program.student_manage_system.view;

import com.wangrg.java_lib.java_program.student_manage_system.contract.LoginContract;
import com.wangrg.java_lib.java_program.student_manage_system.controller.LoginController;
import com.wangrg.java_lib.java_program.student_manage_system.view.base.BaseView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * by wangrongjun on 2017/9/11.
 */
public class LoginView extends BaseView implements LoginContract.ILoginView {

    private JTextField tfAccount;
    private JTextField tfPassword;
    private JRadioButton rbStudent;
    private JRadioButton rbManager;
    private JButton btnLogin;

    private LoginContract.ILoginController controller;

    public static String account = "3114006535";// 注销后返回的帐户名
    public static String password = "123";// 注销后返回的密码
    public static int identity = 0;// 0代表学生，1代表管理员

    @Override
    protected void initComponent() {
        controller = new LoginController(this);// TODO Spring

        tfAccount = new JTextField(10);
        tfPassword = new JPasswordField(10);
        rbStudent = new JRadioButton("学生");
        rbManager = new JRadioButton("管理员");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbStudent);
        buttonGroup.add(rbManager);
        rbStudent.setSelected(true);
        btnLogin = new JButton("登录");
        btnLogin.addActionListener(this);

        if (account != null) {
            tfAccount.setText(account);
        }
        if (password != null) {
            tfPassword.setText(password);
        }
        rbStudent.setSelected(identity == 0);
    }

    @Override
    protected void initLayout() {
        setLayout(new FlowLayout());

        Box accountBox = Box.createHorizontalBox();
        accountBox.add(new JLabel("帐号："));
        accountBox.add(Box.createHorizontalStrut(8));
        accountBox.add(tfAccount);

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.add(new JLabel("密码："));
        passwordBox.add(Box.createHorizontalStrut(8));
        passwordBox.add(tfPassword);

        Box identityBox = Box.createHorizontalBox();
        identityBox.add(new JLabel("身份："));
        identityBox.add(Box.createHorizontalStrut(8));
        identityBox.add(rbStudent);
        identityBox.add(Box.createHorizontalStrut(8));
        identityBox.add(rbManager);

        Box loginBox = Box.createVerticalBox();
        loginBox.add(accountBox);
        loginBox.add(Box.createVerticalStrut(8));
        loginBox.add(passwordBox);
        loginBox.add(Box.createVerticalStrut(8));
        loginBox.add(identityBox);
        loginBox.add(Box.createVerticalStrut(8));
        loginBox.add(btnLogin);

        add(loginBox);
    }

    @Override
    protected void handleAction(ActionEvent e) {
        super.handleAction(e);
        if (e.getSource() == btnLogin) {
            int identity = rbManager.isSelected() ? 1 : 0;
            controller.clickLogin(tfAccount.getText(), tfPassword.getText(), identity);
        }
    }

    @Override
    public void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    public void loading(String hint) {
        btnLogin.setText(hint);
        btnLogin.setEnabled(false);
    }

    @Override
    public void loadFinish() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void closeAndOpenMainView(Class viewClass) {
        dispose();
        try {
            viewClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
