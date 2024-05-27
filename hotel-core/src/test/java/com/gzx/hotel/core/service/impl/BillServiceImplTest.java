package com.gzx.hotel.core.service.impl;

import com.gzx.hotel.core.service.BillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BillServiceImplTest {

    @Resource
    BillService billService;

    @Test
    public void getBillById() {
        System.out.println(billService.getBillById(1L));
    }
}