package com.gzx.hotel.core.server;

import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.constant.Wind;
import com.gzx.hotel.core.dto.ACStatus;
import com.gzx.hotel.core.po.AirConditioner;
import com.gzx.hotel.core.po.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CentralServer {

    /**
     * 调度器
     */
    private Dispatcher dispatcher;


    @Autowired
    public CentralServer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 调度任务
     * @param request
     * @return 调度后的服务请求，包含调度的状态
     */
    public synchronized Request dispatch(Request request) {
        dispatcher.dispatch(request);
        return request;
    }

    public synchronized Request finishRequest(Long requestId) {
        Request request = dispatcher.finishRequest(requestId);
        return request;
    }

    public synchronized Request finishRequest(Request request) {
        dispatcher.finishRequest(request.getRecordId());
        return request;
    }

    public synchronized ACStatus getACStatus() {
        return dispatcher.getACStatus();
    }

    public synchronized Request getRequest(Long requestId) {
        return dispatcher.getRequest(requestId);
    }
}
