package com.wangrg.java_program.student_manage_system.contract;

/**
 * by wangrongjun on 2017/9/12.
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
 * 4.点击删除课程按钮：controller.removeCourse
 * 5.页面显示课程列表（有分页栏）的按钮：view.showCourseList
 */
public class ManagerMainContract {

    interface ManagerMainView extends BaseContract.IBaseView {

    }

    interface ManagerMainController extends BaseContract.IBaseController {

    }

}
