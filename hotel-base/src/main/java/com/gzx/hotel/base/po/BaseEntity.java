package com.gzx.hotel.base.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    @TableId(value = "id_", type = IdType.AUTO)
    private Long id;
}
