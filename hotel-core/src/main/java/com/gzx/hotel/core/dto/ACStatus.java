package com.gzx.hotel.core.dto;

import com.gzx.hotel.core.po.Request;
import lombok.Data;

import java.util.List;

/**
 * 封装空调服务队列和调度队列中的内容
 */
@Data
public class ACStatus {

    private List<Request> waiting;

    private List<Request> servicing;

    public ACStatus(List<Request> servicing, List<Request> waiting) {
        this.waiting = waiting;
        this.servicing = servicing;
    }

    public ACStatus() {
    }
}
