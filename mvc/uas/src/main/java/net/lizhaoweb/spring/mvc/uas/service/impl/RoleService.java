/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 18:37
 */
package net.lizhaoweb.spring.mvc.uas.service.impl;

import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.dao.read.IAccountReadMapper;
import net.lizhaoweb.spring.mvc.uas.dao.read.IRoleReadMapper;
import net.lizhaoweb.spring.mvc.uas.dao.write.IRoleWriteMapper;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.model.Role;
import net.lizhaoweb.spring.mvc.uas.search.SearchAccount;
import net.lizhaoweb.spring.mvc.uas.search.SearchRole;
import net.lizhaoweb.spring.mvc.uas.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <h1>业务 [实现] - 角色</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class RoleService extends UASAbstractService implements IRoleService {

    @Autowired
    private IRoleWriteMapper writeMapper;

    @Autowired
    private IRoleReadMapper readMapper;

    @Autowired
    private IAccountReadMapper accountReadMapper;

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
    public DataDeliveryWrapper<Object> save(Role bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Save bean is null");
        }
        long id = writeMapper.insert(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "保存角色成功", id > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> update(Role bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Update bean is null");
        }
        int count = writeMapper.update(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "更新角色成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Delete id is null");
        }
        SearchAccount searchAccount = new SearchAccount();
        searchAccount.setRoleId(id);
        List<Account> accountList = accountReadMapper.findAll(searchAccount);
        if (accountList != null && accountList.size() > 0) {
            return new DataDeliveryWrapper<Object>(StatusCode.WRONG_BUSINESS_TYPE, "有用户关联角色", accountList);
        }
        int count = writeMapper.delete(id);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "删除角色成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> remove(String ids) {
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "角色不允许批量删除", null);
    }

    @Override
    public DataDeliveryWrapper<PageResult<List<Role>>> pageList(SearchRole search) {
        if (search == null) {
            throw new IllegalArgumentException("Search model is null");
        }
        if (search.getPage() == null) {
            throw new IllegalArgumentException("Search page is null");
        }
        PageResult<List<Role>> pageResult = new PageResult<List<Role>>(search.getPage());
        List<Role> result = readMapper.findAll(search);
        pageResult.setResult(result);
        return new DataDeliveryWrapper<PageResult<List<Role>>>(StatusCode.SUCCESS, "获取角色列表成功", pageResult);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<Role> findAll(SearchRole search) {
//        return readMapper.findAll(search);
//    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Role find(SearchRole search) {
//        Role result = null;
//        List<Role> list = this.findAll(search);
//        if (list != null && list.size() > 0) {
//            result = list.get(0);
//        }
//        return result;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role get(Long id) {
        return readMapper.get(id);
    }
}
