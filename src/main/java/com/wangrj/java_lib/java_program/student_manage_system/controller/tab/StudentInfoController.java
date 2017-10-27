package com.wangrj.java_lib.java_program.student_manage_system.controller.tab;

import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Location;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentInfoContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentInfoContract;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_util.LogUtil;

/**
 * by wangrongjun on 2017/9/14.
 */
public class StudentInfoController implements StudentInfoContract.StudentInfoController {

    private StudentInfoContract.StudentInfoView view;
    private IStudentService service;

    public StudentInfoController(StudentInfoContract.StudentInfoView view) {
        this.view = view;
        service = Spring.getBean(IStudentService.class);
    }

    @Override
    public void clickShowStudentInfo() {
        view.showStudentInfo(service.getStudentFromLocal());
    }

    @Override
    public void clickUpdateStudentName(String newName) {
        if (TextUtil.isEmpty(newName)) {
            view.showMsg("不能为空");
            return;
        }
        BackgroundExecutor<Boolean> executor = Spring.getBean(BackgroundExecutor.class);
        executor.before(() -> view.loading("正在修改")).
                execute(() -> {
                    service.getStudentFromLocal().setStudentName(newName);
                    return service.updateStudent(service.getStudentFromLocal());
                }).
                after((aBoolean, e) -> {
                    view.loadFinish();
                    view.showStudentInfo(service.getStudentFromLocal());
                });
    }

    @Override
    public void clickUpdateStudentLocation(Location location) {
        LogUtil.print(String.valueOf(location));
    }

    @Override
    public void clickUpdatePassword(String oldPassword, String newPassword) {
        LogUtil.print(oldPassword + "  " + newPassword);
    }

}
