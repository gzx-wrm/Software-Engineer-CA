package com.gzx.hotel.core.service.impl;

import com.gzx.hotel.base.service.impl.ICrudServiceImpl;
import com.gzx.hotel.core.dao.RecordDao;
import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.service.RecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl extends ICrudServiceImpl<Record> implements RecordService {

    @Override
    public Record getRecordsByUsername(String username, int complete) {
        return ((RecordDao) baseMapper).selectByUsername(username, complete);
    }
}
