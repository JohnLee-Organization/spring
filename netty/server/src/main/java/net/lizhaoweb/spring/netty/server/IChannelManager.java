/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:27
 */
package net.lizhaoweb.spring.netty.server;

import io.netty.channel.Channel;

import java.util.Collection;

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
public interface IChannelManager<Ch> {

    void register(Ch channel);

    Ch getMessageChannel(Channel channel);

    Ch getMessageChannel(String channelId);

    Ch getMessageChannelByBoxMac(String boxMac);

    Ch removeMessageChannel(Channel channel);

    Ch removeMessageChannel(String channelId);

    Ch removeMessageChannelByBoxMac(String boxMac);

    int channelCount();

    Collection<Ch> getMessageChannelCollection();

    void closeAll();
}
