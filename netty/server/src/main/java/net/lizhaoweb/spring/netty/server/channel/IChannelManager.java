/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.channel
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:10
 */
package net.lizhaoweb.spring.netty.server.channel;

import io.netty.channel.Channel;

import java.util.Collection;

/**
 * <H1>管理器 [接口] - 通道</H1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IChannelManager<Ch> {

    /**
     * 注册自定义通道
     *
     * @param channel 自定义通道
     */
    void register(Ch channel);

    /**
     * 获取自定义通道，通过Netty通道对象
     *
     * @param channel Netty通道对象
     * @return 自定义通道
     */
    Ch getMessageChannel(Channel channel);

    /**
     * 获取自定义通道，通过通道ID
     *
     * @param channelId 通道ID
     * @return 自定义通道
     */
    Ch getMessageChannel(String channelId);

    /**
     * 获取自定义通道，通过MAC地址
     *
     * @param mac MAC地址
     * @return 自定义通道
     */
    Ch getMessageChannelByMac(String mac);

    /**
     * 移除自定义通道，通过Netty通道对象
     *
     * @param channel Netty通道对象
     * @return 自定义通道
     */
    Ch removeMessageChannel(Channel channel);

    /**
     * 移除自定义通道，通过通道ID
     *
     * @param channelId 通道ID
     * @return 自定义通道
     */
    Ch removeMessageChannel(String channelId);

    /**
     * 移除自定义通道，通过MAC地址
     *
     * @param mac MAC地址
     * @return 自定义通道
     */
    Ch removeMessageChannelByMac(String mac);

    /**
     * 获取通道数量
     *
     * @return int
     */
    int channelCount();

    /**
     * 获取所有自定义通道
     *
     * @return Collection
     */
    Collection<Ch> getMessageChannelCollection();

    /**
     * 关闭所有通道
     */
    void closeAll();
}
