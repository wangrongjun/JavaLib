package com.wangrj.java_lib.java_program.student_manage_system.controller.tab;

import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.StudentCourseContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IStudentService;
import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public class StudentCourseController implements StudentCourseContract.IStudentCourseController {

    private static final int PAGE_SIZE = 10;

    private StudentCourseContract.IStudentCourseView view;
    private IStudentService service;
    private CourseSortType sortType;
    private int currentPageIndex = -1;// 当前页码。如果为-1，说明是第一次显示，需要查询。否则说明已经查询。
    private int totalRecordCount;// 总记录数
    private List<Course> courseList;

    public StudentCourseController(StudentCourseContract.IStudentCourseView view) {
        service = Spring.getBean(IStudentService.class);
        this.view = view;
    }

    @Override
    public void clickShowCourseList(int pageIndex, CourseSortType sortType) {
        // 对查询出的totalRecordCount和courseList进行保存，避免每次切换到这个Tab时都重新加载
        this.sortType = sortType;
        if (currentPageIndex != pageIndex) {
            currentPageIndex = pageIndex;
            startLoad("正在加载");
        } else {
            show();
        }
    }

    @Override
    public void clickRefreshCourseList(CourseSortType sortType) {
        this.sortType = sortType;
        startLoad("正在刷新");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clickSelectCourse(int courseId) {
        Long studentId = service.getStudentFromLocal().getStudentId();
        BackgroundExecutor<Boolean> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在执行选课操作...")).
                execute(() -> service.selectedCourse(studentId, courseId)).
                after((succeed, e) -> {
                    view.loadFinish();
                    if (succeed) {
                        clickRefreshCourseList(sortType);
                    } else {
                        view.showMsg("选课失败");
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clickCancelCourse(int courseId) {
        Long studentId = service.getStudentFromLocal().getStudentId();
        BackgroundExecutor<Boolean> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在执行退课操作...")).
                execute(() -> service.cancelCourse(studentId, courseId)).
                after((succeed, e) -> {
                    view.loadFinish();
                    if (succeed) {
                        clickRefreshCourseList(sortType);
                    } else {
                        view.showMsg("退课失败");
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void startLoad(String loadingHint) {
        Spring.getBean(BackgroundExecutor.class).
                before(() -> view.loading(loadingHint)).
                execute(() -> {
                    totalRecordCount = service.getCourseAllCount();// TODO 优化：如果换页，不用查询数目，刷新才需要
                    return null;
                }).
                after((aVoid, e) -> {
                    view.loadFinish();
                    if (totalRecordCount == 0) {
                        view.showMsg("没有数据");
                    } else {
                        Long studentId = service.getStudentFromLocal().getStudentId();
                        courseList = service.getAllCourse(studentId, currentPageIndex, PAGE_SIZE, sortType);
                        show();
                    }
                });
    }

    private void show() {
        view.showCourseList(courseList, currentPageIndex, (totalRecordCount - 1) / PAGE_SIZE + 1);
    }

}
