package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.Request;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RequestDao extends ICrudDao<Request> {

    @Select("SELECT request.* FROM request " +
            "LEFT JOIN record ON request.record_id = record.id_ " +
            "WHERE record.room_id = #{roomId} AND request.status = 4")
    List<Request> getByRoomId(Long roomId);
}
