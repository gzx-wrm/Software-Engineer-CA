package com.gzx.hotel.core.po;

import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Record extends BaseEntity {

    private Long roomId;

    private Date startTime;

    private Date endTime;

    private Integer complete;

    public Record(Long roomId, Date startTime, Date endTime, Integer complete) {
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
    }

    public Record() {
    }
}
