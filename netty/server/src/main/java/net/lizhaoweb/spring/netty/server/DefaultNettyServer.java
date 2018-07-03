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
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Configuration config;

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
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new DefaultChannelInitializer(config))
                    .option(ChannelOption.SO_BACKLOG, config.getSocketBackLog())          // (5)
                    .option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
                    .childOption(ChannelOption.SO_KEEPALIVE, config.isSocketKeepalive()); // (6)

            // Bind and start to accept incoming connections.
            channelFuture = serverBootstrap.bind(this.config.getPort()).sync(); // (7)
//            ChannelFuture f = b.bind(host, port).sync(); // (7)
            logger.info("Netty server is run");
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
