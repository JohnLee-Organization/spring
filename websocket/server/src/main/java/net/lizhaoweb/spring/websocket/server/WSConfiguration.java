/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 18:25
 */
package net.lizhaoweb.spring.websocket.server;

import lombok.Data;

import java.io.Serializable;

/**
 * <h1>模型 - WebSocket 配置</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class WSConfiguration implements Cloneable, Serializable {

    private static final int SO_TIMEOUT = 12 * 60 * 60 * 1000; // 12小时
    private static final int CHANNEL_CACHE_SIZE = 1024 * 1024; // 1M

    private int socketTimeout = SO_TIMEOUT;

    private int readCacheSize = CHANNEL_CACHE_SIZE;
}
