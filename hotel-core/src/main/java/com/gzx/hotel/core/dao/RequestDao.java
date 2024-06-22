package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.po.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface RequestDao extends ICrudDao<Request> {

    @Select("<script>"
            + "SELECT request.* "
            + "FROM request "
            + "LEFT JOIN record ON request.record_id = record.id_ "
            + "WHERE record.room_id = #{roomId} AND request.status = 4"
            + "<if test='startRequestTime != null'>"
            + "  AND request.request_time &gt;= #{startRequestTime} "
            + "</if>"
            + "<if test='endRequestTime != null'>"
            + "  AND request.request_time &lt;= #{endRequestTime} "
            + "</if>"
            + "</script>")
    List<Request> getByRoomId(@Param("roomId") Long roomId, @Param("startRequestTime") Date startRequestTime, @Param("endRequestTime") Date endRequestTime);
}
