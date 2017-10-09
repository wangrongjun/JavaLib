package com.wangrg.java_lib.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * by wangrongjun on 2017/9/12.
 */
public class ProxyTest {

    public static void main(String[] args) {
        IHello hello = (IHello) Proxy.newProxyInstance(
                RealHello.class.getClassLoader(),
                RealHello.class.getInterfaces(),
                new InvocationHandlerImpl(new RealHello())
        );
        hello.hello("aaa");

        System.out.println(IHello.class.isInterface());

        System.out.println(RealHello.class.isEnum());
        System.out.println(RealHello.class.isAnnotation());
    }

    interface IHello {
        void hello(String hello);
    }

    static class RealHello implements IHello {
        @Override
        public void hello(String hello) {
            System.out.println(hello);
        }
    }

    static class InvocationHandlerImpl implements InvocationHandler {

        private IHello hello;

        public InvocationHandlerImpl(IHello hello) {
            this.hello = hello;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before");
            Object returnValue = method.invoke(hello, args);
            System.out.println("after");
            return returnValue;
        }
    }
}
