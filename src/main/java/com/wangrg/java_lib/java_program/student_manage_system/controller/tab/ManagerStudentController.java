package com.wangrg.java_lib.java_program.student_manage_system.controller.tab;

import com.wangrg.java_lib.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrg.java_lib.java_program.student_manage_system.contract.tab.ManagerStudentContract;
import com.wangrg.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_lib.java_program.student_manage_system.service.IManagerService;

import java.util.List;

/**
 * by wangrongjun on 2017/9/19.
 */
public class ManagerStudentController implements ManagerStudentContract.IManagerStudentController {

    private static final int PAGE_SIZE = 10;

    private ManagerStudentContract.IManagerStudentView view;
    private IManagerService service;
    private StudentSortType sortType;
    private int currentPageIndex = -1;// 当前页码。如果为-1，说明是第一次显示，需要查询。否则说明已经查询。
    private int totalRecordCount;// 总记录数
    private List<Student> studentList;

    public ManagerStudentController(ManagerStudentContract.IManagerStudentView view) {
        this.view = view;
        service = Spring.getBean(IManagerService.class);
    }

    @Override
    public void clickShowStudentList(int pageIndex, StudentSortType sortType) {
        this.sortType = sortType;
        if (currentPageIndex != pageIndex) {
            currentPageIndex = pageIndex;
            startLoad("正在加载");
        } else {
            show();
        }
    }

    @Override
    public void clickRefreshStudentList(StudentSortType sortType) {
        this.sortType = sortType;
        startLoad("正在刷新");
    }

    @Override
    public void clickAddStudent(Student student) {

    }

    @Override
    public void clickDeleteStudent(long studentId) {

    }

    @SuppressWarnings("unchecked")
    private void startLoad(String loadingHint) {
        Spring.getBean(BackgroundExecutor.class).
                before(() -> view.loading(loadingHint)).
                execute(() -> {
                    totalRecordCount = service.getStudentAllCount();// TODO 优化：如果换页，不用查询数目，刷新才需要
                    return null;
                }).
                after((aVoid, e) -> {
                    view.loadFinish();
                    if (totalRecordCount == 0) {
                        view.showMsg("没有数据");
                    } else {
                        studentList = service.getAllStudent(currentPageIndex, PAGE_SIZE, sortType);
                        show();
                    }
                });
    }

    private void show() {
        view.showStudentList(studentList, currentPageIndex, (totalRecordCount - 1) / PAGE_SIZE + 1);
    }

}
