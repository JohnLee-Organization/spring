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

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;
import lombok.Setter;
import net.lizhaoweb.spring.websocket.HttpToWebSocketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractResinWebSoceckService implements HttpToWebSocketService {

    private static final String HEADER_SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    @Setter
    private WebSocketListener listener;

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.setHeader(request, response);
            WebSocketServletRequest wsRequest = (WebSocketServletRequest) request;
            wsRequest.startWebSocket(listener);
        } catch (Exception e) {
            throw new WebSocketException(e);
        }
    }

    @Override
    public void setHeader(HttpServletRequest request, HttpServletResponse response) {
        //当调用了下面的startWebSocket函数后，该socket就会和相应的listener建立起对应关系
        String secWebSocketProtocol = request.getHeader(HEADER_SEC_WEB_SOCKET_PROTOCOL);
        response.setHeader(HEADER_SEC_WEB_SOCKET_PROTOCOL, secWebSocketProtocol);
    }
}
