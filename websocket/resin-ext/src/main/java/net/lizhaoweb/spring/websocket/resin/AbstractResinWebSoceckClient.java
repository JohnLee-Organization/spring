/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket.resin
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:57
 */
package net.lizhaoweb.spring.websocket.resin;

import com.caucho.websocket.WebSocketContext;
import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;
import lombok.Setter;
import net.lizhaoweb.spring.websocket.HttpHandler;
import net.lizhaoweb.spring.websocket.HttpToWebSocketClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h1>客户端 [抽象] - Http 转 WebSocket</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractResinWebSoceckClient implements HttpToWebSocketClient {

    /**
     * WebSocket 监听器
     */
    @Setter
    private WebSocketListener listener;

    /**
     * 请求头或响应头处理器
     */
    @Setter
    private HttpHandler headerHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSocketContext startWebSocket(HttpServletRequest request, HttpServletResponse response) {
        try {
            headerHandler.handle(request, response);
            WebSocketServletRequest wsRequest = (WebSocketServletRequest) request;
            return wsRequest.startWebSocket(listener);
        } catch (Exception e) {
            throw new WebSocketException(e);
        }
    }
}
