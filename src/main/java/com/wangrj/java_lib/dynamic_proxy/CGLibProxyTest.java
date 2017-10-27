package com.wangrj.java_lib.dynamic_proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * by wangrongjun on 2017/10/21.
 */
public class CGLibProxyTest {

    public static void main(String[] args) {
        User user = new CGLIBProxy().createProxyInstance(new User(1, "wang"));
        System.out.println(user.getId());
        System.out.println(user.getName());
        user.setId(2);
        user.setName("rong");
        // 输出：
        // 执行sql语句获取Id
        // 1
        // 执行sql语句获取Name
        // wang
    }

    static class User {
        private int id;
        private String name;

        public User() {
        }

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class CGLIBProxy implements MethodInterceptor {
        private Object targetObject;//代理的目标对象

        @SuppressWarnings("unchecked")
        <T> T createProxyInstance(T targetObject) {
            this.targetObject = targetObject;
            Enhancer enhancer = new Enhancer();//该类用于生成代理对象
            enhancer.setSuperclass(targetObject.getClass());//设置父类
            enhancer.setCallback(this);//设置回调对象为本身
            return (T) enhancer.create();
        }

        public Object intercept(Object proxy, Method method, Object[] args,
                                MethodProxy methodProxy) throws Throwable {
            String name = method.getName();
            if (name.startsWith("get")) {
                System.out.println("执行sql语句获取" + name.substring(3, name.length()));
            }
            return method.invoke(targetObject, args);
        }
    }

}
