package com.gzx.hotel.core.server;

import com.gzx.hotel.core.po.AirConditioner;
import com.gzx.hotel.core.po.Request;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
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

    private static final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public ServerQueue() {
        map = new HashMap<>();
        serverQueue = new PriorityQueue<>();
    }

    public synchronized int getSize() {
        return serverQueue.size();
    }

    public synchronized void put(Request request) {
        synchronized (lock) {
            ServerObject serverObject = new ServerObject(request);
            serverObject.start();
            map.put(request.getId(), serverObject);
            serverQueue.add(serverObject);
        }
    }

    /**
     * @Author: gzx
     * @Description: 暂时弹出优先级最低的一个请求，但是不结束
     * @Params:
     * @return:
     */
    public synchronized Request pop() {
        synchronized (lock) {
            if (serverQueue.size() == 0) {
                return null;
            }
            ServerObject serverObject = serverQueue.remove();
            map.remove(serverObject.getRequestId());
            return serverObject.getRequest();
        }
    }

    public synchronized Request peek() {
        synchronized (lock) {
            if (serverQueue.size() == 0) {
                return null;
            }
            return serverQueue.peek().getRequest();
        }
    }

    public synchronized Request remove(Request request) {
        synchronized (lock) {
            return remove(request.getId());
        }
    }

    public synchronized Request remove(Long requestId) {
        synchronized (lock) {
            if (!map.containsKey(requestId)) {
                return null;
            }
            ServerObject removed = map.remove(requestId);
            removed.finish();
            serverQueue.remove(removed);
            return removed.getRequest();
        }
    }

    public synchronized Request getRequest(Long requestId) {
        synchronized (lock) {
            ServerObject serverObject = map.get(requestId);
            if (serverObject != null) {
                return serverObject.getRequest();
            }
            return null;
        }
    }

    public synchronized List<Request> getServicingRequests() {
        synchronized (lock) {
            return map.values().stream().map(ServerObject::getRequest).collect(Collectors.toList());
        }
    }

}
