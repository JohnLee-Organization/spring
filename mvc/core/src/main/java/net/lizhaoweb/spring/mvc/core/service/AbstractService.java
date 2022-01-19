/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.core.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 5:45
 */
package net.lizhaoweb.spring.mvc.core.service;

import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>业务层[实现] - 抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

//    /**
//     * 页面作用域
//     */
//    @Autowired
//    @Getter
//    private PageContext pageContext;

    /**
     * 请求对象
     */
    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    protected HttpServletRequest request;

    /**
     * 响应对象
     */
    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    protected HttpServletResponse response;

    /**
     * 会话对象
     */
    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    protected HttpSession session;

    /**
     * 应用上下文
     */
    @Autowired
    @Getter(value = AccessLevel.PROTECTED)
    protected ServletContext application;
}
