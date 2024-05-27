package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDao extends ICrudDao<UserRole> {
}
