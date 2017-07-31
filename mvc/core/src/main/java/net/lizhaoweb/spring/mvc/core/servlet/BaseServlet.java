/**
 * Copyright (c) 2013, 2014, XinZhe and/or its affiliates. All rights reserved.
 * XINZHE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.lizhaoweb.spring.mvc.core.servlet;


import lombok.AccessLevel;
import lombok.Getter;
import net.lizhaoweb.common.util.base.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 基础Servlet类
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2014-10-23<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = -8056395805007597465L;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Getter(value = AccessLevel.PROTECTED)
    private ServletContext application;

    @Getter(value = AccessLevel.PROTECTED)
    private WebApplicationContext applicationContext;

    @Getter(value = AccessLevel.PROTECTED)
    private HttpSession session;

    @Override
    public void init() throws ServletException {
        super.init();
        application = this.getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(application);

        this.initC();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("service('%s', '%s')", request, response);
        request.setCharacterEncoding(Constant.Charset.UTF8);
        response.setCharacterEncoding(Constant.Charset.UTF8);
        response.setContentType("text/html;charset=" + Constant.Charset.UTF8);
        super.service(request, response);
        session = request.getSession();
    }

    protected abstract void initC();
}
