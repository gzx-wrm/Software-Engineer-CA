package com.gzx.hotel.core.server;

import com.gzx.hotel.core.constant.ServerStatus;
import com.gzx.hotel.core.po.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 等待队列
 */
@Component
public class WaitQueue {

    private HashMap<Long, Request> map;

    private HashMap<Request, Runnable> waitingQueue;

    @Autowired
    public WaitQueue() {
        this.map = new HashMap<>();
        this.waitingQueue = new HashMap<>();
    }

    public synchronized void put(Request request, Runnable runnable) {
        waitingQueue.put(request, runnable);
        map.put(request.getId(), request);
        request.setStatus(ServerStatus.WAITING);
        ((Thread) runnable).start();
    }

    public synchronized Request remove(Request request) {
        waitingQueue.remove(request);
        map.remove(request.getId());
        return request;
    }

    public synchronized Request remove(Long requestId) {
        Request request = map.remove(requestId);
        waitingQueue.remove(request);
        return request;
    }

    public synchronized Request getRequest(Long requestId) {
        return map.get(requestId);
    }

    public synchronized List<Request> getWaitingRequests() {
        return new ArrayList<>(map.values());
    }
}
