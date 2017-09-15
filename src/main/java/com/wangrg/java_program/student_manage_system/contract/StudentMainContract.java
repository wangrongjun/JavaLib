package com.wangrg.java_program.student_manage_system.contract;

/**
 * by wangrongjun on 2017/9/12.
 * <p>
 * 学生主页面的功能：
 * <p>
 * 1.点击注销按钮：controller.clickLogout()
 * 2.关闭当前窗口并跳转到登录窗口：view.closeAndOpenLoginView(Class viewClass)
 * 3.点击退出按钮：controller.clickExit()
 * 4.退出程序：view.exit()（ps：本来由controller直接完成更合适，只是为了兼容Activity，唉）
 */
public interface StudentMainContract {

    interface IStudentMainView extends BaseContract.IBaseView {
        void closeAndOpenLoginView(Class viewClass, String account, String password);

        void exit();
    }

    interface IStudentMainController extends BaseContract.IBaseController {
        void clickLogout();

        void clickExit();
    }

}
