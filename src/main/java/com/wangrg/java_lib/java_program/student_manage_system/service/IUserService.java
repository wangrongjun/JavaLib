package com.wangrg.java_lib.java_program.student_manage_system.service;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrg.java_lib.java_program.student_manage_system.exception.PasswordErrorException;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IUserService {

    Student studentLogin(long studentId, String password) throws
            AccountNotExistsException, PasswordErrorException;

    Manager managerLogin(String managerName, String password) throws
            AccountNotExistsException, PasswordErrorException;

}
