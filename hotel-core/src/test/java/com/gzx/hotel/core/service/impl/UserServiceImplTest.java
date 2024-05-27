package com.gzx.hotel.core.service.impl;

import com.gzx.hotel.core.Application;
import com.gzx.hotel.core.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Resource
    UserService userService;

    @Resource
    BCryptPasswordEncoder encoder;

    @Test
    public void test1() {
        System.out.println(encoder.encode("654321"));
    }

    @Test
    public void test() throws Exception {
//        System.out.println(((UserServiceImpl) userService).loadUserByUsername("gzx").getAuthorities());
        System.out.println(userService.login("gzx", "123456"));
    }

}