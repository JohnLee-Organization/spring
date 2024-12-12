/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-06
 * @time : 17:16
 */
package net.lizhaoweb.coyote;

import net.lizhaoweb.mikefox.net.SocketStatus;

/**
 * Adapter. This represents the entry point in a coyote-based servlet container.
 * <p>
 * Created by jhon on 2024/6/6 17:16
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ProtocolHandler
 */
public interface Adapter {

    /**
     * Call the service method, and notify all listeners
     *
     * @throws Exception if an error happens during handling of
     *                   the request. Common errors are:
     *                    <ul><li>IOException if an input/output error occurs and we are
     *                    processing an included servlet (otherwise it is swallowed and
     *                    handled by the top level error handler mechanism)
     *                        <li>ServletException if a servlet throws an exception and
     *                   we are processing an included servlet (otherwise it is swallowed
     *                   and handled by the top level error handler mechanism)
     *                   </ul>
     *                   Tomcat should be able to handle and log any other exception ( including
     *                   runtime exceptions )
     */
    void service(Request req, Response res) throws Exception;

    boolean event(Request req, Response res, SocketStatus status) throws Exception;

    boolean asyncDispatch(Request req, Response res, SocketStatus status) throws Exception;

    void log(Request req, Response res, long time);

}
