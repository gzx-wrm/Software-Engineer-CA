package com.gzx.hotel.core;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan("com.gzx.hotel.core.dao")
@EnableAspectJAutoProxy
//@EnableWebSocket
public class Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
