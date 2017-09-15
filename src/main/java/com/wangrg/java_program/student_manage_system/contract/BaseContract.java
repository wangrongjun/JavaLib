package com.wangrg.java_program.student_manage_system.contract;

import javax.swing.*;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface BaseContract {

    interface IBaseView {
        default void showMsg(String msg) {
            JOptionPane.showMessageDialog(null, msg);
        }

        default void loading(String hint) {
        }

        default void loadFinish(String hint) {
        }
    }

    interface IBaseController {

    }

}
