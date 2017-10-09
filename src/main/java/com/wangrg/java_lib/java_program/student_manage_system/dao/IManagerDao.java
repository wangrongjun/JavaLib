package com.wangrg.java_lib.java_program.student_manage_system.dao;

import com.wangrg.java_lib.db3.Dao;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Manager;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IManagerDao extends Dao<Manager> {

    Manager queryByName(String managerName);

}
