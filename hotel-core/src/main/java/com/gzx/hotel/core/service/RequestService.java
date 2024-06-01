package com.gzx.hotel.core.service;

import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.core.po.Request;

import java.util.List;

public interface RequestService extends ICrudService<Request> {

    List<Request> getByRoomId(Long roomId);
}
