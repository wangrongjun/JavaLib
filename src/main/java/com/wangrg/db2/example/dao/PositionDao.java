package com.wangrg.db2.example.dao;

import com.wangrg.db2.Dao;
import com.wangrg.db2.example.bean.Position;

import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public interface PositionDao extends Dao<Position> {

    List<Position> queryByDepartmentId(int departmentId);

}
