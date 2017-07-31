/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:21
 */
package net.lizhaoweb.spring.mvc.uas.service.impl;

import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.dao.read.IRoleAuthRelationReadMapper;
import net.lizhaoweb.spring.mvc.uas.dao.write.IRoleAuthRelationWriteMapper;
import net.lizhaoweb.spring.mvc.uas.model.RoleAuthRelation;
import net.lizhaoweb.spring.mvc.uas.search.UASAbstractSearch;
import net.lizhaoweb.spring.mvc.uas.service.IRoleAuthRelationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <h1>业务 [实现] - 角色权限关系</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class RoleAuthRelationService extends UASAbstractService implements IRoleAuthRelationService {

    @Autowired
    private IRoleAuthRelationWriteMapper writeMapper;

    @Autowired
    private IRoleAuthRelationReadMapper readMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createTable() {
        writeMapper.createTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAndCreateTable() {
        if (readMapper.existTable() < 1) {
            writeMapper.createTable();
        }
    }

    @Override
    public DataDeliveryWrapper<Object> save(RoleAuthRelation bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Save bean is null");
        }
        long id = writeMapper.insert(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "保存角色权限关系成功", id > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> update(RoleAuthRelation bean) {
        return null;
    }

    @Override
    public DataDeliveryWrapper<Object> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Delete id is null");
        }
        int count = writeMapper.delete(id);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "删除角色权限关系成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> remove(String ids) {
        if (StringUtil.isBlank(ids)) {
            throw new IllegalArgumentException("Remove ids is null");
        }
        int count = writeMapper.remove(ids);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "批量删除角色权限关系成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<PageResult<List<RoleAuthRelation>>> pageList(UASAbstractSearch<Long> search) {
        return null;
    }

    @Override
    public RoleAuthRelation get(Long id) {
        return null;
    }
}
