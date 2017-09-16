package com.wangrg.java_program.student_manage_system.controller.tab;

import com.wangrg.java_program.student_manage_system.bean.Location;
import com.wangrg.java_program.student_manage_system.contract.tab.StudentInfoContract;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IStudentService;
import com.wangrg.java_util.LogUtil;

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
        view.loading("正在修改");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.getStudentFromLocal().setStudentName(newName);
            service.updateStudent(service.getStudentFromLocal());
            view.loadFinish("修改成功");
            view.showStudentInfo(service.getStudentFromLocal());
        }).start();
    }

    @Override
    public void clickUpdateStudentLocation(Location location) {
        LogUtil.print(String.valueOf(location));
    }

}
