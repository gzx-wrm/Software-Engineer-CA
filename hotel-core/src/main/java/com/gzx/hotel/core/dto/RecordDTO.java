package com.gzx.hotel.core.dto;

import com.gzx.hotel.core.po.Record;

/**
 * 返回给住客的记录，就是把记录和room联系起来了
 */
public class RecordDTO {
    private Record record;

    private String roomName;
}
