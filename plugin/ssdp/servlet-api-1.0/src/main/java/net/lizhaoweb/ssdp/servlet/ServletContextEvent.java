/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:30
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to
 * the servlet context of a web application.
 * <p>
 * Created by jhon on 2024/5/30 15:30
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletContextListener
 */
@SuppressWarnings("unused")
public class ServletContextEvent extends EventObject {

    /**
     * Construct a ServletContextEvent from the given context.
     *
     * @param source - the ServletContext that is sending the event.
     */
    public ServletContextEvent(ServletContext source) {
        super(source);
    }

    /**
     * Return the ServletContext that changed.
     *
     * @return the ServletContext that sent the event.
     */
    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }

}
