package com.wangrj.java_lib.java_program.student_manage_system.controller;

import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.contract.LoginContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.ManagerMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrj.java_lib.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_program.student_manage_system.service.IUserService;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.contract.ManagerMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrj.java_lib.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_program.student_manage_system.service.IUserService;
import com.wangrj.java_lib.java_util.TextUtil;

/**
 * by wangrongjun on 2017/9/12.
 */
public class LoginController implements LoginContract.ILoginController {

    private LoginContract.ILoginView view;
    private IUserService userService;
    private IStudentService studentService;
    private IManagerService managerService;

    public LoginController(LoginContract.ILoginView view) {
        this.view = view;
        userService = Spring.getBean(IUserService.class);
        studentService = Spring.getBean(IStudentService.class);
        managerService = Spring.getBean(IManagerService.class);
    }

    @Override
    public void clickLogin(String account, String password, int identity) {
        if (TextUtil.isEmpty(account, password)) {
            view.showMsg("请输入帐号和密码");
            return;
        }
        if (identity == 0) {
            studentLogin(account, password);
        } else {
            managerLogin(account, password);
        }

//        view.loading("正在登录");
//        new Thread(() -> {
//            try {
//                if (identity == 0) {
//                    Student student = userService.studentLogin(account, password);
//                    studentService.setStudentToLocal(student);
//                    view.closeAndOpenMainView(Spring.getClass(StudentMainContract.IStudentMainView.class));
//                } else {
//                    Manager manager = userService.managerLogin(account, password);
//                    managerService.setManagerToLocal(manager);
//                    // TODO view.closeAndOpenMainView(ManagerMainView.class);
//                    view.showMsg("登录成功。欢迎您，" + manager.getManagerName());
//                }
//            } catch (AccountNotExistsException e) {
//                view.showMsg("用户不存在");
//            } catch (PasswordErrorException e) {
//                view.showMsg("密码错误");
//            }
//            view.loadFinish(null);
//        }).start();
    }

    private void studentLogin(String account, String password) {
        long studentId;
        try {
            studentId = Long.parseLong(account);
        } catch (NumberFormatException e) {
            view.showMsg("用户不存在");
            return;
        }
        BackgroundExecutor<Student> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在登录")).
                execute(() -> userService.studentLogin(studentId, password)).
                after((student, e) -> {
                    view.loadFinish();
                    if (e == null) {
                        studentService.setStudentToLocal(student);
                        view.closeAndOpenMainView(Spring.getClass(StudentMainContract.IStudentMainView.class));
                    } else if (e instanceof AccountNotExistsException) {
                        view.showMsg("用户不存在");
                    } else if (e instanceof PasswordErrorException) {
                        view.showMsg("密码错误");
                    } else {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void managerLogin(String account, String password) {
        BackgroundExecutor<Manager> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在登录")).
                execute(() -> userService.managerLogin(account, password)).
                after((manager, e) -> {
                    view.loadFinish();
                    if (e == null) {
                        managerService.setManagerToLocal(manager);
                        view.closeAndOpenMainView(Spring.getClass(ManagerMainContract.IManagerMainView.class));
                    } else if (e instanceof AccountNotExistsException) {
                        view.showMsg("用户不存在");
                    } else if (e instanceof PasswordErrorException) {
                        view.showMsg("密码错误");
                    } else {
                        throw new RuntimeException(e);
                    }
                });
    }

}
