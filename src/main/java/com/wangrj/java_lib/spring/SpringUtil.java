package com.wangrj.java_lib.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * by wangrongjun on 2017/10/21.
 */
public class SpringUtil {

    public static void getContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "com/wangrj/java_lib/spring/spring-context.xml");
    }

}
