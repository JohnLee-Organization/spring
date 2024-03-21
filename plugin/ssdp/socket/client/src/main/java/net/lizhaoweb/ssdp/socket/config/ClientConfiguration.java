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
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;
import net.lizhaoweb.ssdp.service.impl.ResponseMessageConverter;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 16:57
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class ClientConfiguration extends SocketConfiguration {

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

    /**
     * 请求消息转换器
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private RequestMessageConverter requestMessageConverter;

    /**
     * 响应消息转换器
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private ResponseMessageConverter responseMessageConverter;

    public ClientConfiguration() {
        String hostname = "239.255.255.250";//TODO hostname
        if (StringUtils.isNotBlank(this.getBroadcastAddress())) {
            hostname = this.getBroadcastAddress();
        }
        try {
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(int port);
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            this.groupInetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new SsdpUnknownHostException(e);
        }
        this.groupPort = 1900;//TODO port
        if (this.getBroadcastPort() > 1024) {
            this.groupPort = this.getBroadcastPort();
        }
        this.packetSize = Math.max(packetSize, 1024 * 64);
        this.packetBuffer = new byte[this.packetSize];
        this.requestMessageConverter = new RequestMessageConverter(this);
        this.responseMessageConverter = new ResponseMessageConverter(this);
    }
}
