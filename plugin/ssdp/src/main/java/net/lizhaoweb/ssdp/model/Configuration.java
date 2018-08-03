/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 21:02
 */
package net.lizhaoweb.ssdp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.common.util.base.IPUtil;
import net.lizhaoweb.common.util.base.OSUtil;
import net.lizhaoweb.common.util.base.bean.SimpleInetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * <h1>模型 - SSDP 配置</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class Configuration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // SSDP socket 地址
    private static InetSocketAddress ssdpInetSocketAddress;

    /**
     * 广播的 IP 地址
     */
    @Setter
    @Getter
    private String broadcastAddress;

    /**
     * 广播的端口号
     */
    @Setter
    @Getter
    private int broadcastPort;

    /**
     * 本地服务监听端口号。
     */
    @Setter
    @Getter
    private int unicastPort;

    /**
     * SSDP 通知有效时长(单位：秒)。
     */
    @Setter
    @Getter
    private int maxAge;

    /**
     * 产品名称。
     */
    @Setter
    @Getter
    private String productName;

    /**
     * 产品版本号。
     */
    @Setter
    @Getter
    private String productVersion;

    /**
     * 设置设备响应最长等待时间(单位：秒)，设备响应在0和这个值之间随机选择响应延迟的值。
     * <p>
     * 这样可以为控制点响应平衡网络负载。
     */
    @Setter
    @Getter
    private int maxDelayTime;

    /**
     * 设置要获取 IP 地址的网卡名
     */
    @Setter
    @Getter
    private String networkInterfaceName;


    /**
     * 本地地址对象
     */
    private InetAddress inetAddress;

    /**
     * 无参构造
     */
    public Configuration() {
        try {
            this.inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取组播地址。
     *
     * @return InetSocketAddress
     */
    public synchronized InetSocketAddress getSSDPMulticastSocketAddress() {
        if (ssdpInetSocketAddress == null) {
            ssdpInetSocketAddress = new InetSocketAddress(broadcastAddress, broadcastPort);
        }
        return ssdpInetSocketAddress;
    }

    /**
     * 获取本地 IP 地址
     *
     * @return String
     */
    public String getHostAddress() {
        if (OSUtil.isWindows()) {
            return inetAddress.getHostAddress();
        } else {
            SimpleInetAddress simpleInetAddress = IPUtil.getLocalSimpleInetAddress(networkInterfaceName);
            if (simpleInetAddress != null) {
                return simpleInetAddress.getIpV4();
            } else {
                return null;
            }
        }
    }
}
