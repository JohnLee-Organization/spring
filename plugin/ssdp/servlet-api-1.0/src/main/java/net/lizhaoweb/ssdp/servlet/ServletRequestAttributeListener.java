/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:36
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventListener;

/**
 * A ServletRequestAttributeListener can be implemented by the
 * developer interested in being notified of request attribute
 * changes. Notifications will be generated while the request
 * is within the scope of the web application in which the listener
 * is registered. A request is defined as coming into scope when
 * it is about to enter the first servlet or filter in each web
 * application, as going out of scope when it exits the last servlet
 * or the first filter in the chain.
 * <p>
 * Created by jhon on 2024/5/30 15:36
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public interface ServletRequestAttributeListener extends EventListener {

    /**
     * Notification that a new attribute was added to the
     * * servlet request. Called after the attribute is added.
     */
    void attributeAdded(ServletRequestAttributeEvent srae);

    /**
     * Notification that an existing attribute has been removed from the
     * * servlet request. Called after the attribute is removed.
     */
    void attributeRemoved(ServletRequestAttributeEvent srae);

    /**
     * Notification that an attribute was replaced on the
     * * servlet request. Called after the attribute is replaced.
     */
    void attributeReplaced(ServletRequestAttributeEvent srae);

}
