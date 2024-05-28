/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-20
 * @time : 16:57
 */
package net.lizhaoweb.ssdp.socket.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.ssdp.exception.SsdpException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model._enum.IpVersion;
import org.apache.commons.lang3.StringUtils;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static net.lizhaoweb.ssdp.model._enum.IpVersion.IPV4;
import static net.lizhaoweb.ssdp.model._enum.IpVersion.IPV6;
import static net.lizhaoweb.ssdp.util.Constant.*;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 16:57
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class ClientConfig extends SocketConfig {

    /**
     * 数据包大小
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private InetAddress groupInetAddress;

    /**
     * 数据包大小
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private int groupPort;

    /**
     * 数据包大小
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private int packetSize;

    /**
     * 数据包缓存
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private byte[] packetBuffer;

    @Setter(AccessLevel.NONE)
    private byte supportIpVersion = 0b00000000;

    public ClientConfig(IpVersion ipVersion) {
        if (IPV4 == ipVersion) {
            supportIpVersion = SUPPORT_IP_V4;
            String hostnameIpV4 = DEFAULT_BROADCAST_ADDRESS_IPV4;//TODO hostname IpV4
            if (StringUtils.isNotBlank(this.getBroadcastAddressIpV4())) {
                hostnameIpV4 = this.getBroadcastAddressIpV4();
            }
            boolean isIpV4 = IPAddressUtil.isIPv4LiteralAddress(hostnameIpV4);
            if (!isIpV4) {
                throw new SsdpException(String.format("The address '%s' is not ipv4", hostnameIpV4));
            }
            try {
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress( int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
                this.groupInetAddress = InetAddress.getByName(hostnameIpV4);
            } catch (UnknownHostException e) {
                throw new SsdpUnknownHostException(e);
            }
        } else if (IPV6 == ipVersion) {
            supportIpVersion = SUPPORT_IP_V6;
            String hostnameIpV6 = DEFAULT_BROADCAST_ADDRESS_IPV6;//TODO hostname IpV6
            if (StringUtils.isNotBlank(this.getBroadcastAddressIpV6())) {
                hostnameIpV6 = this.getBroadcastAddressIpV6();
            }
            boolean isIpV6 = IPAddressUtil.isIPv6LiteralAddress(hostnameIpV6);
            if (!isIpV6) {
                throw new SsdpException(String.format("The address '%s' is not ipv6", hostnameIpV6));
            }
            try {
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress( int port);
//                InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
                this.groupInetAddress = InetAddress.getByName(hostnameIpV6);
            } catch (UnknownHostException e) {
                throw new SsdpUnknownHostException(e);
            }
        }
        this.groupPort = 1900;//TODO port
        if (this.getBroadcastPort() > 1024) {
            this.groupPort = this.getBroadcastPort();
        }
        this.packetSize = Math.max(packetSize, 1024 * 64);
        this.packetBuffer = new byte[this.packetSize];
    }

//    public void setIpVersion(IpVersion ipVersion) {
//        if (IPV4 == ipVersion) {
//            supportIpVersion = SUPPORT_IP_V4;
//        } else if (IPV6 == ipVersion) {
//            supportIpVersion = SUPPORT_IP_V6;
//        }
//    }

    public IpVersion getIpVersion() {
        if ((supportIpVersion & SUPPORT_IP_V4) == SUPPORT_IP_V4) {
            return IPV4;
        } else if ((supportIpVersion & SUPPORT_IP_V6) == SUPPORT_IP_V6) {
            return IPV6;
        }
        return null;
    }
}
