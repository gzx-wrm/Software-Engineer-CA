package com.gzx.hotel.core.controller;

import com.gzx.hotel.base.controller.BaseController;
import com.gzx.hotel.core.po.Room;
import com.gzx.hotel.core.service.RoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/room")
public class RoomController extends BaseController<RoomService, Room> {


}
