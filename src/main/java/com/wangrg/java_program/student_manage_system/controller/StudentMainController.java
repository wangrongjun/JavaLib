package com.wangrg.java_program.student_manage_system.controller;

import com.wangrg.java_program.student_manage_system.contract.LoginContract;
import com.wangrg.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IUserService;

/**
 * by wangrongjun on 2017/9/12.
 */
public class StudentMainController implements StudentMainContract.IStudentMainController {

    private StudentMainContract.IStudentMainView view;
    private IUserService service;

    public StudentMainController(StudentMainContract.IStudentMainView view) {
        service = Spring.getBean(IUserService.class);
        this.view = view;
    }

    @Override
    public void clickLogout() {
        view.closeAndOpenLoginView(
                Spring.getClass(LoginContract.ILoginView.class),
                service.getStudentFromCache().getStudentId() + "",
                service.getStudentFromCache().getPassword()
        );
        service.setStudentToCache(null);
    }

    @Override
    public void clickExit() {
        view.exit();
    }

}
