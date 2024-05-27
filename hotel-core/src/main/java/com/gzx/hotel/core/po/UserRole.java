package com.gzx.hotel.core.po;

import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRole extends BaseEntity {

    private Long userId;

    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole() {
    }
}
