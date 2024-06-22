package com.gzx.hotel.core.server;

import com.gzx.hotel.core.constant.ServerStatus;
import com.gzx.hotel.core.po.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 等待队列
 */
@Component
public class WaitQueue {

    private HashMap<Long, Request> map;

    private HashMap<Request, Thread> waitingQueue;

    private static final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public WaitQueue() {
        this.map = new HashMap<>();
        this.waitingQueue = new HashMap<>();
    }

    public synchronized void put(Request request, Runnable runnable) {
        synchronized (lock) {
            Thread t = new Thread(runnable);
            waitingQueue.put(request, t);
            map.put(request.getId(), request);
            request.setStatus(ServerStatus.WAITING);
            t.start();
        }
    }

    public synchronized Request remove(Request request) {
        synchronized (lock) {
            return remove(request.getId());
        }
    }

    public synchronized Request remove(Long requestId) {
        synchronized (lock) {
            Request removed = map.remove(requestId);
            if (removed == null) {
                return null;
            }
            Thread t = waitingQueue.remove(removed);
            t.interrupt();
            return removed;
        }
    }

    public synchronized Request getRequest(Long requestId) {
        synchronized (lock) {
            return map.get(requestId);
        }
    }

    public synchronized List<Request> getWaitingRequests() {
        synchronized (lock) {
            return new ArrayList<>(map.values());
        }
    }
}
