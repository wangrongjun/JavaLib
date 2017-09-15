package com.wangrg.java_program.shopping_system.servlet;

import com.wangrg.java_program.shopping_system.dao.OrdersDao;
import com.wangrg.java_util.DateUtil;
import com.wangrg.java_util.GsonUtil;
import com.wangrg.java_program.shopping_system.bean.Orders;
import com.wangrg.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/addOrder?userId=1&goodId=1&count=1
 */
public class AddOrderServlet extends CustomHttpServlet {

    private com.wangrg.java_program.shopping_system.Response<Orders> response;

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
            response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.PARAM_ERROR, "����������");
            return;
        }

        try {
            Orders order = new Orders(userId, goodId, count, DateUtil.getCurrentDate());
            int orderId = new OrdersDao().insert(order);
            order.setOrderId(orderId);
            response = new com.wangrg.java_program.shopping_system.Response<>(com.wangrg.java_program.shopping_system.StateCode.OK, order, null);
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
