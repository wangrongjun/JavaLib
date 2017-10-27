package com.wangrj.java_lib.java_program.student_manage_system;

import com.wangrj.java_lib.java_program.student_manage_system.view.LoginView;
import com.wangrj.java_lib.swing.StyleUtil;
import com.wangrj.java_lib.java_program.student_manage_system.view.LoginView;
import com.wangrj.java_lib.swing.StyleUtil;

import java.awt.*;

/**
 * by wangrongjun on 2017/9/12.
 */
public class RunClass {

    public static void main(String[] args) throws Exception {
        StyleUtil.InitGlobalFont(new Font("alias", Font.PLAIN, 14));

        new LoginView();

//        Location location = new Location("中国", "广东", "广州", "番禺");
//        location.setLocationId(1);
//        Student student = new Student(3114006535L, "英俊", "123", location);
//        Spring.getBean(IStudentService.class).setStudentToLocal(student);
//        Spring.getBean(StudentMainContract.IStudentMainView.class);

//        Spring.getBean(IManagerService.class).setManagerToLocal(new Manager("root", "123"));
//        Spring.getBean(ManagerMainContract.IManagerMainView.class);
    }

}
