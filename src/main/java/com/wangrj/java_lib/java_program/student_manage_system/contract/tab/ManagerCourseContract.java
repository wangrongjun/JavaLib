package com.wangrj.java_lib.java_program.student_manage_system.contract.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.BaseContract;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.BaseContract;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 * <p>
 * 管理员课程信息子页面的功能：
 * <p>
 * 1.点击显示课程列表按钮或换页按钮：controller.clickShowCourseList(int pageIndex)
 * 2.点击刷新课程列表按钮：controller.clickRefreshCourseList() - 因为controller持有pageIndex，所以这里没有参数
 * 3.页面显示课程列表：view.showCourseList(List courseList,int currentPageIndex,int totalPageCount)
 * <p>
 * 1.点击添加课程按钮：controller.clickAddCourse(Course course)
 * 1.点击删除课程按钮：controller.clickDeleteCourse(int courseId)
 */
public interface ManagerCourseContract {

    interface IManagerCourseView extends BaseContract.IBaseView {
        void showCourseList(List<Course> courseList, int pageIndex, int totalPageCount);
    }

    interface IManagerCourseController extends BaseContract.IBaseController {
        void clickShowCourseList(int pageIndex, CourseSortType sortType);

        void clickRefreshCourseList(CourseSortType sortType);

        void clickAddCourse(Course course);

        void clickDeleteCourse(int courseId);
    }

}
