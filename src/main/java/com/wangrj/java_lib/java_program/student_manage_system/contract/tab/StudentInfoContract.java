package com.wangrj.java_lib.java_program.student_manage_system.contract.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Location;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.contract.BaseContract;

/**
 * by wangrongjun on 2017/9/14.
 * <p>
 * 学生个人信息子页面的功能：
 * <p>
 * 1.通知需要显示个人信息：controller.clickShowStudentInfo()
 * 2.显示学生个人信息：view.showStudentInfo(Student student)
 * <p>
 * 1.点击修改姓名按钮：controller.clickUpdateStudentName(String newName)
 * 2.点击修改地址按钮：controller.clickUpdateStudentLocation(Location location)
 * 2.点击修改密码按钮：controller.clickUpdatePasswordLocation(String oldPassword,String newPassword)
 */
public interface StudentInfoContract {

    interface StudentInfoView extends BaseContract.IBaseView {
        void showStudentInfo(Student student);
    }

    interface StudentInfoController extends BaseContract.IBaseController {
        void clickShowStudentInfo();

        void clickUpdateStudentName(String newName);

        void clickUpdateStudentLocation(Location location);

        void clickUpdatePassword(String oldPassword, String newPassword);
    }

}
