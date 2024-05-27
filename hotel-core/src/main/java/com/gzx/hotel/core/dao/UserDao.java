package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao extends ICrudDao<User> {

    @Select("SELECT u.*, r.`name` AS role_name " +
            "FROM user u " +
            "LEFT JOIN user_role ur ON u.id_ = ur.user_id " +
            "LEFT JOIN role r ON ur.role_id = r.id_ " +
            "WHERE u.username = #{username}")
    User getUserWithRoles(@Param("username") String username);
}
