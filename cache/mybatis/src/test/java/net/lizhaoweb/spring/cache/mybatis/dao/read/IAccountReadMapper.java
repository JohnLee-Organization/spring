/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.read
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:05
 */
package net.lizhaoweb.spring.cache.mybatis.dao.read;

import net.lizhaoweb.spring.cache.mybatis.model.Account;
import net.lizhaoweb.spring.cache.mybatis.search.SearchAccount;

import java.util.List;

/**
 * <h1>持久层 [接口] 读 - 账户</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IAccountReadMapper {

    /**
     * 根据查询条件查询所有记录。
     *
     * @param search 查询条件
     * @return 记录列表
     */
    List<Account> findAll(SearchAccount search);

    /**
     * 权限查询条件查询记录。
     *
     * @param search 查询条件
     * @return 记录
     */
    List<Account> find(SearchAccount search);

    /**
     * 权限主键查询记录。
     *
     * @param id 主键
     * @return 记录
     */
    Account get(Long id);
}
