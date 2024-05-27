package com.gzx.hotel.core.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SystemSettingsTest {

    @Test
    public void getFeeRate() {
        System.out.println(SystemSettings.getFeeRate());
        System.out.println(SystemSettings.getPowerConsumptionRate(1));
    }
}