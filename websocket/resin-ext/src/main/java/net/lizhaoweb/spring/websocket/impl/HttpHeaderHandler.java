/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 10:18
 */
package net.lizhaoweb.spring.websocket.impl;

import net.lizhaoweb.spring.websocket.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>处理器 [接口] - 设置 Http 的请求头或响应头</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年07月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class HttpHeaderHandler implements HttpHandler {

    private static final String HEADER_SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        //当调用了下面的startWebSocket函数后，该socket就会和相应的listener建立起对应关系
        String secWebSocketProtocol = request.getHeader(HEADER_SEC_WEB_SOCKET_PROTOCOL);
        response.setHeader(HEADER_SEC_WEB_SOCKET_PROTOCOL, secWebSocketProtocol);
    }
}
