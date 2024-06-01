package com.gzx.hotel.core.service.impl;

import com.gzx.hotel.base.service.impl.ICrudServiceImpl;
import com.gzx.hotel.core.dao.RequestDao;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.service.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl extends ICrudServiceImpl<Request> implements RequestService {
    @Override
    public List<Request> getByRoomId(Long roomId) {
        return ((RequestDao) baseMapper).getByRoomId(roomId);
    }
}
