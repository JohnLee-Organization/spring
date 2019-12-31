/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.channel
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:23
 */
package net.lizhaoweb.spring.netty.server.channel;

import io.netty.channel.Channel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import net.lizhaoweb.spring.netty.server.config.ObjectConfiguration;

/**
 * <h1>通道初始化器 - 对象</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ObjectChannelInitializer<C extends Channel> extends BasicChannelInitializer<C> {

    private ObjectConfiguration config; // Netty 配置

    public ObjectChannelInitializer(ObjectConfiguration config) {
        super(config);
        this.config = config;
    }

    @Override
    protected void initChannel0(C channel) throws Exception {
        channel.pipeline()
                // 添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
                // 设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问  防止内存溢出
                .addLast(new ObjectDecoder(config.getMaxObjectSizeForDecoder(), ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                .addLast(new ObjectEncoder());// 添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
    }
}
