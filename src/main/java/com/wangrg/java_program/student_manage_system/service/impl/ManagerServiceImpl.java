package com.wangrg.java_program.student_manage_system.service.impl;

import com.wangrg.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_program.student_manage_system.service.IManagerService;

/**
 * by wangrongjun on 2017/9/14.
 */
public class ManagerServiceImpl implements IManagerService {

    private static Manager manager;

    @Override
    public Manager getManagerFromLocal() {
        return manager;
    }

    @Override
    public void setManagerToLocal(Manager manager) {
        ManagerServiceImpl.manager = manager;
    }
}
