package com.gzx.hotel.core.server;

import com.gzx.hotel.core.po.AirConditioner;
import com.gzx.hotel.core.po.Request;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务队列
 */
@Data
@Component
public class ServerQueue {

    private HashMap<Long, ServerObject> map;

    private PriorityQueue<ServerObject> serverQueue;

    private AirConditioner airConditioner;

    @Autowired
    public ServerQueue() {
        map = new HashMap<>();
        serverQueue = new PriorityQueue<>();
    }

    public synchronized int getSize() {
        return serverQueue.size();
    }

    public synchronized void put(Request request) {
        ServerObject serverObject = new ServerObject(request);
        serverObject.start();
        map.put(request.getId(), serverObject);
        serverQueue.add(serverObject);
    }

    /**
     * 弹出优先级最低的一个请求
     */
    public synchronized Request pop() {
        if (serverQueue.size() == 0) {
            return null;
        }
        ServerObject serverObject = serverQueue.remove();
        serverObject.finish();
        map.remove(serverObject.getRequestId());
        return serverObject.getRequest();
    }

    public synchronized Request peek() {
        if (serverQueue.size() == 0) {
            return null;
        }
        return serverQueue.peek().getRequest();
    }

    public synchronized Request remove(Request request) {
        return remove(request.getId());
    }

    public synchronized Request remove(Long requestId) {
        if (!map.containsKey(requestId)) {
            return null;
        }
        ServerObject removed = map.remove(requestId);
        removed.finish();
        serverQueue.remove(removed);
        return removed.getRequest();
    }

    public synchronized Request getRequest(Long requestId) {
        ServerObject serverObject = map.get(requestId);
        if (serverObject != null) {
            return serverObject.getRequest();
        }
        return null;
    }

    public synchronized List<Request> getServicingRequests() {
        return map.values().stream().map(ServerObject::getRequest).collect(Collectors.toList());
    }

}
