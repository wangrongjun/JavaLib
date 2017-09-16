package com.wangrg.test;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * by wangrongjun on 2017/9/14.
 */
public class RxJavaTest {

    public static void main(String[] args) {
        Observable.
                create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        System.out.println("call: " + Thread.currentThread());
                        subscriber.onNext("hello");
                        subscriber.onCompleted();
                    }
                }).
                subscribeOn(Schedulers.newThread()).
                observeOn(Schedulers.io()).
                subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext: " + Thread.currentThread());
                        System.out.println("onNext: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted: " + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

}
