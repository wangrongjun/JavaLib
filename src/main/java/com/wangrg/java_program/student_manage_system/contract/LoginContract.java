package com.wangrg.java_program.student_manage_system.contract;

/**
 * by wangrongjun on 2017/9/12.
 * <p>
 * 1.点击登录按钮：controller.clickLogin
 * 2.关闭登录窗口（跳到主页面）：view.closeAndOpenMainView
 */
public interface LoginContract {

    interface ILoginView extends BaseContract.IBaseView {
        /**
         * 一开始是void close()，controller调用view.close后创建新窗口。但后来发现无法兼容某些view。
         * 例如Android的Activity，controller无权自行创建，必须由view启动。所以改为如下：
         */
        void closeAndOpenMainView(Class viewClass);
    }

    interface ILoginController extends BaseContract.IBaseController {
        /**
         * @param identity 0代表学生，1代表管理员
         */
        void clickLogin(String account, String password, int identity);
    }

}
