package com.wangrj.java_lib.java_program.student_manage_system.service;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrj.java_lib.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrj.java_lib.java_program.student_manage_system.exception.PasswordErrorException;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IUserService {

    Student studentLogin(long studentId, String password) throws
            AccountNotExistsException, PasswordErrorException;

    Manager managerLogin(String managerName, String password) throws
            AccountNotExistsException, PasswordErrorException;

}
