package com.gzx.hotel.core.dao;

import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.core.dto.BillDTO;
import com.gzx.hotel.core.po.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BillDao extends ICrudDao<Bill> {

    @Select("SELECT b.fee b_fee, re.id_ re_id, re.start_time checkin_time, re.end_time checkout_time, r.`name` room_name " +
            "FROM bill b LEFT JOIN record re ON b.record_id = re.id_ " +
            "LEFT JOIN room r ON re.room_id = r.id_ WHERE b.id_ = #{id}")
    @Results({
            @Result(column = "room_name", property = "roomName"),
            @Result(column = "b_fee", property = "totalFee"),
            @Result(column = "re_id", property = "record.id"),
            @Result(column = "checkin_time", property = "record.startTime"),
            @Result(column = "checkout_time", property = "record.endTime"),
    })
    BillDTO getBillById(Long id);
}
