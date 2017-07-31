/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : AbstractListener.java
 * @Package : net.lizhaoweb.spring.mvc.core.listener
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月26日
 * @Time : 下午11:00:00
 */
package net.lizhaoweb.spring.mvc.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * <h1>监听器 - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractListener implements ApplicationListener<ApplicationEvent> {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(AbstractListener.class);

    /**
     * 执行应用事件。
     *
     * @param event 应用事件
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        this.before(event);
        this.onApplicationLoad(event);
        Object object = event.getSource();
        if (object != null && object instanceof WebApplicationContext) {
            WebApplicationContext webApplicationContext = (WebApplicationContext) object;
            this.onApplicationLoad(webApplicationContext);
            ServletContext servletContext = webApplicationContext.getServletContext();
            this.onApplicationLoad(servletContext);
        }
        this.after(event);
    }

    protected void before(ApplicationEvent event) {
    }

    protected void after(ApplicationEvent event) {
    }

    /**
     * 通过事件加载。
     *
     * @param event 应用事件对象。
     */
    protected abstract void onApplicationLoad(ApplicationEvent event);

    /**
     * WEB应用上下文加载。
     *
     * @param context WEB应用上下文对象。
     */
    protected abstract void onApplicationLoad(WebApplicationContext context);

    /**
     * 通过应用上下文加载 。
     *
     * @param context 应用上下文对象。
     */
    protected abstract void onApplicationLoad(ServletContext context);
}
