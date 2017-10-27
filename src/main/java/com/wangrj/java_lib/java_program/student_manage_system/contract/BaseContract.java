package com.wangrj.java_lib.java_program.student_manage_system.contract;

import com.wangrj.java_lib.swing.JOptionPaneUtil;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface BaseContract {

    interface IBaseView {
        default void showMsg(String msg) {
            JOptionPaneUtil.showInfo(msg);
        }

        default void showError(String error) {
            JOptionPaneUtil.showError(error);
        }

        default void loading(String hint) {
        }

        default void loadFinish() {
        }
    }

    interface IBaseController {

    }

}
