package com.gzx.hotel.base.controller;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.gzx.hotel.base.po.BaseEntity;
import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.base.pojo.ResponseCode;
import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.base.utils.GenericUtil;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 基类控制器，一些增删改查业务的核心代码可以重用
 * @param <S> 实体类型对应的服务
 * @param <T> 实体类型
 */
public abstract class BaseController<S extends ICrudService<T>, T extends BaseEntity> {

    @Autowired
    protected S service;

    protected Logger LOG = LoggerFactory.getLogger(this.getClass());


    /**
     * 域对象类型
     */
    protected Class<T> entityClass;

    public BaseController() {
        this.entityClass = GenericUtil.getSuperGenericClass2(this.getClass());
    }


    /**
     * 加载
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public ResponseBean query(@PathVariable Long id) throws Exception {
        T entity = service.getById(id);
        afterEdit(entity);
        return ResponseBean.ok().data(entity);
    }

    /**
     * 分页查询
     * @param entity
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/list/page")
    public ResponseBean listPage(T entity,
                                @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                @RequestParam(name = "rows", defaultValue = "10", required = false) int rows) {
        PageInfo<T> result = service.listPage(entity, page, rows);
        return ResponseBean.ok().data(result);
    }

    /**
     * 根据实体条件查询
     * @return
     */
    @GetMapping("/list")
    public ResponseBean list(T entity) {
        List<T> list = service.list(entity);

        return ResponseBean.ok().data(list);
    }

    /**
     * 增加，修改
     */
    @PostMapping("/")
    public ResponseBean save(T entity) throws Exception {
        ResponseBean rm = new ResponseBean();
        try {
            beforeSave(entity); //保存前处理实体类
            T entity1 = service.getById(entity.getId());
            if (entity1 != null) {
                service.update(entity, Wrappers.<T>update().eq("id_", entity.getId()));
            }
            else {
                service.save(entity);
            }
//            service.saveOrUpdate(entity);
            rm.setData(entity);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setCode(ResponseCode.FAIL);
            rm.setMsg("操作失败");
        }
        return rm;
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseBean delete(@PathVariable Long id) throws Exception {
        ResponseBean rm = new ResponseBean();
        try {
            service.removeById(id);
            rm.setData(null);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setCode(ResponseCode.FAIL);
            rm.setMsg("操作失败");
        }
        return rm;
    }

    /**
     * 批量删除
     */
    @DeleteMapping(value = "/batch")
    public ResponseBean delete(@RequestParam List<Long> ids) {
        ResponseBean rm = new ResponseBean();
        try {
            service.removeByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setMsg("删除失败");
            rm.setCode(ResponseCode.FAIL);
        }
        return rm;
    }

    /**
     * 保存前执行
     * @param entity
     * @throws Exception
     */
    public void beforeSave(T entity) throws Exception {
    }

    /**
     * 模板方法：在加载后执行
     * @param entity
     */
    public void afterEdit(T entity) {

    }

}
