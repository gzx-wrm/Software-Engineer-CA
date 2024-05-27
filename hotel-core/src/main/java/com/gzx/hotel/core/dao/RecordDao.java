package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.po.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecordDao extends ICrudDao<Record> {

    @Select("SELECT r.* " +
            "FROM record r " +
            "LEFT JOIN user u ON r.id_=u.record_id " +
            "WHERE u.username = #{username} AND " +
            "r.complete = #{complete}")
    Record selectByUsername(String username, int complete);
}
