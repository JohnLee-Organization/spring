/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-31
 * @time : 13:31
 */
package net.lizhaoweb.ssdp.servlet.http;

import java.util.EventListener;

/**
 * Implementations of this interface are notified of changes to the
 * list of active sessions in a web application.
 * To receive notification events, the implementation class
 * must be configured in the deployment descriptor for the web application.
 * <p>
 * Created by jhon on 2024/5/31 13:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see HttpSessionEvent
 */
@SuppressWarnings("unused")
public interface HttpSessionListener extends EventListener {


    /**
     * Notification that a session was created.
     *
     * @param se the notification event
     */
    void sessionCreated(HttpSessionEvent se);

    /**
     * Notification that a session is about to be invalidated.
     *
     * @param se the notification event
     */
    void sessionDestroyed(HttpSessionEvent se);

}
