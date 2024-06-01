package com.gzx.hotel.core.dto;

import com.gzx.hotel.core.po.Request;
import lombok.Data;

import java.util.List;

/**
 * 统计信息
 */
@Data
public class StatisticInfoDTO {

    private Long roomId;

    private String roomName;

    private List<Request> requestList;

    /**
     * 空调使用次数
     */
    private Integer useCount;

    /**
     * 最常设定的目标温度
     */
    private Double mostCommonTemperature;

    /**
     * 最常设置的风速
     */
    private Integer mostCommonWindSpeed;

}
