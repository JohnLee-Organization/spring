/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-30
 * @time : 16:44
 */
package net.lizhaoweb.ssdp.servlet.http;

import java.util.EventListener;

/**
 * This listener interface can be implemented in order to
 * get notifications of changes to the attribute lists of sessions within
 * this web application.
 * <p>
 * Created by jhon on 2024/5/30 16:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public interface HttpSessionAttributeListener extends EventListener {

    /**
     * Notification that an attribute has been added to a session. Called after the attribute is added.
     */
    void attributeAdded(HttpSessionBindingEvent se);

    /**
     * Notification that an attribute has been removed from a session. Called after the attribute is removed.
     */
    void attributeRemoved(HttpSessionBindingEvent se);

    /**
     * Notification that an attribute has been replaced in a session. Called after the attribute is replaced.
     */
    void attributeReplaced(HttpSessionBindingEvent se);

}
