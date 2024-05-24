/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.ipv6
 * @date : 2024-05-23
 * @time : 13:55
 */
package net.lizhaoweb.ssdp.socket.ipv6;

import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.AbstractSsdpSocketServer;
import net.lizhaoweb.ssdp.socket.IServerApplication;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * SSDP服务器 - IPV6
 * <p>
 * Created by jhon on 2024/5/23 13:55
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class SsdpSocketServerForIpV6 extends AbstractSsdpSocketServer {

    public SsdpSocketServerForIpV6(ServerConfig config, IMessageFactory messageFactory) {
        super(config, messageFactory);
    }

    /**
     * 构建服务器应用对象
     *
     * @param config         服务器配置对象
     * @param messageFactory 消息工厂
     * @return IServerApplication
     */
    @Override
    protected IServerApplication buildApplication(ServerConfig config, IMessageFactory messageFactory) {
        return new ServerApplicationForIpV6(config, messageFactory);
    }

    /**
     * 初始化组播地址
     *
     * @param config      服务器配置对象
     * @param application 服务器应用对象
     */
    @Override
    protected void initGroupInetAddress(ServerConfig config, IServerApplication application) {
        if (config.isSupportIpV6()) {
            String hostnameIpV6 = "FF0x::C";//TODO IPV6 hostname
            if (StringUtils.isNotBlank(config.getBroadcastAddressIpV4())) {
                hostnameIpV6 = config.getBroadcastAddressIpV4();
            }
            try {
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(int port);
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
                Inet6Address groupInetAddressIpV6 = (Inet6Address) InetAddress.getByName(hostnameIpV6);
                application.setGroupInetAddress(groupInetAddressIpV6);
            } catch (UnknownHostException e) {
                throw new SsdpUnknownHostException(e);
            }
        }
    }
}
