/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:34
 */
package net.lizhaoweb.spring.mvc.uas.service.impl;

import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.dao.read.IAuthorizationReadMapper;
import net.lizhaoweb.spring.mvc.uas.dao.write.IAuthorizationWriteMapper;
import net.lizhaoweb.spring.mvc.uas.model.Authorization;
import net.lizhaoweb.spring.mvc.uas.search.SearchAuthorization;
import net.lizhaoweb.spring.mvc.uas.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>业务 [实现] - 权限</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class AuthorizationService extends UASAbstractService implements IAuthorizationService {

    @Autowired
    private IAuthorizationWriteMapper writeMapper;

    @Autowired
    private IAuthorizationReadMapper readMapper;

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
    public DataDeliveryWrapper<Object> save(Authorization bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Save bean is null");
        }
        long id = writeMapper.insert(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "保存权限成功", id > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> update(Authorization bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Update bean is null");
        }
        int count = writeMapper.update(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "更新权限成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Delete id is null");
        }
        Authorization bean = readMapper.get(id);
        if (bean == null) {
            return new DataDeliveryWrapper<Object>(StatusCode.PARAMETER_NOT_EXIST, "权限不存在", null);
        }
        if (bean.getRoleList() != null && bean.getRoleList().size() > 0) {
            return new DataDeliveryWrapper<Object>(StatusCode.WRONG_BUSINESS_TYPE, "有角色关联权限", bean.getRoleList());
        }
        int count = writeMapper.delete(bean.getId());
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "删除权限成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> remove(String ids) {
        if (StringUtil.isBlank(ids)) {
            throw new IllegalArgumentException("Remove ids is null");
        }
        List<Authorization> list = readMapper.getAll(ids);
        if (list == null || list.size() < 1) {
            return new DataDeliveryWrapper<Object>(StatusCode.PARAMETER_NOT_EXIST, "权限不存在", null);
        }
        List<Authorization> errorList = new ArrayList<Authorization>();
        List<String> realIdList = new ArrayList<String>();
        for (Authorization bean : list) {
            if (bean == null) {
                continue;
            }
            if (bean.getRoleList() != null && bean.getRoleList().size() > 0) {
                errorList.add(bean);
            } else {
                realIdList.add(bean.getId());
            }
        }
        if (realIdList == null || realIdList.size() < 1) {
            return new DataDeliveryWrapper<Object>(StatusCode.WRONG_BUSINESS_TYPE, "有角色关联权限", errorList);
        }
        String realIds = StringUtil.join(realIdList, ',');
        int count = writeMapper.remove(realIds);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "批量删除权限成功", errorList);
    }

    @Override
    public DataDeliveryWrapper<PageResult<List<Authorization>>> pageList(SearchAuthorization search) {
        if (search == null) {
            throw new IllegalArgumentException("Search model is null");
        }
        if (search.getPage() == null) {
            throw new IllegalArgumentException("Search page is null");
        }
        PageResult<List<Authorization>> pageResult = new PageResult<List<Authorization>>(search.getPage());
        List<Authorization> result = readMapper.findAll(search);
        pageResult.setResult(result);
        return new DataDeliveryWrapper<PageResult<List<Authorization>>>(StatusCode.SUCCESS, "获取权限列表成功", pageResult);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<Authorization> findAll(SearchAuthorization search) {
//        return readMapper.findAll(search);
//    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Authorization find(SearchAuthorization search) {
//        Authorization result = null;
//        List<Authorization> list = this.findAll(search);
//        if (list != null && list.size() > 0) {
//            result = list.get(0);
//        }
//        return result;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authorization get(String id) {
        return readMapper.get(id);
    }
}
