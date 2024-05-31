/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:39
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.EventListener;

/**
 * A ServletRequestListener can be implemented by the developer
 * interested in being notified of requests coming in and out of
 * scope in a web component. A request is defined as coming into
 * scope when it is about to enter the first servlet or filter
 * in each web application, as going out of scope when it exits
 * the last servlet or the first filter in the chain.
 * <p>
 * Created by jhon on 2024/5/30 15:39
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public interface ServletRequestListener extends EventListener {

    /**
     * The request is about to go out of scope of the web application.
     */
    void requestDestroyed(ServletRequestEvent sre);

    /**
     * The request is about to come into scope of the web application.
     */
    void requestInitialized(ServletRequestEvent sre);

}
