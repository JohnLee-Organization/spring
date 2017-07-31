/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:31
 */
package net.lizhaoweb.spring.mvc.uas.interceptor;

import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.core.interceptor.AbstractInterceptor;
import net.lizhaoweb.spring.mvc.uas.model.Account;
import net.lizhaoweb.spring.mvc.uas.service.SystemConfigFactory;
import net.lizhaoweb.spring.mvc.uas.util.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>拦截器 - 登录</h1>
 * <p>
 * 检查用户是否登录，如果没有例如就跳转到登录页面。
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class LoginInterceptor extends AbstractInterceptor {

    private String loginURL;

    public LoginInterceptor(String loginURL) {
        if (StringUtil.isBlank(loginURL)) {
            throw new IllegalArgumentException("loginURL is null or empty string");
        }
        this.loginURL = loginURL;
    }

    /**
     * 拦截
     *
     * @param request  请求对象
     * @param response 响应对象。
     * @param handler  处理器。
     * @return {Boolean}
     * @throws Exception 异常
     */
    @Override
    protected boolean preMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!SystemConfigFactory.isUASTakeEffect()) {
            return true;
        }
        HttpSession session = request.getSession(true);
        Account account = (Account) session.getAttribute(Constant.Application.System.Config.USER_SESSION_KEY);
        if (account == null) {
            String jumpURLString = request.getRequestURL().toString();
            request.setAttribute(Constant.Application.System.Config.JUMP_URL_KEY, jumpURLString);
            request.getRequestDispatcher(loginURL).forward(request, response);
            return false;
        }
        session.setAttribute(Constant.Application.System.Config.USER_SESSION_KEY, account);
        return true;
    }
}
