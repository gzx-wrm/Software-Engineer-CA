package com.gzx.hotel.core.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("room")
public class Room extends BaseEntity {

    /**
     * 房间名
     */
    @TableField("`name`")
    private String name;

    /**
     * 是否正在被使用
     */
    @TableField("`use`")
    private Integer use;
}
