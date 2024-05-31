/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-31
 * @time : 13:30
 */
package net.lizhaoweb.ssdp.servlet.http;

/**
 * This is the class representing event notifications for
 * changes to sessions within a web application.
 * <p>
 * Created by jhon on 2024/5/31 13:30
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class HttpSessionEvent extends java.util.EventObject {

    /**
     * Construct a session event from the given source.
     */
    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    /**
     * Return the session that changed.
     */
    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }

}
