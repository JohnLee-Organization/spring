/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.controller
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:17
 */
package net.lizhaoweb.spring.mvc.uas.controller;

import lombok.Setter;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.search.SearchAccount;
import net.lizhaoweb.spring.mvc.uas.service.IAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <h1>控制层 - 用户操作</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequestMapping("/usa/user-activity")
public class UserActivityController extends UASAbstractController {

    @Setter
    private IAccountService accountService;

    @RequestMapping(value = "/to-login", method = {RequestMethod.GET})
    public String showLoginPage() {
        String viewPath = String.format(VIEW_PATH_FORMAT, this.getViewPrefix(), "login");
        return viewPath;
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response, SearchAccount search) {
        DataDeliveryWrapper<Map<String, Object>> result = null;
        try {
            result = accountService.login(request, response, search);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Map<String, Object>>(StatusCode.SERVER_INTERNAL_EXCEPTION, "用户登录异常", (Map<String, Object>) null);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public DataDeliveryWrapper<Account> logout(HttpServletRequest request, HttpServletResponse response) {
        DataDeliveryWrapper<Account> result = null;
        try {
            result = accountService.logout(request, response);
        } catch (Exception e) {
            result = new DataDeliveryWrapper<Account>(StatusCode.SERVER_INTERNAL_EXCEPTION, "用户登出异常", (Account) null);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected String getViewPrefix() {
        return "/user-activity";
    }
}
