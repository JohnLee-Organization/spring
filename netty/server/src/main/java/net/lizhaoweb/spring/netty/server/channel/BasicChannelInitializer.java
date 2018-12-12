/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.channel
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:13
 */
package net.lizhaoweb.spring.netty.server.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.spring.netty.server.config.BasicConfiguration;
import net.lizhaoweb.spring.netty.server.config.ObjectConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * <h1>通道初始化器 - 基本</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor
public class BasicChannelInitializer<C extends Channel> extends ChannelInitializer<C> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Marker fatal = MarkerFactory.getMarker("FATAL");
    protected Marker constructor = MarkerFactory.getMarker("CONSTRUCTOR");

    @NonNull
    private BasicConfiguration config; // Netty 配置

    /**
     * 初始化通道
     *
     * @param channel 套节子通道
     * @throws Exception 异常
     */
    @Override
    protected void initChannel(C channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast("ping", new IdleStateHandler(config.getReaderIdleTime(), config.getWriterIdleTime(), config.getAllIdleTime(), config.getTimeUnit()));

        this.initChannel0(channel);

        List<Class<ChannelHandler>> channelHandlerList = config.getChannelHandlerClassList();
        if (channelHandlerList == null || channelHandlerList.size() < 1) {
            return;
        }
        fatal.add(constructor);
        for (Class<ChannelHandler> channelHandlerClass : channelHandlerList) {
            ChannelHandler channelHandler;
            try {
                Constructor<ChannelHandler> handlerConstructor = channelHandlerClass.getConstructor(ObjectConfiguration.class);
                if (handlerConstructor != null) {
                    channelHandler = handlerConstructor.newInstance(config);
                    logger.info(fatal, "New instance by constructor{} for '{}'", constructor, channelHandlerClass.getName());
                } else {
                    channelHandler = channelHandlerClass.newInstance();
                    logger.info(fatal, "New instance for '{}'", channelHandlerClass.getName());
                }
            } catch (Exception e) {
                logger.warn(fatal, e.getMessage());
                channelHandler = channelHandlerClass.newInstance();
            }
            channelPipeline.addLast(channelHandler);
        }
    }

    protected void initChannel0(C channel) throws Exception {
    }
}
