package com.wangrj.java_lib.swing;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * by wangrongjun on 2017/9/16.
 */
public class SwingScheduler extends Scheduler {

    public static Scheduler awt() {
        return new SwingScheduler();
    }

    @Override
    public Worker createWorker() {
        return new MyWorker();
    }

    class MyWorker extends Scheduler.Worker {

        private volatile boolean unsubscribed;
        private SwingWorker swingWorker;

        @Override
        public Subscription schedule(Action0 action) {
            return schedule(action, 0, TimeUnit.MILLISECONDS);
        }

        /**
         * 该方法真正需要你实现的，只是保证action.call()方法在你希望的线程下执行
         */
        @Override
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (unsubscribed) {
                return Subscriptions.unsubscribed();
            }

            swingWorker = new SwingWorker() {// 在非Awt线程启动SwingWorker
                @Override
                protected Object doInBackground() throws Exception {// SwingWorker自己创建的新线程，什么也不需要做
                    return null;
                }

                @Override
                protected void done() {
                    action.call();// SwingWorker会自动切回Awt线程执行call方法
                }
            };
            swingWorker.execute();

            if (unsubscribed) {
                swingWorker.cancel(true);
                return Subscriptions.unsubscribed();
            }

            return new MySubscription(swingWorker);
        }

        @Override
        public void unsubscribe() {
            unsubscribed = true;
            swingWorker.cancel(true);
        }

        @Override
        public boolean isUnsubscribed() {
            return unsubscribed;
        }
    }

    class MySubscription implements Subscription {
        private volatile boolean unsubscribed;
        private SwingWorker swingWorker;

        MySubscription(SwingWorker swingWorker) {
            this.swingWorker = swingWorker;
        }

        @Override
        public void unsubscribe() {
            unsubscribed = true;
            swingWorker.cancel(true);
        }

        @Override
        public boolean isUnsubscribed() {
            return unsubscribed;
        }
    }

}
