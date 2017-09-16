package com.wangrg.java_program.student_manage_system;

import com.wangrg.swing.StyleUtil;
import com.wangrg.swing.SwingScheduler;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;

/**
 * by wangrongjun on 2017/9/13.
 */
public class WindowTest extends JFrame {

    public WindowTest() {
        super("Test");
        setSize(800, 500);
        init();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        setLayout(new FlowLayout());
        JButton button = new JButton("显示");
        button.addActionListener(e -> rx());
        add(button);
    }

    private void rx() {

        Observable.
                create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        System.out.println("call: " + Thread.currentThread());
                        subscriber.onNext("hello");
                        subscriber.onNext("world");
//                        subscriber.onCompleted();
                    }
                }).
                subscribeOn(Schedulers.newThread()).
                observeOn(SwingScheduler.awt()).
                subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("call: " + Thread.currentThread());
                        System.out.println(s);
                    }
                });
//                subscribe(new Observer<String>() {
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("onNext: " + Thread.currentThread());
//                        System.out.println("onNext: " + s);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted: " + Thread.currentThread());
//                    }
//]
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                });
    }

    public static void main(String[] args) {
        StyleUtil.InitGlobalFont(new Font("alias", Font.PLAIN, 14));
        new WindowTest();
    }

}
