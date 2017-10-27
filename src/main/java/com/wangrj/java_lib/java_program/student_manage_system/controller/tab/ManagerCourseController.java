package com.wangrj.java_lib.java_program.student_manage_system.controller.tab;

import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.contract.tab.ManagerCourseContract;
import com.wangrj.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrj.java_lib.java_program.student_manage_system.service.IManagerService;
import com.wangrj.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public class ManagerCourseController implements ManagerCourseContract.IManagerCourseController {

    private static final int PAGE_SIZE = 10;

    private ManagerCourseContract.IManagerCourseView view;
    private IManagerService service;
    private CourseSortType sortType;
    private int currentPageIndex = -1;// 当前页码。如果为-1，说明是第一次显示，需要查询。否则说明已经查询。
    private int totalRecordCount;// 总记录数
    private List<Course> courseList;

    public ManagerCourseController(ManagerCourseContract.IManagerCourseView view) {
        service = Spring.getBean(IManagerService.class);
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

    @Override
    public void clickAddCourse(Course course) {
        BackgroundExecutor<Boolean> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在添加")).
                execute(() -> service.addCourse(course)).
                after((aBoolean, e) -> {
                    view.loadFinish();
                    if (aBoolean) {
                        clickRefreshCourseList(sortType);
                    } else {
                        view.showCourseList(courseList, currentPageIndex, totalRecordCount);
                        view.showError("添加失败，已有同名课程");
                    }
                });
    }

    @Override
    public void clickDeleteCourse(int courseId) {
        BackgroundExecutor<Boolean> executor = Spring.getBean(BackgroundExecutor.class);
        executor.
                before(() -> view.loading("正在删除")).
                execute(() -> service.deleteCourse(courseId)).
                after((aBoolean, e) -> {
                    view.loadFinish();
                    if (aBoolean) {
                        clickRefreshCourseList(sortType);
                    } else {
                        view.showCourseList(courseList, currentPageIndex, totalRecordCount);
                        view.showError("删除失败，该课程已经有学生选择");
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
                        courseList = service.getAllCourse(currentPageIndex, PAGE_SIZE, sortType);
                        show();
                    }
                });
    }

    private void show() {
        view.showCourseList(courseList, currentPageIndex, (totalRecordCount - 1) / PAGE_SIZE + 1);
    }

}
