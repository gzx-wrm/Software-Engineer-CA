package com.gzx.hotel.core.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bill extends BaseEntity {

    private Long recordId;

    private Double fee;

    @TableField(exist = false)
    private List<Request> detail;

    public Bill(Long recordId, Double fee, List<Request> detail) {
        this.recordId = recordId;
        this.fee = fee;
        this.detail = detail;
    }

    public Bill() {
    }
}
