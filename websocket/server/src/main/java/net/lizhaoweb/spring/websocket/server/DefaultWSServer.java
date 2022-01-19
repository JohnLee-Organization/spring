/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:35
 */
package net.lizhaoweb.spring.websocket.server;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>服务器 [实现] - WebSocket</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年07月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class DefaultWSServer implements IWSServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @NonNull
    private WSConfiguration config;

    @Override
    public void init() {

    }

    @Override
    public void run() {

    }

    @Override
    public void destroy() {

    }
}
