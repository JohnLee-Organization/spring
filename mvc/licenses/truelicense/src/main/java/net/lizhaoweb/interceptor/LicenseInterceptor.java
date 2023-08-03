/*
 * Copyright (c) 2023, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.lic.truelicense
 * @date : 2023-08-03
 * @time : 15:58
 */
package net.lizhaoweb.interceptor;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.lic.truelicense.c.LicenseVerify;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器校验license证书
 * <p>
 * Created by Jhon.Lee on 8/3/2023 3:58 PM
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
@Component
public class LicenseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseVerify licenseVerify = new LicenseVerify();

        //校验证书是否有效
        boolean verifyResult = licenseVerify.verify();

        if (verifyResult) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            JSONObject obj = new JSONObject();
            obj.put("errcode", "0319");
            obj.put("errmsg", "您的证书无效，请核查服务器是否取得授权或重新申请证书！");
            response.getWriter().print(obj);
            response.getWriter().flush();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
