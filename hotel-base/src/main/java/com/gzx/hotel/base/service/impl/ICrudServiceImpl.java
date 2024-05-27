package com.gzx.hotel.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gzx.hotel.base.dao.ICrudDao;
import com.gzx.hotel.base.po.BaseEntity;
import com.gzx.hotel.base.service.ICrudService;

import java.util.List;

public class ICrudServiceImpl<T extends BaseEntity> extends ServiceImpl<ICrudDao<T>, T> implements ICrudService<T> {

    @Override
    public PageInfo<T> listPage(T entity, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
//        List<T> res = baseMapper.selectByPage(entity);
        List<T> res = baseMapper.selectList(Wrappers.query(entity));
        return new PageInfo<T>(res);
    }

    @Override
    public List<T> list(T entity) {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }
}
