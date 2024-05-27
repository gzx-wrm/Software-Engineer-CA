package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomDao extends ICrudDao<Room> {

    @Override
    @Select("<script>" +
            "SELECT * FROM room" +
            "<where>" +
            "<if test='name != null and name.trim() != \"\"'> AND `name` = #{name}</if>" +
            "<if test='use != null'> AND `use` = #{use}</if>" +
            "</where>" +
            "</script>")
    List<Room> selectByPage(Room entity);
}
