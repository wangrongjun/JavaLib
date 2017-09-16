package com.wangrg.java_program.student_manage_system.background_executor;

/**
 * by wangrongjun on 2017/9/15.
 */
public abstract class BackgroundExecutor<Result> {

    public interface FunBeforeExecute {
        void beforeExecute();
    }

    public interface FunExecuteInBackground<Result> {
        Result executeInBackground() throws Exception;
    }

    public interface FunAfterExecute<Result> {
        void afterExecute(Result result, Exception e);
    }

    private FunBeforeExecute funBeforeExecute;
    private FunExecuteInBackground<Result> funExecuteInBackground;

    public BackgroundExecutor<Result> before(FunBeforeExecute funBeforeExecute) {
        this.funBeforeExecute = funBeforeExecute;
        return this;
    }

    public BackgroundExecutor<Result> executeInBackground(FunExecuteInBackground<Result> funExecuteInBackground) {
        this.funExecuteInBackground = funExecuteInBackground;
        return this;
    }

    public void after(FunAfterExecute<Result> resultFunAfterExecute) {
        execute(funBeforeExecute, funExecuteInBackground, resultFunAfterExecute);
    }

    abstract void execute(FunBeforeExecute funBeforeExecute,
                          FunExecuteInBackground<Result> funExecuteInBackground,
                          FunAfterExecute<Result> resultFunAfterExecute);

}
