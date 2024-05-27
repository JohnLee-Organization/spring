/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.config
 * @date : 2024-03-06
 * @time : 10:23
 */
package net.lizhaoweb.ssdp.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Properties;

import static net.lizhaoweb.ssdp.util.Constant.DEFAULT_BROADCAST_ADDRESS_IPV4;
import static net.lizhaoweb.ssdp.util.Constant.DEFAULT_BROADCAST_ADDRESS_IPV6;

/**
 * [模型] [SSDP] 配置
 * <p>
 * Created by Jhon.Lee on 3/6/2024 10:23
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
@Data
public class SsdpConfig {

//    // SSDP socket 地址
//    private static InetSocketAddress ssdpInetSocketAddress;

    /**
     * 是否支持IPV4
     */
    @Setter(AccessLevel.NONE)
    private boolean supportIpV4;

    /**
     * 是否支持IPV6
     */
    @Setter(AccessLevel.NONE)
    private boolean supportIpV6;

    /**
     * 广播的 IP 地址 - IPV4
     */
    private String broadcastAddressIpV4 = DEFAULT_BROADCAST_ADDRESS_IPV4;

    /**
     * 广播的 IP 地址 - IPV6
     */
    private String broadcastAddressIpV6 = DEFAULT_BROADCAST_ADDRESS_IPV6;

    /**
     * 广播的端口号
     */
    private int broadcastPort = 1900;

    /**
     * 本地的 IP 地址 - IPV4
     */
    private String localAddressIpV4 = "0.0.0.0";

    /**
     * 本地的 IP 地址 - IPV6
     */
    private String localAddressIpV6 = "::";

    /**
     * 本地的端口号
     */
    private int localPort = 1900;

//    /**
//     * 单播的 IP 地址
//     */
//    private String unicastAddress;
//
//    /**
//     * 单播的端口号。
//     */
//    private int unicastPort;

    /**
     * SSDP 通知有效时长(单位：秒)。
     */
    private int maxAge;

    /**
     * 产品名称。
     */
    private String productName;

    /**
     * 产品版本号。
     */
    private String productVersion;

    /**
     * 设置设备响应最长等待时间(单位：秒)，设备响应在0和这个值之间随机选择响应延迟的值。
     * <p>
     * 这样可以为控制点响应平衡网络负载。
     */
    private int maxDelayTime = 5;

    /**
     * 设置要获取 IP 地址的网卡名
     */
    private String networkInterfaceName;


    public SsdpConfig() {
        this.supportIpV4 = Boolean.parseBoolean(System.getProperty("java.net.preferIPv4Stack", "true"));
        this.supportIpV6 = Boolean.parseBoolean(System.getProperty("java.net.preferIPv6Addresses", "true"));
    }


    //    /**
//     * 本地地址对象
//     */
//    private InetAddress inetAddress;
//
//    /**
//     * 无参构造
//     */
//    public Configuration() {
//        try {
//            this.inetAddress = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            throw new SsdpUnknownHostException(e);
//        }
//    }
//
//    /**
//     * 获取组播地址。
//     *
//     * @return InetSocketAddress
//     */
//    public synchronized InetSocketAddress getSSDPMulticastSocketAddress() {
//        if (ssdpInetSocketAddress == null) {
//            ssdpInetSocketAddress = new InetSocketAddress(broadcastAddress, broadcastPort);
//        }
//        return ssdpInetSocketAddress;
//    }
//
//    /**
//     * 获取本地 IP 地址
//     *
//     * @return String
//     */
//    public String getHostAddress() {
//        if (OSUtil.isWindows()) {
//            return inetAddress.getHostAddress();
//        } else {
//            SimpleInetAddress simpleInetAddress = IPUtil.getLocalSimpleInetAddress(networkInterfaceName);
//            if (simpleInetAddress != null) {
//                return simpleInetAddress.getIpV4();
//            } else {
//                return null;
//            }
//        }
//    }

    public void convert(Properties properties) {
        this.broadcastAddressIpV4 = properties.getProperty("ssdp.broadcast.address.ipv4", DEFAULT_BROADCAST_ADDRESS_IPV4); // 广播的 IP 地址 - IPV4
        this.broadcastAddressIpV6 = properties.getProperty("ssdp.broadcast.address.ipv6", DEFAULT_BROADCAST_ADDRESS_IPV6); // 广播的 IP 地址 - IPV6
        this.broadcastPort = Integer.parseInt(properties.getProperty("ssdp.broadcast.port", "1900")); // 广播的端口号
        this.localAddressIpV4 = properties.getProperty("ssdp.local.address.ipv4", "0.0.0.0"); // 本地的 IP 地址 - IPV4
        this.localAddressIpV6 = properties.getProperty("ssdp.local.address.ipv6", "::"); // 本地的 IP 地址 - IPV6
        this.localPort = Integer.parseInt(properties.getProperty("ssdp.local.port", "1900")); // 本地的端口号
//        this.unicastAddress = properties.getProperty("ssdp.unicast.address", DEFAULT_BROADCAST_ADDRESS_IPV4); // 单播的 IP 地址
//        this.unicastPort = Integer.parseInt(properties.getProperty("ssdp.unicast.port", "1800")); // 单播的端口号
        this.maxAge = Integer.parseInt(properties.getProperty("ssdp.time.age.max", "5")); // SSDP 通知有效时长(单位：秒)。
        this.productName = properties.getProperty("ssdp.product.name", ""); // 产品名称
        this.productVersion = properties.getProperty("ssdp.product.version", ""); // 产品版本号
        this.maxDelayTime = Integer.parseInt(properties.getProperty("ssdp.time.delay.max", "5")); // 设置设备响应最长等待时间(单位：秒)，设备响应在0和这个值之间随机选择响应延迟的值。这样可以为控制点响应平衡网络负载。
        this.networkInterfaceName = properties.getProperty("ssdp.network.interface.name", "eth0"); // 设置要获取 IP 地址的网卡名}
    }

}
