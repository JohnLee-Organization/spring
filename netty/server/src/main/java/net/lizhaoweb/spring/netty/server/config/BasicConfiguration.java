/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:54
 */
package net.lizhaoweb.spring.netty.server.config;

import io.netty.channel.ChannelHandler;
import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Netty 配置 - 基本
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class BasicConfiguration extends AbstractConfiguration {

    private long readerIdleTime = 60; // 通道读取闲置时长
    private long writerIdleTime = 60; // 通道写入闲置时长
    private long allIdleTime = 80; // 通道总闲置时长
    private TimeUnit timeUnit = TimeUnit.SECONDS; // 时长单位

    private List<Class<ChannelHandler>> channelHandlerClassList; // 处理器列表

    public BasicConfiguration(String host, int port) {
        super(host, port);
    }
}
