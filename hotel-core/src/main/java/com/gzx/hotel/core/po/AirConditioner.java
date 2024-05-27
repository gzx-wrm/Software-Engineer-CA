package com.gzx.hotel.core.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("airconditioner")
public class AirConditioner extends BaseEntity {
    private Long roomId;

    private Double temperature;

    @TableField("windspeed")
    private Integer windSpeed;

    private Double targetTemperature;

    @TableField("target_windspeed")
    private Integer targetWindSpeed;
}
