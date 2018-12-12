/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:51
 */
package net.lizhaoweb.spring.netty.server.config;

import io.netty.channel.EventLoopGroup;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.Serializable;

/**
 * Netty 配置 - 抽象
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
@RequiredArgsConstructor
public abstract class AbstractConfiguration implements Cloneable, Serializable, Closeable {

    private EventLoopGroup parentGroup; // 父事件组

    private EventLoopGroup childGroup; // 子事件组

    @NonNull
    private String host;// 监听地址

    @NonNull
    private int port; // 监听端口


    private int socketBackLog = 128; // 套节子...
    private boolean socketKeepalive = true; // 是否保持套节子
    private boolean tcpNoDelay = true; // 是否延迟

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
