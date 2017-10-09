package com.wangrg.java_lib.java_program.student_manage_system.controller.tab;

import com.wangrg.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.contract.tab.StudentSelectedCourseContract;
import com.wangrg.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_lib.java_program.student_manage_system.service.IStudentService;

import java.util.List;

/**
 * by wangrongjun on 2017/9/18.
 */
public class StudentSelectedCourseController implements StudentSelectedCourseContract.IStudentSelectedCourseController {

    private static final int PAGE_SIZE = 10;

    private StudentSelectedCourseContract.IStudentSelectedCourseView view;
    private IStudentService service;
    private int currentPageIndex = -1;// 当前页码。如果为-1，说明是第一次显示，需要查询。否则说明已经查询。
    private int totalRecordCount;// 总记录数
    private List<Course> courseList;

    public StudentSelectedCourseController(StudentSelectedCourseContract.IStudentSelectedCourseView view) {
        service = Spring.getBean(IStudentService.class);
        this.view = view;
    }

    @Override
    public void clickShowSelectedCourseList(int pageIndex) {
        // 对查询出的totalRecordCount和courseList进行保存，避免每次切换到这个Tab时都重新加载
        if (currentPageIndex != pageIndex) {
            currentPageIndex = pageIndex;
            startLoad("正在加载");
        } else {
            show();
        }
    }

    @Override
    public void clickRefreshSelectedCourseList() {
        startLoad("正在刷新");
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
                        clickRefreshSelectedCourseList();
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
                    Long studentId = service.getStudentFromLocal().getStudentId();
                    totalRecordCount = service.getCourseSelectedCount(studentId);
                    return null;
                }).
                after((aVoid, e) -> {
                    view.loadFinish();
                    if (totalRecordCount == 0) {
                        view.showMsg("没有数据");
                    } else {
                        Long studentId = service.getStudentFromLocal().getStudentId();
                        courseList = service.getSelectedCourse(studentId, currentPageIndex, PAGE_SIZE);
                        show();
                    }
                });
    }

    private void show() {
        view.showSelectedCourseList(courseList, currentPageIndex, (totalRecordCount - 1) / PAGE_SIZE + 1);
    }

}
