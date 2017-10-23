package com.wangrg.java_lib.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * by wangrongjun on 2017/10/20.
 */
public class JavaProxyTest {

    public static void main(String[] args) {
        UserDao userDao = new JavaProxy().createProxyInstance(new UserDaoImpl());
        System.out.println(userDao.toString());
        System.out.println("---------------------------------------");
        userDao.insert();
    }

    interface UserDao {
        int insert();
    }

    static class UserDaoImpl implements UserDao {
        @Override
        public int insert() {
            System.out.println("执行方法insert");
            return 0;
        }
    }

    static class JavaProxy implements InvocationHandler {
        private Object targetObject;

        @SuppressWarnings("unchecked")
        <T> T createProxyInstance(T targetObject) {
            this.targetObject = targetObject;
            // 1.目标对象的类加载器
            // 2.目标对象实现的接口类数组
            // 3.回调对象（代理类本身）
            return (T) Proxy.newProxyInstance(
                    targetObject.getClass().getClassLoader(),
                    targetObject.getClass().getInterfaces(),
                    this
            );
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("执行方法" + method.getName() + "之前");
            Object returnValue = method.invoke(targetObject, args);
            System.out.println("执行方法" + method.getName() + "之后");
            return returnValue;
        }
    }

}
