/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.ipv6
 * @date : 2024-05-23
 * @time : 13:54
 */
package net.lizhaoweb.ssdp.socket.ipv6;

import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.AbstractServerApplication;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;

/**
 * [服务器] 上下文 - IPV6
 * <p>
 * Created by jhon on 2024/5/23 13:54
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class ServerApplicationForIpV6 extends AbstractServerApplication {

    /**
     * [服务器] 运行上下文
     *
     * @param config         SSDP配置对象
     * @param messageFactory 消息工厂
     */
    public ServerApplicationForIpV6(ServerConfig config, IMessageFactory messageFactory) {
        super(config, messageFactory);
    }
}
