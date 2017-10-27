package com.wangrj.java_lib.java_program.student_manage_system.contract.tab;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.BaseContract;

import java.util.List;

/**
 * by wangrongjun on 2017/9/18.
 * <p>
 * 管理端学生管理子页
 * <p>
 * 1.点击显示学生列表：controller.clickShowStudentList(int pageIndex)
 * 2.点击刷新：controller.clickRefreshStudentList()
 * 3.页面显示学生列表：view.showStudentList(List studentList,int pageIndex,int totalPageCount)
 * <p>
 * 1.点击添加学生按钮：controller.clickAddStudent(Student student)
 * 2.点击删除学生按钮：controller.clickDeleteStudent(long studentId)
 */
public interface ManagerStudentContract {

    interface IManagerStudentView extends BaseContract.IBaseView {
        void showStudentList(List<Student> studentList, int pageIndex, int totalPageCount);
    }

    interface IManagerStudentController extends BaseContract.IBaseController {
        void clickShowStudentList(int pageIndex, StudentSortType sortType);

        void clickRefreshStudentList(StudentSortType sortType);

        void clickAddStudent(Student student);

        void clickDeleteStudent(long studentId);
    }

}
