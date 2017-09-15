package com.wangrg.java_program.student_manage_system.service.impl;

import com.wangrg.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.dao.IManagerDao;
import com.wangrg.java_program.student_manage_system.dao.IStudentDao;
import com.wangrg.java_program.student_manage_system.exception.AccountNotExistsException;
import com.wangrg.java_program.student_manage_system.exception.PasswordErrorException;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IUserService;

/**
 * by wangrongjun on 2017/9/11.
 */
public class UserServiceImpl implements IUserService {

    private IStudentDao studentDao = Spring.getBean(IStudentDao.class);
    private IManagerDao managerDao = Spring.getBean(IManagerDao.class);

    private static Student student;
    private static Manager manager;

    @Override
    public Student studentLogin(String studentId, String password) throws AccountNotExistsException, PasswordErrorException {
        try {
            Student student = studentDao.queryById(Long.parseLong(studentId));
            if (student == null) {
                throw new AccountNotExistsException();
            }
            if (password != null && password.equals(student.getPassword())) {
                return student;
            } else {
                throw new PasswordErrorException();
            }
        } catch (NumberFormatException e) {
            throw new AccountNotExistsException();
        }
    }

    @Override
    public Manager managerLogin(String managerId, String password) throws AccountNotExistsException, PasswordErrorException {
        try {
            Manager manager = managerDao.queryById(Long.parseLong(managerId));
            if (manager == null) {
                throw new AccountNotExistsException();
            }
            if (password != null && password.equals(manager.getPassword())) {
                return manager;
            } else {
                throw new PasswordErrorException();
            }
        } catch (NumberFormatException e) {
            throw new AccountNotExistsException();
        }
    }

    @Override
    public Student getStudentFromCache() {
        return student;
    }

    @Override
    public void setStudentToCache(Student student) {
        UserServiceImpl.student = student;
    }

    @Override
    public Manager getManagerFromCache() {
        return manager;
    }

    @Override
    public void setManagerToCache(Manager manager) {
        UserServiceImpl.manager = manager;
    }

}
