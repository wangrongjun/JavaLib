package com.wangrj.java_lib.java_program.student_manage_system.dao.impl;

import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.dao.IManagerDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.dao.IManagerDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;

import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class ManagerDaoImpl extends StudentManageSystemDao<Manager> implements IManagerDao {

    @Override
    public Manager queryByName(String managerName) {
        List<Manager> managerList = query(Where.eq("managerName", managerName));
        return managerList.size() > 0 ? managerList.get(0) : null;
    }

}
