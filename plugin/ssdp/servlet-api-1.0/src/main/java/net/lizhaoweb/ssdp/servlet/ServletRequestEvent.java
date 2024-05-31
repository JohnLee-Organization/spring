/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:37
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventObject;

/**
 * Events of this kind indicate lifecycle
 * events for a ServletRequest.
 * The source of the event
 * is the ServletContext of this web application.
 * <p>
 * Created by jhon on 2024/5/30 15:37
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletRequestListener
 */
@SuppressWarnings("unused")
public class ServletRequestEvent extends EventObject {

    private ServletRequest request;

    /**
     * Construct a ServletRequestEvent for the given ServletContext
     * and ServletRequest.
     *
     * @param sc      the ServletContext of the web application.
     * @param request the ServletRequest that is sending the event.
     */
    public ServletRequestEvent(ServletContext sc, ServletRequest request) {
        super(sc);
        this.request = request;
    }

    /**
     * Returns the ServletRequest that is changing.
     */
    public ServletRequest getServletRequest() {
        return this.request;
    }

    /**
     * Returns the ServletContext of this web application.
     */
    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }

}
