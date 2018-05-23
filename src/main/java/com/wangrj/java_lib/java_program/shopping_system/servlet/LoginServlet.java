package com.wangrj.java_lib.java_program.shopping_system.servlet;

import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_program.shopping_system.Response;
import com.wangrj.java_lib.java_program.shopping_system.StateCode;
import com.wangrj.java_lib.java_program.shopping_system.bean.User;
import com.wangrj.java_lib.web.CustomHttpServlet;
import com.wangrj.java_lib.java_program.shopping_system.dao.UserDao;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * 
 */
public class LoginServlet extends CustomHttpServlet {

    private Response<User> response;

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
            User user = userDao.queryLogin(phone, password);
            if (user != null) {
                response = new Response<>(StateCode.OK, user, null);
            } else {
                response = new Response<>(StateCode.ERROR_NORMAL, "�ֻ��Ų����ڻ��������");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.toPrettyJson(response);
    }
}
