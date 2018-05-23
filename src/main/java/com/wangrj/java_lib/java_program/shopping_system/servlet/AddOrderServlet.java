package com.wangrj.java_lib.java_program.shopping_system.servlet;

import com.wangrj.java_lib.java_program.shopping_system.StateCode;
import com.wangrj.java_lib.java_program.shopping_system.dao.OrdersDao;
import com.wangrj.java_lib.java_util.DateUtil;
import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_program.shopping_system.bean.Orders;
import com.wangrj.java_lib.web.CustomHttpServlet;
import com.wangrj.java_lib.java_program.shopping_system.Response;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/addOrder?userId=1&goodId=1&count=1
 */
public class AddOrderServlet extends CustomHttpServlet {

    private Response<Orders> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"userId", "goodId", "count"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        int userId;
        int goodId;
        int count;

        try {
            userId = Integer.parseInt((String) parameterMap.get("userId"));
            goodId = Integer.parseInt((String) parameterMap.get("goodId"));
            count = Integer.parseInt((String) parameterMap.get("count"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.PARAM_ERROR, "����������");
            return;
        }

        try {
            Orders order = new Orders(userId, goodId, count, DateUtil.getCurrentDate());
            int orderId = new OrdersDao().insert(order);
            order.setOrderId(orderId);
            response = new Response<>(StateCode.OK, order, null);
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
