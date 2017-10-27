package com.wangrj.java_lib.java_program.student_manage_system;

import com.wangrj.java_lib.java_program.student_manage_system.bean.*;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.dao.impl.*;
import org.junit.Test;

/**
 * by wangrongjun on 2017/9/11.
 */
public class DaoTest {

    private LocationDaoImpl locationDao = new LocationDaoImpl();
    private ManagerDaoImpl managerDao = new ManagerDaoImpl();
    private StudentDaoImpl studentDao = new StudentDaoImpl();
    private CourseDaoImpl courseDao = new CourseDaoImpl();
    private SCDaoImpl scDao = new SCDaoImpl();

    public static void main(String[] args) {
        new DaoTest().testInsert();
    }

    @Test
    public void testInsert() {
        dropAndCreateTable();

        Location 番禺 = new Location("中国", "广东", "广州", "番禺");
        Location 天河 = new Location("中国", "广东", "广州", "天河");
        locationDao.insert(番禺);
        locationDao.insert(天河);

        managerDao.insert(new Manager("root", "123"));

        Student 王沣 = new Student(3114006534L, "王沣", "123", 天河);
        Student 英俊 = new Student(3114006535L, "英俊", "123", 番禺);
        Student 子桥 = new Student(3114006536L, "子桥", "123", 天河);
        studentDao.insert(王沣);
        studentDao.insert(英俊);
        studentDao.insert(子桥);

        Course c语言从入门到精通 = new Course("C语言从入门到精通");
        Course java从入门到猝死 = new Course("Java从入门到猝死");
        Course web开发从小白到大牛 = new Course("Web开发从小白到大牛");
        courseDao.insert(c语言从入门到精通);
        courseDao.insert(java从入门到猝死);
        courseDao.insert(web开发从小白到大牛);
        for (int i = 1; i <= 30; i++) {
            courseDao.insert(new Course("course_" + (i < 10 ? "0" + i : i)));
        }

        scDao.insert(new SC(英俊, c语言从入门到精通));
        scDao.insert(new SC(英俊, java从入门到猝死));
        scDao.insert(new SC(英俊, web开发从小白到大牛));
        scDao.insert(new SC(王沣, c语言从入门到精通));
        scDao.insert(new SC(王沣, java从入门到猝死));
    }

    @Test
    public void testQuery() {
//        ICourseService courseService = new CourseServiceImpl();
//        courseService.getAllCount();
//        courseService.getCourseList(0, 8);
//        courseService.getCourseList(1, 8);
//        courseService.getCourseList(2, 8);
        courseDao.queryAllWithCountAndSelectedState(3114006535L, 0, 5, CourseSortType.SORT_BY_SELECTED_COUNT);
    }

    //    @Test
    public void dropAndCreateTable() {
        scDao.dropTable();
        courseDao.dropTable();
        studentDao.dropTable();
        managerDao.dropTable();
        locationDao.dropTable();

        locationDao.createTable();
        managerDao.createTable();
        studentDao.createTable();
        courseDao.createTable();
        scDao.createTable();
    }

}
