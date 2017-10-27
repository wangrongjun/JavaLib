package com.wangrj.java_lib.java_program.shopping_system.servlet;

import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.web.CustomHttpServlet;
import com.wangrj.java_lib.java_program.shopping_system.Response;
import com.wangrj.java_lib.java_program.shopping_system.StateCode;
import com.wangrj.java_lib.java_program.shopping_system.bean.User;
import com.wangrj.java_lib.java_program.shopping_system.dao.UserDao;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/register?phone=15521302230&password=123
 */
public class RegisterServlet extends CustomHttpServlet {

    private Response<String> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"phone", "password"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {
        String phone = (String) parameterMap.get("phone");
        String password = (String) parameterMap.get("password");

        if (TextUtil.isEmpty(phone, password)) {
            response = new Response<>(StateCode.PARAM_ERROR, "�ֻ��Ż�����Ϊ��");
            return;
        }

        try {
            UserDao userDao = new UserDao();
            if (userDao.query(phone) != null) {
                response = new Response<>(StateCode.ERROR_NORMAL, "���ֻ�����ע��");
            } else {
                User user = new User(phone, password, null, null, 0);
                userDao.insert(user);
                response = new Response<>(StateCode.OK, "ע��ɹ�");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
