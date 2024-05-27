package com.gzx.hotel.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.gzx.hotel.base.po.BaseEntity;

import java.util.List;

public interface ICrudService<T extends BaseEntity> extends IService<T> {
    PageInfo<T> listPage(T entity, int pageNum, int pageSize);

    List<T> list(T entity);
}
