/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : netty
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:00
 */
package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class NettyTcpMulticastServer {
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

//                            ProtobufDecoder protobufDecoder = new ProtobufDecoder(ChatRequest.Command.getDefaultInstance());
//
//                            //Decoder
//                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(2 * 1024 * 1024, 0, 4, 0, 4));
//                            ch.pipeline().addLast("protobufDecoder", protobufDecoder);
//
//                            //Encoder
//                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
//                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
//
//
                            //handler
                            ch.pipeline().addLast(new SocketHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .localAddress("239.255.255.250", 1900)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //Bind and start to accept incoming connections
            ChannelFuture cf = bs.bind(8080).sync();

            cf.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
