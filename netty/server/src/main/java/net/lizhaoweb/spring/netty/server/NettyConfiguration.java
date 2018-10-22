/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:05
 */
package net.lizhaoweb.spring.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>模型 - Netty 配置</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月03日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class NettyConfiguration<Ch> implements Cloneable, Serializable, Closeable {

    @Setter(value = AccessLevel.PACKAGE)
    private EventLoopGroup parentGroup; // 父事件组

    @Setter(value = AccessLevel.PACKAGE)
    private EventLoopGroup childGroup; // 子事件组

    private int port; // 监听端口

    // 设置对象序列化最大长度为1M 防止内存溢出
    private int maxObjectSizeForDecoder = 1024 * 1024;

    private long readerIdleTime = 60; // 通道读取闲置时长
    private long writerIdleTime = 60; // 通道写入闲置时长
    private long allIdleTime = 80; // 通道总闲置时长
    private TimeUnit timeUnit = TimeUnit.SECONDS; // 时长单位

    private int socketBackLog = 128; // 套节子...
    private boolean socketKeepalive = true; // 是否保持套节子
    private boolean tcpNoDelay = true; // 是否延迟

    private List<Class<ChannelHandler>> channelHandlerClassList; // 处理器列表

    private IChannelManager<Ch> channelManager;

    /**
     * 关闭配置中的一些设置
     */
    @Override
    public void close() {
        if (childGroup != null) {
            childGroup.shutdownGracefully();
        }
        if (parentGroup != null) {
            parentGroup.shutdownGracefully();
        }
    }
}
