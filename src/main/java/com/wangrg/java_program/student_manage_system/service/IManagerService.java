package com.wangrg.java_program.student_manage_system.service;

import com.wangrg.java_program.student_manage_system.bean.Manager;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IManagerService {

    Manager getManagerFromLocal();

    void setManagerToLocal(Manager manager);

}
