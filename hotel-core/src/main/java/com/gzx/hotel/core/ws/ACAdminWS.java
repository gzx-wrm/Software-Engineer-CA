package com.gzx.hotel.core.ws;

import com.gzx.hotel.core.server.CentralServer;
import com.gzx.hotel.core.service.RequestService;
import com.gzx.hotel.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ServerEndpoint("/monitor")
public class ACAdminWS {

    private static CentralServer centralServer;

    private static JsonUtil jsonUtil;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public void setSocketMessageHandler(CentralServer centralServer, JsonUtil jsonUtil) {
        ACAdminWS.centralServer = centralServer;
        ACAdminWS.jsonUtil = jsonUtil;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("空调管理员上线，username: {}", session.getUserPrincipal().getName());
        try {
            session.getBasicRemote().sendText("success");
            // 每隔1秒发送一条消息给客户端
            executorService.scheduleAtFixedRate(() -> {
                try {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(jsonUtil.toJson(centralServer.getACStatus()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 1, TimeUnit.SECONDS);
        } catch (IOException e) {
            log.error("与服务器建立连接失败，{}", e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("监控断开，username: {}", session.getUserPrincipal().getName());
        executorService.shutdown();
    }

}
