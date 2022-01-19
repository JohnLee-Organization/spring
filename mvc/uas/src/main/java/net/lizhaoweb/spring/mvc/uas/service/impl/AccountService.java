/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:28
 */
package net.lizhaoweb.spring.mvc.uas.service.impl;

import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.dao.read.IAccountReadMapper;
import net.lizhaoweb.spring.mvc.uas.dao.write.IAccountWriteMapper;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.model.EncryptionAlgorithm;
import net.lizhaoweb.spring.mvc.uas.search.SearchAccount;
import net.lizhaoweb.spring.mvc.uas.service.IAccountService;
import net.lizhaoweb.spring.mvc.uas.service.SystemConfigFactory;
import net.lizhaoweb.spring.mvc.uas.util.Constant;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>业务 [实现] - 账户</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class AccountService extends UASAbstractService implements IAccountService {

    @Autowired
    private IAccountWriteMapper writeMapper;

    @Autowired
    private IAccountReadMapper readMapper;

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
    public DataDeliveryWrapper<Object> save(Account bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Save bean is null");
        }
        String inputPassword = bean.getPassword();
        logger.trace("[Action]{} [LoginName]{} [InputPassword]{}", "Save", bean.getLoginName(), inputPassword);
        String encryptedPassword = this.passwordEncrypted(inputPassword);
        logger.debug("[Action]{} [LoginName]{} [EncryptedPassword]{}", "Save", bean.getLoginName(), inputPassword);
        bean.setPassword(encryptedPassword);
        long id = writeMapper.insert(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "保存账户成功", id > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> update(Account bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Update bean is null");
        }
        String inputPassword = bean.getPassword();
        logger.trace("[Action]{} [LoginName]{} [InputPassword]{}", "Update", bean.getLoginName(), inputPassword);
        String encryptedPassword = this.passwordEncrypted(inputPassword);
        logger.debug("[Action]{} [LoginName]{} [EncryptedPassword]{}", "Update", bean.getLoginName(), inputPassword);
        bean.setPassword(encryptedPassword);
        int count = writeMapper.update(bean);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "更新账户成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Delete id is null");
        }
        int count = writeMapper.delete(id);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "删除账户成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<Object> remove(String ids) {
        if (StringUtil.isBlank(ids)) {
            throw new IllegalArgumentException("Remove ids is null");
        }
        int count = writeMapper.remove(ids);
        return new DataDeliveryWrapper<Object>(StatusCode.SUCCESS, "批量删除账户成功", count > 0);
    }

    @Override
    public DataDeliveryWrapper<PageResult<List<Account>>> pageList(SearchAccount search) {
        if (search == null) {
            throw new IllegalArgumentException("Search model is null");
        }
        if (search.getPage() == null) {
            throw new IllegalArgumentException("Search page is null");
        }
        PageResult<List<Account>> pageResult = new PageResult<List<Account>>(search.getPage());
        List<Account> result = readMapper.findAll(search);
        pageResult.setResult(result);
        return new DataDeliveryWrapper<PageResult<List<Account>>>(StatusCode.SUCCESS, "获取账户列表成功", pageResult);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<Account> findAll(SearchAccount search) {
//        return readMapper.findAll(search);
//    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Account find(SearchAccount search) {
//        Account result = null;
//        List<Account> list = this.findAll(search);
//        if (list != null && list.size() > 0) {
//            result = list.get(0);
//        }
//        return result;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account get(Long id) {
        return readMapper.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataDeliveryWrapper<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response, SearchAccount search) {
        if (search == null) {
            throw new IllegalArgumentException("Search model is null");
        }
        if (StringUtil.isBlank(search.getLoginName())) {
            return new DataDeliveryWrapper<Map<String, Object>>(StatusCode.USER_NAME_PASS_ERROR, "用户名不能为空", (Map<String, Object>) null);
        }
        if (StringUtil.isBlank(search.getPassword())) {
            return new DataDeliveryWrapper<Map<String, Object>>(StatusCode.USER_NAME_PASS_ERROR, "密码不能为空", (Map<String, Object>) null);
        }
        String inputPassword = search.getPassword();
        logger.trace("[Action]{} [LoginName]{} [InputPassword]{}", "Login", search.getLoginName(), inputPassword);
        String encryptedPassword = this.passwordEncrypted(inputPassword);
        logger.debug("[Action]{} [LoginName]{} [EncryptedPassword]{}", "Login", search.getLoginName(), inputPassword);
        search.setPassword(encryptedPassword);
        List<Account> list = readMapper.findAll(search);
        if (list == null || list.size() < 1) {
            return new DataDeliveryWrapper<Map<String, Object>>(StatusCode.USER_NAME_PASS_ERROR, "用户名或密码错误", (Map<String, Object>) null);
        }
        String jumpURL = request.getParameter(Constant.Application.System.Config.JUMP_TO_KEY);
        Account account = list.get(0);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("forward", jumpURL);
        result.put("data", account);
        request.getSession().setAttribute(Constant.Application.System.Config.USER_SESSION_KEY, account);
        return new DataDeliveryWrapper<Map<String, Object>>(StatusCode.SUCCESS, "登录成功", result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataDeliveryWrapper<Account> logout(HttpServletRequest request, HttpServletResponse response) {
        Account account = (Account) request.getSession().getAttribute(Constant.Application.System.Config.USER_SESSION_KEY);
        request.getSession().removeAttribute(Constant.Application.System.Config.USER_SESSION_KEY);
        return new DataDeliveryWrapper<Account>(StatusCode.SUCCESS, "登出成功", account);
    }

    private String passwordEncrypted(String password) {
        if (SystemConfigFactory.getSystemConfig() == null || SystemConfigFactory.getSystemConfig().getPasswordEncryptionAlgorithm() == EncryptionAlgorithm.UNKNOWN) {
            return password;
        }
        byte[] stringUtf8Bytes = StringUtils.getBytesUtf8(password);
        byte[] encryptedBytes = DigestUtils.getDigest(SystemConfigFactory.getSystemConfig().getPasswordEncryptionAlgorithm().getName()).digest(stringUtf8Bytes);
        String encryptedString = Hex.encodeHexString(encryptedBytes);
        return encryptedString;
    }
}
