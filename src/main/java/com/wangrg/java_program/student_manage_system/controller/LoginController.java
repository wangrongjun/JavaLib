package com.wangrg.java_program.student_manage_system.controller;

import com.wangrg.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.contract.LoginContract;
import com.wangrg.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrg.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrg.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IUserService;
import com.wangrg.java_util.TextUtil;

/**
 * by wangrongjun on 2017/9/12.
 */
public class LoginController implements LoginContract.ILoginController {

    private LoginContract.ILoginView view;
    private IUserService service;

    public LoginController(LoginContract.ILoginView view) {
        this.view = view;
        service = Spring.getBean(IUserService.class);
    }

    @Override
    public void clickLogin(String account, String password, int identity) {
        if (TextUtil.isEmpty(account, password)) {
            view.showMsg("请输入帐号和密码");
            return;
        }

        view.loading("正在登录");
        new Thread(() -> {
            try {
                if (identity == 0) {
                    Student student = service.studentLogin(account, password);
                    service.setStudentToCache(student);
                    view.closeAndOpenMainView(Spring.getClass(StudentMainContract.IStudentMainView.class));
                } else {
                    Manager manager = service.managerLogin(account, password);
                    service.setManagerToCache(manager);
                    // TODO view.closeAndOpenMainView(ManagerMainView.class);
                    view.showMsg("登录成功。欢迎您，" + manager.getManagerName());
                }
            } catch (AccountNotExistsException e) {
                view.showMsg("用户不存在");
            } catch (PasswordErrorException e) {
                view.showMsg("密码错误");
            }
            view.loadFinish(null);
        }).start();
    }

}
