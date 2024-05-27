package com.gzx.hotel.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzx.hotel.base.po.BaseEntity;

import java.util.List;

public interface ICrudDao<T extends BaseEntity> extends BaseMapper<T> {
    List<T> selectByPage(T entity);
}
