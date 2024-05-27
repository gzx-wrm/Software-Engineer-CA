package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.Request;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestDao extends ICrudDao<Request> {
}
