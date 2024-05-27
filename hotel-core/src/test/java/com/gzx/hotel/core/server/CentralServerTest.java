package com.gzx.hotel.core.server;

import com.gzx.hotel.core.Application;
import com.gzx.hotel.core.po.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class CentralServerTest {

    @Resource
    CentralServer centralServer;

    @Test
    public void dispatch() throws InterruptedException {
        Request request1 = new Request();
        request1.setId(1L);
        request1.setWindSpeed(2);
        Request request2 = new Request();
        request2.setId(2L);
        request2.setWindSpeed(2);
        Request request3 = new Request();
        request3.setId(3L);
        request3.setWindSpeed(3);
        Request request4 = new Request();
        request4.setId(4L);
        request4.setWindSpeed(3);

        centralServer.dispatch(request1);
        centralServer.dispatch(request2);
        centralServer.dispatch(request3);
        System.out.println(request1.getStatus());
        System.out.println(request2.getStatus());
        System.out.println(request3.getStatus());
        centralServer.dispatch(request4);
        System.out.println(request4.getStatus());
        Thread.sleep(6000);

        System.out.println(request1.getStatus());
        System.out.println(request2.getStatus());
        System.out.println(request3.getStatus());
        System.out.println(request4.getStatus());
        System.out.println(request1.getFee());
    }

    @Test
    public void finishRequest() {
    }
}