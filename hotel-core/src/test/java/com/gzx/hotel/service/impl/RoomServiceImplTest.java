package com.gzx.hotel.service.impl;

import com.gzx.hotel.core.Application;
import com.gzx.hotel.core.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class RoomServiceImplTest {

    @Resource
    RoomService roomService;

    @Test
    public void test() {
        System.out.println(roomService.list());
    }

}