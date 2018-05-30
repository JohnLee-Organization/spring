/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.core.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:43
 */
package net.lizhaoweb.spring.mvc.core.interceptor;

import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>拦截器- 基础抽象类（方法执行后拦截）</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年05月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractAfterExecuteMethodInterceptor extends AbstractInterceptor implements AsyncHandlerInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean beforeMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
}
