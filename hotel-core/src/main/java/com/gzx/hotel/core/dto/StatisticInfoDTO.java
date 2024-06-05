package com.gzx.hotel.core.dto;

import com.gzx.hotel.core.po.Request;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 统计信息
 */
@Data
public class StatisticInfoDTO {

    private Long roomId;

    private String roomName;

    private Date startRequestTime;

    private Date endRequestTime;

    private List<Request> requestList;

    /**
     * 空调使用次数
     */
    private Integer useCount;

    /**
     * 空调使用费用
     */
    private Double totalFee;

    /**
     * 最常设定的目标温度
     */
    private Double mostCommonTemperature;

    /**
     * 最常设置的风速
     */
    private Integer mostCommonWindSpeed;

}
