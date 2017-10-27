package com.wangrj.java_lib.java_program.student_manage_system.controller;

import com.wangrj.java_lib.java_program.student_manage_system.contract.LoginContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;

/**
 * by wangrongjun on 2017/9/12.
 */
public class StudentMainController implements StudentMainContract.IStudentMainController {

    private StudentMainContract.IStudentMainView view;
    private IStudentService service;

    public StudentMainController(StudentMainContract.IStudentMainView view) {
        service = Spring.getBean(IStudentService.class);
        this.view = view;
    }

    @Override
    public void clickLogout() {
        view.closeAndOpenLoginView(
                Spring.getClass(LoginContract.ILoginView.class),
                service.getStudentFromLocal().getStudentId() + "",
                service.getStudentFromLocal().getPassword()
        );
        service.setStudentToLocal(null);
    }

    @Override
    public void clickExit() {
        view.exit();
    }

}
