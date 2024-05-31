/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-30
 * @time : 16:43
 */
package net.lizhaoweb.ssdp.servlet.http;

import java.util.EventListener;

/**
 * Objects that are bound to a session may listen to container
 * events notifying them that sessions will be passivated and that
 * session will be activated. A container that migrates session between VMs
 * or persists sessions is required to notify all attributes bound to sessions
 * implementing HttpSessionActivationListener.
 * <p>
 * Created by jhon on 2024/5/30 16:43
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public interface HttpSessionActivationListener extends EventListener {

    /**
     * Notification that the session is about to be passivated.
     */
    void sessionWillPassivate(HttpSessionEvent se);

    /**
     * Notification that the session has just been activated.
     */
    void sessionDidActivate(HttpSessionEvent se);

}
