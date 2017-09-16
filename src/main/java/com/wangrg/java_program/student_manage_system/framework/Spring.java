package com.wangrg.java_program.student_manage_system.framework;

import com.wangrg.java_program.student_manage_system.background_executor.BackgroundExecutor;
import com.wangrg.java_program.student_manage_system.background_executor.SwingBackgroundExecutor;
import com.wangrg.java_program.student_manage_system.contract.LoginContract;
import com.wangrg.java_program.student_manage_system.contract.StudentMainContract;
import com.wangrg.java_program.student_manage_system.dao.*;
import com.wangrg.java_program.student_manage_system.dao.impl.*;
import com.wangrg.java_program.student_manage_system.service.IManagerService;
import com.wangrg.java_program.student_manage_system.service.IStudentService;
import com.wangrg.java_program.student_manage_system.service.IUserService;
import com.wangrg.java_program.student_manage_system.service.impl.ManagerServiceImpl;
import com.wangrg.java_program.student_manage_system.service.impl.StudentServiceImpl;
import com.wangrg.java_program.student_manage_system.service.impl.UserServiceImpl;
import com.wangrg.java_program.student_manage_system.view.LoginView;
import com.wangrg.java_program.student_manage_system.view.StudentMainView;

import java.util.HashMap;
import java.util.Map;

/**
 * by wangrongjun on 2017/9/12.
 */
public class Spring {

    private static Map<Class, Class> beanMap;

    private static void initMap() {
        beanMap = new HashMap<>();

        // BackgroundExecutor
        beanMap.put(BackgroundExecutor.class, SwingBackgroundExecutor.class);

        // Dao
        beanMap.put(IStudentDao.class, StudentDaoImpl.class);
        beanMap.put(IManagerDao.class, ManagerDaoImpl.class);
        beanMap.put(ICourseDao.class, CourseDaoImpl.class);
        beanMap.put(ISCDao.class, SCDaoImpl.class);
        beanMap.put(ILocationDao.class, LocationDaoImpl.class);

        // Service
        beanMap.put(IUserService.class, UserServiceImpl.class);
        beanMap.put(IStudentService.class, StudentServiceImpl.class);
        beanMap.put(IManagerService.class, ManagerServiceImpl.class);

        // Contract
        beanMap.put(LoginContract.ILoginView.class, LoginView.class);
        beanMap.put(StudentMainContract.IStudentMainView.class, StudentMainView.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanClass) {
        Class cls = getClass(beanClass);
        try {
            return (T) cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
        @SuppressWarnings("unchecked")
        private static <T> T getBeanAutoWired(Class<T> beanClass) {
            Class cls = getClass(beanClass);
            // 简陋地模拟Spring的自动装配功能：
            // 对cls的所有构造方法进行遍历：找到需要自动装配的构造方法并进行装配，只能装配一层
            Constructor[] constructors = cls.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getAnnotation(AutoWired.class) == null) {
                    continue;
                }
                // 找到有AutoWired注解修饰的构造方法，对第一个参数进行自动装配
                try {
                    Class parameterType = constructor.getParameterTypes()[0];
                    parameterType = getClass(parameterType);// 找到beanMap中与之对应的类型
                    Object parameter = parameterType.newInstance();
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance(parameter);
                } catch (Exception e) {
                    // 1.没有参数却指定了自动装配。2.自动装配的类没有无参构造方法。3.没有开启权限或方法私有。
                    throw new RuntimeException(e);
                }
            }
            // 如果来到这里，说明没有需要自动装配的构造方法，调用无参构造方法创建对象即可
            try {
                return (T) cls.newInstance();
            } catch (Exception e) {
                // 没有无参构造方法或没有用@AutoWired修饰构造方法
                throw new RuntimeException(cls.getName() + "：没有无参构造方法或没有用@AutoWired修饰构造方法", e);
            }
        }
    */

    public static Class getClass(Class beanClass) {
        if (beanMap == null) {
            initMap();
        }
        Class cls = beanMap.get(beanClass);
        if (cls == null) {
            throw new RuntimeException("bean " + beanClass.getName() + " not define");
        }
        return cls;
    }

}
