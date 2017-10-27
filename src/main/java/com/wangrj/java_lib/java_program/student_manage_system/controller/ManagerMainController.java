package com.wangrj.java_lib.java_program.student_manage_system.controller;

import com.wangrj.java_lib.java_program.student_manage_system.contract.LoginContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.ManagerMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;
import com.wangrj.java_lib.java_program.student_manage_system.contract.LoginContract;
import com.wangrj.java_lib.java_program.student_manage_system.contract.ManagerMainContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;

/**
 * by wangrongjun on 2017/9/12.
 */
public class ManagerMainController implements ManagerMainContract.IManagerMainController {

    private ManagerMainContract.IManagerMainView view;
    private IManagerService service;

    public ManagerMainController(ManagerMainContract.IManagerMainView view) {
        service = Spring.getBean(IManagerService.class);
        this.view = view;
    }

    @Override
    public void clickLogout() {
        view.closeAndOpenLoginView(
                Spring.getClass(LoginContract.ILoginView.class),
                service.getManagerFromLocal().getManagerName() + "",
                service.getManagerFromLocal().getPassword()
        );
        service.setManagerToLocal(null);
    }

    @Override
    public void clickExit() {
        view.exit();
    }

}
