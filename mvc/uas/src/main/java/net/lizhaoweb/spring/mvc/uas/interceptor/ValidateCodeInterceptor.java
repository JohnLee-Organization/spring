/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 20:32
 */
package net.lizhaoweb.spring.mvc.uas.interceptor;

import net.lizhaoweb.common.util.base.HttpUtil;
import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.core.interceptor.AbstractBeforeExecuteMethodInterceptor;
import net.lizhaoweb.spring.mvc.core.servlet.ValidateCodeServlet;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.uas.service.SystemConfigFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>拦截器 - 验证码</h1>
 * <p>
 * 检查用户登陆时的验证码是否输入正确
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ValidateCodeInterceptor extends AbstractBeforeExecuteMethodInterceptor implements AsyncHandlerInterceptor {

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
    protected boolean beforeMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!SystemConfigFactory.isUASTakeEffect()) {
            return true;
        }
        if (SystemConfigFactory.getSystemConfig() != null && !SystemConfigFactory.getSystemConfig().isCheckValidateCode()) {
            return true;
        }
        HttpSession session = request.getSession(true);
        String serverValidateCode = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE_KEY);
        String localValidateCode = request.getParameter("validateCode");

        if (StringUtil.isBlank(serverValidateCode)) {
            HttpUtil.printJson(response, new DataDeliveryWrapper<Object>(StatusCode.VERIFICATION_CODE_NULL, "验证码无效，请刷新验证码", null));
            return false;
        }
        if (!serverValidateCode.equalsIgnoreCase(localValidateCode)) {
            HttpUtil.printJson(response, new DataDeliveryWrapper<Object>(StatusCode.VERIFICATION_CODE_INVALID, "验证码输入错误", null));
            return false;
        }
        return true;
    }
}
