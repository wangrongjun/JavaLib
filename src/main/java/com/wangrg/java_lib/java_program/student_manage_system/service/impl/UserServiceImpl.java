package com.wangrg.java_lib.java_program.student_manage_system.service.impl;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.dao.IManagerDao;
import com.wangrg.java_lib.java_program.student_manage_system.dao.IStudentDao;
import com.wangrg.java_lib.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrg.java_lib.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrg.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_lib.java_program.student_manage_system.service.IUserService;

/**
 * by wangrongjun on 2017/9/11.
 */
public class UserServiceImpl implements IUserService {

    private IStudentDao studentDao = Spring.getBean(IStudentDao.class);
    private IManagerDao managerDao = Spring.getBean(IManagerDao.class);

    @Override
    public Student studentLogin(long studentId, String password) throws AccountNotExistsException, PasswordErrorException {
            Student student = studentDao.queryById(studentId);
            if (student == null) {
                throw new AccountNotExistsException();
            }
            if (password != null && password.equals(student.getPassword())) {
                return student;
            } else {
                throw new PasswordErrorException();
            }
    }

    @Override
    public Manager managerLogin(String managerName, String password) throws AccountNotExistsException, PasswordErrorException {
        Manager manager = managerDao.queryByName(managerName);
        if (manager == null) {
            throw new AccountNotExistsException();
        }
        if (password != null && password.equals(manager.getPassword())) {
            return manager;
        } else {
            throw new PasswordErrorException();
        }
    }

}
