/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 16:07
 */
package net.lizhaoweb.spring.websocket;

import com.caucho.websocket.WebSocketContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>客户端 [接口] - Http 转 WebSocket</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年07月10日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface HttpToWebSocketClient {

    /**
     * 启动 WebSocket
     *
     * @param request  Http 请求
     * @param response Http 响应
     * @return WebSocketContext
     */
    WebSocketContext startWebSocket(HttpServletRequest request, HttpServletResponse response);
}
