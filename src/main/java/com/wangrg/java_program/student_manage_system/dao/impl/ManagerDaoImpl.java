package com.wangrg.java_program.student_manage_system.dao.impl;

import com.wangrg.db2.Where;
import com.wangrg.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_program.student_manage_system.dao.IManagerDao;
import com.wangrg.java_program.student_manage_system.dao.base.StudentManageSystemDao;

import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class ManagerDaoImpl extends StudentManageSystemDao<Manager> implements IManagerDao {

    public Manager query(String managerId, String password) {
        List<Manager> managerList = query(Where.build("managerId", managerId));
        if (managerList.size() > 0) {
            Manager manager = managerList.get(0);
            if (password != null && password.equals(manager.getPassword())) {
                return manager;
            }
        }
        return null;
    }

}
