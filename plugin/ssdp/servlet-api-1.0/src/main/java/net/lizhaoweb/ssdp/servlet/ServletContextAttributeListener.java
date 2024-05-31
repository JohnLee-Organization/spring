/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:26
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventListener;

/**
 * Implementations of this interface receive notifications of
 * * changes to the attribute list on the servlet context of a web application.
 * To receive notification events, the implementation class
 * must be configured in the deployment descriptor for the web application.
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletContextAttributeEvent
 * <p>
 * Created by jhon on 2024/5/30 15:26
 */
@SuppressWarnings("unused")
public interface ServletContextAttributeListener extends EventListener {

    /**
     * Notification that a new attribute was added to the servlet context. Called after the attribute is added.
     */
    void attributeAdded(ServletContextAttributeEvent scab);

    /**
     * Notification that an existing attribute has been removed from the servlet context. Called after the attribute is removed.
     */
    void attributeRemoved(ServletContextAttributeEvent scab);

    /**
     * Notification that an attribute on the servlet context has been replaced. Called after the attribute is replaced.
     */
    void attributeReplaced(ServletContextAttributeEvent scab);

}
