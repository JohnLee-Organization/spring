/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:31
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventListener;

/**
 * Implementations of this interface receive notifications about
 * changes to the servlet context of the web application they are
 * part of.
 * To receive notification events, the implementation class
 * must be configured in the deployment descriptor for the web
 * application.
 * <p>
 * Created by jhon on 2024/5/30 15:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletContextEvent
 */
@SuppressWarnings("unused")
public interface ServletContextListener extends EventListener {

    /**
     * Notification that the web application initialization
     * process is starting.
     * All ServletContextListeners are notified of context
     * initialization before any filter or servlet in the web
     * application is initialized.
     */
    void contextInitialized(ServletContextEvent sce);

    /**
     * Notification that the servlet context is about to be shut down.
     * All servlets and filters have been destroy()ed before any
     * ServletContextListeners are notified of context
     * destruction.
     */
    void contextDestroyed(ServletContextEvent sce);

}
