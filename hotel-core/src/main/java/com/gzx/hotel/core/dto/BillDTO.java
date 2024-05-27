package com.gzx.hotel.core.dto;

import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.po.Request;
import lombok.Data;

import java.util.List;

@Data
public class BillDTO {
    private String roomName;

    private Record record;

    private Double totalFee;

    private List<Request> requestList;
}
