/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.ipv6
 * @date : 2024-05-28
 * @time : 10:03
 */
package net.lizhaoweb.ssdp.socket.ipv6;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.socket.AbstractSsdpSocketClient;
import net.lizhaoweb.ssdp.socket.config.ClientConfig;

import java.net.InetAddress;

/**
 * a
 * <p>
 * Created by jhon on 2024/5/28 10:03
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
@Slf4j
public class SsdpSocketClientForIpV6 extends AbstractSsdpSocketClient {

    public SsdpSocketClientForIpV6(ClientConfig config) {
        super(config);
    }

    @Override
    protected InetAddress getBroadcastInetAddress(ClientConfig config) {
        InetAddress groupInetAddress = null;
        try {
            groupInetAddress = InetAddress.getByName(config.getBroadcastAddressIpV6());
        } catch (Exception e) {
            throw new SsdpUnknownHostException(e);
        }
        return groupInetAddress;
    }

}
