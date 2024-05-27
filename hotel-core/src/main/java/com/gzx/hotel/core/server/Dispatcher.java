package com.gzx.hotel.core.server;

import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.dto.ACStatus;
import com.gzx.hotel.core.po.Request;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 调度器
 */
@Component
public class Dispatcher {

    @Resource
    private ServerQueue serverQueue;

    @Resource
    private WaitQueue waitQueue;

    public void dispatch(Request request) {
        // 判断服务队列中的任务是否超出了服务的最大数，如果不是则加入服务，否则加入等待队列
        if (serverQueue.getSize() < SystemSettings.getServerAbility()) {
            serverQueue.put(request);
            return;
        }
        // 否则创建线程机试任务进行等待后调度
        waitQueue.put(request, () -> {
            try {
                // 休眠一段时间
                Thread.sleep(SystemSettings.getWaitingTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 然后移除服务队列中的一个服务，放入这个服务
            Request removed = serverQueue.pop();
            waitQueue.remove(request);
            serverQueue.put(request);

            // 将移除的服务重新放回等待队列
            dispatch(removed);
        });
        // 开始执行等待任务
//        ((Thread) waitQueue.get(request)).start();
    }

    public Request finishRequest(Long requestId) {
        return serverQueue.remove(requestId);
    }

    public Request getRequest(Long requestId) {
        Request request = serverQueue.getRequest(requestId);
        if (request != null) {
            return request;
        }
        return waitQueue.getRequest(requestId);
    }

    public ACStatus getACStatus() {
        return new ACStatus(serverQueue.getServicingRequests(), waitQueue.getWaitingRequests());
    }
}
