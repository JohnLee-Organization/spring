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

import net.lizhaoweb.ssdp.exception.SsdpException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.AbstractSsdpSocketServer;
import net.lizhaoweb.ssdp.socket.IServerApplication;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import org.apache.commons.lang3.StringUtils;
import sun.net.util.IPAddressUtil;

import java.net.Inet6Address;
import java.net.UnknownHostException;

import static net.lizhaoweb.ssdp.util.Constant.DEFAULT_BROADCAST_ADDRESS_IPV6;

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
        if (!config.isSupportIpV6()) {
            return;
        }
        // 根据互联网地址指派机构的指派，SSDP在IPv6环境下使用多播地址FF0x::C，这里的X根据scope的不同可以有不同的取值。
        String hostnameIpV6 = DEFAULT_BROADCAST_ADDRESS_IPV6;//TODO IPV6 hostname
        if (StringUtils.isNotBlank(config.getBroadcastAddressIpV6())) {
            hostnameIpV6 = config.getBroadcastAddressIpV6();
        }
        try {
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress( int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            boolean isIpV6 = IPAddressUtil.isIPv6LiteralAddress(hostnameIpV6);
            if (!isIpV6) {
                throw new SsdpException(String.format("The address '%s' is not ipv6", hostnameIpV6));
            }
            Inet6Address groupInetAddressIpV6 = (Inet6Address) Inet6Address.getByName(hostnameIpV6);
            application.setGroupInetAddress(groupInetAddressIpV6);
        } catch (UnknownHostException e) {
            throw new SsdpUnknownHostException(e);
        }
    }
}
