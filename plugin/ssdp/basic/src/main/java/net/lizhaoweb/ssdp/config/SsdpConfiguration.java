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

import lombok.Data;

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
public class SsdpConfiguration {

//    // SSDP socket 地址
//    private static InetSocketAddress ssdpInetSocketAddress;

    /**
     * 广播的 IP 地址
     */
    private String broadcastAddress = "239.255.255.250";

    /**
     * 广播的端口号
     */
    private int broadcastPort = 1900;

    /**
     * 本地服务监听端口号。
     */
    private int unicastPort;

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
}
