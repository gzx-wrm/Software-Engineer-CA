package com.gzx.hotel.core.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gzx.hotel.base.po.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {

    @JsonIgnore
    private Long recordId;

    private String username;

    private String password;

    private String name;

    @TableField(exist = false)
    private String roleName;

    private Integer locked;

    private Date createTime;

    public User(Long recordId, String username, String password, String name, String roleName, Integer locked, Date createTime) {
        this.recordId = recordId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.roleName = roleName;
        this.locked = locked;
        this.createTime = createTime;
    }

    public User() {
    }
}
