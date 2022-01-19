/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:28
 */
package net.lizhaoweb.spring.mvc.uas.service;

import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.search.SearchAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <h1>业务 [接口] - 账户</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IAccountService extends ISqlService<Long, Account, SearchAccount> {

    DataDeliveryWrapper<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response, SearchAccount search);

    DataDeliveryWrapper<Account> logout(HttpServletRequest request, HttpServletResponse response);
}
