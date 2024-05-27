package com.gzx.hotel.core.ws;

import com.gzx.hotel.core.constant.ServerStatus;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.server.CentralServer;
import com.gzx.hotel.core.service.RequestService;
import com.gzx.hotel.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/sync")
public class ClientWS {

    private static CentralServer centralServer;

    // 存储recordId和session，同一时刻只能有一个用户连接一个session
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // 存储session和request的对应关系
    private static ConcurrentHashMap<String, Request> requestPool = new ConcurrentHashMap<>();

    private static RequestService requestService;

    private static JsonUtil jsonUtil;

    @Autowired
    public void setSocketMessageHandler(CentralServer centralServer, RequestService requestService, JsonUtil jsonUtil) {
        ClientWS.centralServer = centralServer;
        ClientWS.requestService = requestService;
        ClientWS.jsonUtil = jsonUtil;
    }


    @OnOpen
    public void onOpen(Session session) {
        String username = session.getUserPrincipal().getName();
        log.info("空调客户端开机，与服务端建立连接，连接id: {}", session.getId());
        try {
            Session history = sessionPool.get(username);
            if (history != null) {
                log.info("同一用户的多次连接！");
                sessionPool.remove(username);
                closeSession(history, new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "同一用户的多次连接！"));
            }
            sessionPool.put(username, session);
            session.getBasicRemote().sendText("success");
        } catch (IOException e) {
            log.error("与服务器建立连接失败，{}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("请求同步到达：{}", message);
        try {
            // 获取服务请求对象
            Request request = jsonUtil.fromJson(message, Request.class);
            Request request1 = requestPool.get(session.getId());
            // 进行数据库存储
            // 存储到内存
            if (request1 == null) {
                request.setRequestTime(new Date());
                requestService.save(request);
                requestPool.put(session.getId(), request);
                // 进行服务
                centralServer.dispatch(request);
                request1 = request;
            }
            else if (!request.equals(request1)) {
                session.getBasicRemote().sendText("上一个请求还未完成！");
                return;
            }
            // 判断该次消息是否为end信号，如果是的话就要结束服务并保存服务结果
            if (Objects.equals(request.getStatus(), ServerStatus.END)) {
//                closeSession(session, new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "结束服务并退出"));
                // 第二种方法：不关闭连接，而是只结束服务，下次可以就着这次的连接使用
                request1 = finishRequest(request1);
                requestPool.remove(session.getId());
            }
            // 进行返回
            session.getBasicRemote().sendText(jsonUtil.toJson(request1));
        } catch (IOException e) {
            log.error("向客户端同步请求失败, {}", e.getMessage());
            closeSession(session, new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, e.getMessage()));
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("客户端关闭连接: {}", session.getId());
        sessionPool.remove(session.getUserPrincipal().getName());
        finishRequest(requestPool.get(session.getId()));
        requestPool.remove(session.getId());
    }

    private void closeSession(Session session, CloseReason reason) {
        try {
            session.close(reason);
            Request request = requestPool.get(session.getId());
            finishRequest(request);
            requestPool.remove(session.getId());
        } catch (IOException e) {
            log.error("关闭连接失败！sessionId: {}，错误信息: {}", session.getId(), e.getMessage());
        }
    }

    private Request finishRequest(Request request) {
        if (request != null) {
            request = centralServer.finishRequest(request.getId());
            requestService.updateById(request);
        }
        return request;
    }
}
