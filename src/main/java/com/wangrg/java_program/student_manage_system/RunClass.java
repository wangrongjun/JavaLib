package com.wangrg.java_program.student_manage_system;

import com.wangrg.java_program.student_manage_system.bean.Location;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IStudentService;
import com.wangrg.swing.StyleUtil;

import java.awt.*;

/**
 * by wangrongjun on 2017/9/12.
 */
public class RunClass {

    public static void main(String[] args) throws Exception {
        StyleUtil.InitGlobalFont(new Font("alias", Font.PLAIN, 14));

//        Spring.getClass(LoginContract.ILoginView.class).newInstance();

        Location location = new Location("中国", "广东", "广州", "番禺");
        location.setLocationId(1);
        Student student = new Student(3114006535L, "英俊", "123", location);
        Spring.getBean(IStudentService.class).setStudentToLocal(student);
        Spring.getClass(StudentMainContract.IStudentMainView.class).newInstance();
    }

}
