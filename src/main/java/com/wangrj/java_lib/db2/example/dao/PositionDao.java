package com.wangrj.java_lib.db2.example.dao;

import com.wangrj.java_lib.db2.Dao;
import com.wangrj.java_lib.db2.example.bean.Position;
import com.wangrj.java_lib.db2.Dao;
import com.wangrj.java_lib.db2.example.bean.Position;

import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public interface PositionDao extends Dao<Position> {

    List<Position> queryByDepartmentId(int departmentId);

}
