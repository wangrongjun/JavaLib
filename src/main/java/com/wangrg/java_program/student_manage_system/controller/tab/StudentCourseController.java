package com.wangrg.java_program.student_manage_system.controller.tab;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.bean.SC;
import com.wangrg.java_program.student_manage_system.contract.tab.StudentCourseContract;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IStudentService;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public class StudentCourseController implements StudentCourseContract.IStudentCourseController {

    private static final int PAGE_SIZE = 10;

    private StudentCourseContract.IStudentCourseView view;
    private IStudentService service;
    private int currentPageIndex = -1;// 当前页码。如果为-1，说明是第一次显示，需要查询。否则说明已经查询。
    private int totalRecordCount;// 总记录数
    private List<Course> courseList;

    public StudentCourseController(StudentCourseContract.IStudentCourseView view) {
        service = Spring.getBean(IStudentService.class);
        this.view = view;
    }

    @Override
    public void clickShowCourseList(int pageIndex) {
        // 对查询出的totalRecordCount和courseList进行保存，避免每次切换到这个Tab时都重新加载
        if (currentPageIndex != pageIndex) {
            currentPageIndex = pageIndex;
            startLoad();
        } else {
            show();
        }
    }

    @Override
    public void clickRefreshCourseList() {
        startLoad();
    }

    @Override
    public void clickSelectCourse(int courseId) {
        Long studentId = service.getStudentFromLocal().getStudentId();
        view.loading("正在执行选课操作...");
        new Thread(() -> {
            view.loadFinish(null);
            SC sc = service.selectedCourse(studentId, courseId);
            if (sc == null) {
                view.showMsg("添加失败");
            } else {
                clickRefreshCourseList();
            }
        }).start();
    }

    private void startLoad() {
        view.loading("正在加载");
        new Thread(() -> {
            totalRecordCount = service.getCourseAllCount();// TODO 优化：如果换页，不用查询数目，只有刷新才需要
            if (totalRecordCount == 0) {
                view.loadFinish("没有数据");
            } else {
                Long studentId = service.getStudentFromLocal().getStudentId();
                courseList = service.getAllCourse(studentId, currentPageIndex, PAGE_SIZE);
                view.loadFinish("加载完成");
                show();
            }
        }).start();
    }

    private void show() {
        view.showCourseList(courseList, currentPageIndex, (totalRecordCount - 1) / PAGE_SIZE + 1);
    }

}
