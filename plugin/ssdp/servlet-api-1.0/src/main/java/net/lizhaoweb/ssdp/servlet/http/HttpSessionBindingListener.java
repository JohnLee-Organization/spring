/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-31
 * @time : 13:29
 */
package net.lizhaoweb.ssdp.servlet.http;

import java.util.EventListener;

/**
 * Causes an object to be notified when it is bound to
 * or unbound from a session. The object is notified
 * by an {@link HttpSessionBindingEvent} object. This may be as a result
 * of a servlet programmer explicitly unbinding an attribute from a session,
 * due to a session being invalidated, or due to a session timing out.
 * <p>
 * Created by jhon on 2024/5/31 13:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see HttpSession
 * @see HttpSessionBindingEvent
 */
@SuppressWarnings("unused")
public interface HttpSessionBindingListener extends EventListener {


    /**
     * Notifies the object that it is being bound to
     * a session and identifies the session.
     *
     * @param event the event that identifies the
     *              session
     * @see #valueUnbound
     */
    void valueBound(HttpSessionBindingEvent event);


    /**
     * Notifies the object that it is being unbound
     * from a session and identifies the session.
     *
     * @param event the event that identifies
     *              the session
     * @see #valueBound
     */
    void valueUnbound(HttpSessionBindingEvent event);

}
