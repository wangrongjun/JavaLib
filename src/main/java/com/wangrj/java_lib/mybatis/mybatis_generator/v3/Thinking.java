package com.wangrj.java_lib.mybatis.mybatis_generator.v3;

import com.wangrj.java_lib.java_program.shopping_system.bean.Shop;
import com.wangrj.java_lib.java_program.shopping_system.bean.User;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Thinking {

    public static void main(String[] args) {
        System.out.println(new A() {
            @Override
            public User query() {
                String name = getGenericClass().getName();
                System.out.println("name:   " + name);
                return null;
            }
        }.query());
        System.out.println(new B() {
        }.getGenericClass());
        System.out.println(new C() {
        }.getGenericClass());
        System.out.println(new D() {
        }.getGenericClass());
    }

    interface I<T> {
        default Class getGenericClass() {
            Class interFace = this.getClass().getInterfaces()[0];
            ParameterizedType type;
            try {
                type = (ParameterizedType) interFace.getGenericInterfaces()[0];
            } catch (ClassCastException e) {
                System.err.println("interface [" + interFace.getName() + "] should has a generic type at I");
                return Object.class;
            }
            Type[] types = type.getActualTypeArguments();
            Class genericClass = null;
            if (types != null && types.length > 0) {
                genericClass = (Class) types[0];
            }
            return genericClass;
        }
    }

    interface A extends I<User> {
        User query();
    }

    interface B extends I<Shop> {

    }

    interface C extends I<Object> {

    }

    interface D extends I {

    }
}
