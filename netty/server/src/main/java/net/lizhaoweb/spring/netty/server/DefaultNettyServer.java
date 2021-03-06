/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:04
 */
package net.lizhaoweb.spring.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.lizhaoweb.spring.netty.server.config.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <h1>服务器 [实现] - Netty</h1>
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
public class DefaultNettyServer implements INettyServer {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @NonNull
    private AbstractConfiguration config;

    @NonNull
    private Map<String, Object> configMap;

    @Setter
    private LoggingHandler loggingHandler;

    @Setter
    private ChannelInitializer<?> channelInitializer;

    private ChannelFuture channelFuture;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        try {
            logger.info("Initializing netty server ...");
            this.config.setParentGroup(new NioEventLoopGroup());// (1)
            this.config.setChildGroup(new NioEventLoopGroup());
            if (configMap != null && configMap.size() > 0) {
                for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                    if (entry == null) {
                        continue;
                    }
                    this.config.setConfig(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("Init netty server", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            logger.info("Running netty server ...");
            ServerBootstrap serverBootstrap = new ServerBootstrap(); // (2)
            serverBootstrap.group(config.getParentGroup(), config.getChildGroup())
                    .channel(NioServerSocketChannel.class); // (3)
//                    .handler(new LoggingHandler(LogLevel.INFO))
            if (loggingHandler != null) {
                serverBootstrap.handler(loggingHandler);
            }
//                    .childHandler(new DefaultChannelInitializer(config))
            serverBootstrap.childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, config.getSocketBackLog())          // (5)
                    .option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
                    .childOption(ChannelOption.SO_KEEPALIVE, config.isSocketKeepalive()); // (6)

            // Bind and start to accept incoming connections.
            if (this.config.getHost() == null) {
                channelFuture = serverBootstrap.bind(this.config.getPort()); // (7)
            } else {
                channelFuture = serverBootstrap.bind(this.config.getHost(), this.config.getPort()); // (7)
            }
//            channelFuture = serverBootstrap.bind(this.config.getPort()).sync(); // (7)
            logger.info("Netty server is running");
        } catch (Exception e) {
            logger.error("Netty server is running", e);
//            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        try {

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            channelFuture.channel().closeFuture().sync();

            config.close();
            logger.info("Netty server is closed");
        } catch (Exception e) {
            logger.error("Netty server is closed", e);
        }
    }
}
