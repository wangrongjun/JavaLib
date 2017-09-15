package com.wangrg.java_program.shopping_system.servlet;

import com.wangrg.java_util.GsonUtil;
import com.wangrg.java_util.TextUtil;
import com.wangrg.java_program.shopping_system.bean.User;
import com.wangrg.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * 
 */
public class LoginServlet extends CustomHttpServlet {

    private com.wangrg.java_program.shopping_system.Response<User> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"phone", "password"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {
        String phone = (String) parameterMap.get("phone");
        String password = (String) parameterMap.get("password");

        if (TextUtil.isEmpty(phone, password)) {
            response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.PARAM_ERROR, "�ֻ��Ż�����Ϊ��");
            return;
        }

        try {
            com.wangrg.java_program.shopping_system.dao.UserDao userDao = new com.wangrg.java_program.shopping_system.dao.UserDao();
            User user = userDao.queryLogin(phone, password);
            if (user != null) {
                response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.OK, user, null);
            } else {
                response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.ERROR_NORMAL, "�ֻ��Ų����ڻ��������");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
