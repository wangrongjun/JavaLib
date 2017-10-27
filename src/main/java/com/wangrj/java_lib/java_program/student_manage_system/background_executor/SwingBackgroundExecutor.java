package com.wangrj.java_lib.java_program.student_manage_system.background_executor;

import javax.swing.*;

/**
 * by wangrongjun on 2017/9/15.
 */
public class SwingBackgroundExecutor<Result> extends BackgroundExecutor<Result> {

    private Exception e;

    @Override
    void execute(FunBeforeExecute funBeforeExecute,
                 FunExecuteInBackground<Result> funExecuteInBackground,
                 FunAfterExecute<Result> resultFunAfterExecute) {
        if (funBeforeExecute != null) {
            funBeforeExecute.beforeExecute();
        }

        new SwingWorker<Result, Void>() {
            @Override
            protected Result doInBackground() {
                try {
//                    Thread.sleep(1000);// TODO delete
                    return funExecuteInBackground.executeInBackground();
                } catch (Exception e1) {
                    e = e1;
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    Result result = get();
                    resultFunAfterExecute.afterExecute(result, e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute();
    }

}
