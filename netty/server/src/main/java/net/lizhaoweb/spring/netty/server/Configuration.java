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
public class Configuration implements Cloneable, Serializable, Closeable {

    @Setter(value = AccessLevel.PACKAGE)
    private EventLoopGroup parentGroup;

    @Setter(value = AccessLevel.PACKAGE)
    private EventLoopGroup childGroup;

    private int port;

    // 设置对象序列化最大长度为1M 防止内存溢出
    private int maxObjectSizeForDecoder = 1024 * 1024;

    private long readerIdleTime = 60;
    private long writerIdleTime = 60;
    private long allIdleTime = 80;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private int socketBackLog = 128;
    private boolean socketKeepalive = true;
    private boolean tcpNoDelay = true;

    private List<ChannelHandler> channelHandlerList;

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
