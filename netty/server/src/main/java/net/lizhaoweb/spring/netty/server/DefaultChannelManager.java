/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:54
 */
package net.lizhaoweb.spring.netty.server;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <H1>管理器 [接口] - 通道</H1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年10月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class DefaultChannelManager<Ch> implements IChannelManager<Ch> {

    protected Map<String, Ch> channelMap = new ConcurrentHashMap<>();
    protected Map<String, String> macChannelMap = new ConcurrentHashMap<>();

    @Override
    public Ch getMessageChannel(Channel channel) {
        String channelId = channel.id().toString();
        return getMessageChannel(channelId);
    }

    @Override
    public Ch getMessageChannel(String channelId) {
        return channelMap.get(channelId);
    }

    @Override
    public Ch getMessageChannelByMac(String boxMac) {
        String channelId = macChannelMap.get(boxMac);
        return getMessageChannel(channelId);
    }

    @Override
    public Ch removeMessageChannel(Channel channel) {
        String channelId = channel.id().toString();
        return removeMessageChannel(channelId);
    }

    @Override
    public Ch removeMessageChannel(String channelId) {
        return channelMap.remove(channelId);
    }

    @Override
    public Ch removeMessageChannelByMac(String boxMac) {
        String channelId = macChannelMap.remove(boxMac);
        return channelMap.remove(channelId);
    }

    @Override
    public int channelCount() {
        return (channelMap == null) ? -1 : channelMap.size();
    }

    @Override
    public Collection<Ch> getMessageChannelCollection() {
        return channelMap.values();
    }

}
