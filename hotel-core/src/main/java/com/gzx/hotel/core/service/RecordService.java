package com.gzx.hotel.core.service;

import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.core.po.Record;

import java.util.List;

public interface RecordService extends ICrudService<Record> {

    Record getRecordsByUsername(String username, int complete);
}
