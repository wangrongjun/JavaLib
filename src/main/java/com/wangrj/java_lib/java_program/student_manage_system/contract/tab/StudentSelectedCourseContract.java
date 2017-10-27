package com.wangrj.java_lib.java_program.student_manage_system.contract.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.contract.BaseContract;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 * <p>
 * 学生选课信息子页面的功能：
 * <p>
 * 1.点击显示已选课程列表按钮或换页按钮：controller.clickShowSelectedCourseList(int pageIndex)
 * 2.点击刷新已选课程列表按钮：controller.clickRefreshSelectedCourseList() - 因为controller持有pageIndex，所以这里没有参数
 * 3.页面显示学生已选课程列表：view.showSelectedCourseList(List courseList,int currentPageIndex,int totalPageIndex)
 * <p>
 * 1.点击取消选课按钮：controller.clickCancelCourse(long courseId)
 */
public interface StudentSelectedCourseContract {

    interface IStudentSelectedCourseView extends BaseContract.IBaseView {
        void showSelectedCourseList(List<Course> courseList, int pageIndex, int totalPageCount);
    }

    interface IStudentSelectedCourseController extends BaseContract.IBaseController {
        void clickShowSelectedCourseList(int pageIndex);

        void clickRefreshSelectedCourseList();

        void clickCancelCourse(int courseId);
    }

}
