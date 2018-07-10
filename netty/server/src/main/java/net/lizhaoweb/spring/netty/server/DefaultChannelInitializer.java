/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:06
 */
package net.lizhaoweb.spring.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <h1>通道初始化器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月03日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor
public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

    @NonNull
    private NettyConfiguration config;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast("ping", new IdleStateHandler(config.getReaderIdleTime(), config.getWriterIdleTime(), config.getAllIdleTime(), config.getTimeUnit()));

        // 添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
        // 设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问  防止内存溢出
        channel.pipeline().addLast(new ObjectDecoder(config.getMaxObjectSizeForDecoder(), ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));

        // 添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
        channel.pipeline().addLast(new ObjectEncoder());

        List<ChannelHandler> channelHandlerList = config.getChannelHandlerList();
        if (channelHandlerList == null || channelHandlerList.size() < 1) {
            return;
        }
        for (ChannelHandler channelHandler : channelHandlerList) {
            channel.pipeline().addLast(channelHandler);
        }
    }
}
