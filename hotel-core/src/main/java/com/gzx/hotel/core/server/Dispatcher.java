package com.gzx.hotel.core.server;

import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.dto.ACStatus;
import com.gzx.hotel.core.po.Request;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 调度器
 */
@Component
public class Dispatcher {

    @Resource
    private ServerQueue serverQueue;

    @Resource
    private WaitQueue waitQueue;

    private static final ReentrantLock lock = new ReentrantLock();

    public synchronized void dispatch(Request request) {
        synchronized (lock) {
            // 判断服务队列中的任务是否超出了服务的最大数，如果不是则加入服务，否则加入等待队列
            if (serverQueue.getSize() < SystemSettings.getServerAbility()) {
                serverQueue.put(request);
                return;
            }

            // 否则使用算法放入等待队列或服务队列
            Request peek = serverQueue.peek();
            if (request.getWindSpeed() > peek.getWindSpeed()) {
                // 移除服务队列中的一个服务，放入这个服务
                Request removed = serverQueue.pop();
                serverQueue.put(request);
                // 将移除的服务重新放回等待队列
                dispatch(removed);
            }
            else {
                waitQueue.put(request, () -> {
                    if (request.getWindSpeed() < peek.getWindSpeed()) {
                        while (!Thread.currentThread().isInterrupted() && serverQueue.getSize() >= SystemSettings.getServerAbility());
                    }
                    else if (Objects.equals(request.getWindSpeed(), peek.getWindSpeed())) {
                        try {
                            // 休眠一段时间
                            Thread.sleep(SystemSettings.getWaitingTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!Thread.currentThread().isInterrupted()) {
                        synchronized (lock) {
                            // 然后移除服务队列中的一个服务，放入这个服务
                            Request removed = serverQueue.pop();
                            waitQueue.remove(request);
                            serverQueue.put(request);

                            // 将移除的服务重新放回等待队列
                            dispatch(removed);
                        }
                    }
                });
            }
        }
    }

    public synchronized Request finishRequest(Long requestId) {
        synchronized (lock) {
            Request removed = waitQueue.remove(requestId);
            if (removed != null) {
                return removed;
            }
            return serverQueue.remove(requestId);
        }
    }

    public synchronized Request getRequest(Long requestId) {
        synchronized (lock) {
            Request request = serverQueue.getRequest(requestId);
            if (request != null) {
                return request;
            }
            return waitQueue.getRequest(requestId);
        }
    }

    public synchronized ACStatus getACStatus() {
        synchronized (lock) {
            return new ACStatus(serverQueue.getServicingRequests(), waitQueue.getWaitingRequests());
        }
    }
}
