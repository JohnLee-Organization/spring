/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.websocket.resin
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 19:04
 */
package net.lizhaoweb.spring.websocket.resin;

import com.caucho.websocket.AbstractWebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>监听器 [实现] - WebSocket消息监听</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractResinWebSocketListener extends AbstractWebSocketListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
}
