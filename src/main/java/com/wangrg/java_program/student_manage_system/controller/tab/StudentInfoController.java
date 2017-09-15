package com.wangrg.java_program.student_manage_system.controller.tab;

import com.wangrg.java_program.student_manage_system.bean.Location;
import com.wangrg.java_program.student_manage_system.contract.tab.StudentInfoContract;
import com.wangrg.java_program.student_manage_system.data.ILocalData;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IStudentService;
import com.wangrg.java_util.LogUtil;

/**
 * by wangrongjun on 2017/9/14.
 */
public class StudentInfoController implements StudentInfoContract.StudentInfoController {

    private StudentInfoContract.StudentInfoView view;
    private IStudentService service;
    private ILocalData localData;

    public StudentInfoController(StudentInfoContract.StudentInfoView view) {
        this.view = view;
        service = Spring.getBean(IStudentService.class);
        localData = Spring.getBean(ILocalData.class);
    }

    @Override
    public void clickShowStudentInfo() {
        view.showStudentInfo(localData.getStudent());
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
            localData.getStudent().setStudentName(newName);
            service.updateStudent(localData.getStudent());
            view.loadFinish("修改成功");
            view.showStudentInfo(localData.getStudent());
        }).start();
    }

    @Override
    public void clickUpdateStudentLocation(Location location) {
        LogUtil.print(String.valueOf(location));
    }

}
