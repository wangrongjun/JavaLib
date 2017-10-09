package com.wangrg.java_lib.java_program.student_manage_system.contract;

/**
 * by wangrongjun on 2017/9/12.
 * <p>
 * 管理员主页面的功能：
 * <p>
 * 1.点击注销按钮：controller.clickLogout()
 * 2.关闭当前窗口并跳转到登录窗口：view.closeAndOpenLoginView(Class viewClass)
 * 3.点击退出按钮：controller.clickExit()
 * 4.退出程序：view.exit()
 * <p>
 * <p>
 * <p>
 * <p>
 * 管理员主页面的功能：
 * <p>
 * 1.页面显示管理员个人信息：view.showManagerInfo
 * 2.点击退出按钮：controller.clickExit
 * 3.退出程序：view.exit
 * <p>
 * 1.点击显示所有学生按钮：controller.showAllStudent
 * 2.点击搜索学生按钮：controller.searchStudent
 * 3.点击添加学生按钮：controller.addStudent
 * 4.点击删除学生按钮：controller.removeStudent
 * 5.页面显示学生列表（有分页栏）的按钮：view.showStudentList
 * <p>
 * 1.点击显示所有课程按钮：controller.showAllCourse
 * 2.点击搜索课程按钮：controller.searchCourse
 * 3.点击添加课程按钮：controller.addCourse
 * 4.点击删除课程按钮：controller.deleteCourse
 * 5.页面显示课程列表（有分页栏）的按钮：view.showCourseList
 */
public interface ManagerMainContract {

    interface IManagerMainView extends BaseContract.IBaseView {
        void closeAndOpenLoginView(Class viewClass, String account, String password);

        void exit();
    }

    interface IManagerMainController extends BaseContract.IBaseController {
        void clickLogout();

        void clickExit();
    }

}
