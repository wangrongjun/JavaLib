package com.wangrj.java_lib.test;

public class JavaLibTestClass {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("kkk");
        System.out.print("1 / 12");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("\b");
        System.out.print("\b");
        System.out.print("\b");
        System.out.print("\b");
        System.out.print("\b");
        System.out.print("2 / 12");
    }

    public static void main1(String[] args) {
        System.out.print("正在进行第 ");
        try {
            for (int i = 0; i < 10; i++) {
                System.out.print("\b\b\b\b" + i + " 次");
                Thread.currentThread().sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
