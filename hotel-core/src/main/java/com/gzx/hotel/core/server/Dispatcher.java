package com.gzx.hotel.core.server;

import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.dto.ACStatus;
import com.gzx.hotel.core.po.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 调度器
 */
@Component
@Slf4j
public class Dispatcher {

    @Resource
    private ServerQueue serverQueue;

    @Resource
    private WaitQueue waitQueue;

    private static final ReentrantLock lock = new ReentrantLock();

    public synchronized void dispatch(Request request) {
        synchronized (lock) {
            log.info("request: {}进入调度", request.getId());
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
            } else {
//                waitQueue.put(request, () -> {
//                    while (!Thread.currentThread().isInterrupted() && serverQueue.getSize() >= SystemSettings.getServerAbility() && request.getWindSpeed() < serverQueue.peek().getWindSpeed());
//
//                    boolean flag = true;
//                    while (!Thread.currentThread().isInterrupted() && serverQueue.getSize() >= SystemSettings.getServerAbility() && Objects.equals(request.getWindSpeed(), serverQueue.peek().getWindSpeed()) && flag) {
//                        try {
//                            // 休眠一段时间
//                            Thread.sleep(SystemSettings.getWaitingTime());
//                            flag = false;
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (!Thread.currentThread().isInterrupted()) {
////                        Request removed;
//////                        synchronized (lock) {
////                            // 然后移除服务队列中的一个服务，放入这个服务
////                            removed = serverQueue.pop();
////                            log.info(":request: {}被暂时移出", request.getId());
////                            if (removed == null) {
////                                return;
////                            }
////                            Request removed1 = waitQueue.remove(request);
////                            if (removed1 == null) {
////                                return;
////                            }
////                            serverQueue.put(request);
//////                        }
////                        // 将移除的服务重新放回等待队列
////                        dispatch(removed);
//
//                        synchronized (lock) {
//                            Request removed = serverQueue.pop();
//                            if (removed != null) {
//                                log.info("request: {}被暂时移出", request.getId());
//                                Request removed1 = waitQueue.remove(request);
//                                serverQueue.put(request);
//                                if (removed1 != null) {
//                                    dispatch(removed);
//                                }
//                            }
//                        }
//                    }
//                });
                waitQueue.put(request, newWaitInstance(request));
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

    private Runnable newWaitInstance(Request request) {
        return () -> {
            while (!Thread.currentThread().isInterrupted() && serverQueue.getSize() >= SystemSettings.getServerAbility() && request.getWindSpeed() < serverQueue.peek().getWindSpeed());

            boolean flag = true;
            while (!Thread.currentThread().isInterrupted() && serverQueue.getSize() >= SystemSettings.getServerAbility() && Objects.equals(request.getWindSpeed(), serverQueue.peek().getWindSpeed()) && flag) {
                try {
                    // 休眠一段时间
                    Thread.sleep(SystemSettings.getWaitingTime());
                    flag = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!Thread.currentThread().isInterrupted()) {
                Request removed;
                synchronized (lock) {
                    // 然后移除服务队列中的一个服务，放入这个服务
                    removed = serverQueue.pop();
                    if (removed == null) {
                        return;
                    }
                    log.info("request: {}被暂时移出", removed.getId());
                    Request removed1 = waitQueue.remove(request);
                    if (removed1 == null) {
                        return;
                    }
                    serverQueue.put(request);
                    log.info("request: {}在request: {}被移除后进入服务", request.getId(), removed.getId());
                    // 将移除的服务重新放回等待队列
                    dispatch(removed);
                }


//                synchronized (lock) {
//                    Request removed1 = waitQueue.remove(request);
//                    if (removed1 == null) {
//                        return;
//                    }
//                    if (serverQueue.getSize() >= SystemSettings.getServerAbility()) {
//                        Request removed = serverQueue.pop();
//                        if (removed != null) {
//                            log.info("request: {}被暂时移出", removed.getId());
//                            dispatch(removed);
//                        }
//                    }
//                    serverQueue.put(request);
//                }
            }
        };

    }
}
