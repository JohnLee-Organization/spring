/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 15:15
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.SsdpSearchTarget;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * [传输模型] SSDP查询请求
 * <p>
 * Created by Jhon.Lee on 3/6/2024 15:15
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MSearchRequest extends SsdpRequest {

    public MSearchRequest() {
        this.setTransportProtocol(SsdpTransportProtocol.HTTP_1_1);
        this.setMethod(SsdpMethod.M_SEARCH);
        this.setQueryString("*");

        String[] hostArray = SsdpHeaderType.HOST.getDefaultValue().toString().split(":");
        String targetHostname = hostArray[0];
        int targetPort = Integer.parseInt(hostArray[1]);
        SocketAddress targetSocketAddress = new InetSocketAddress(targetHostname, targetPort);
        this.setTargetSocketAddress(targetSocketAddress);

        this.setHeader(SsdpHeaderType.HOST) // 设置为协议保留多播地址和端口，必须是239.255.255.250:1900。
                .setHeader(SsdpHeaderType.MAN) // 设置协议查询的类型，必须是"ssdp:discover"。
                .setHeader(SsdpHeaderType.MX) // 设置设备响应最长等待时间，单位：秒。默认：5。设备响应在0和这个值之间随机选择响应延迟的值。这样可以为控制点响应平衡网络负载。
                .setHeader(SsdpHeaderType.ST); // 设置服务查询的目标。默认：upnp:rootdevice。
    }

    /**
     * 设置服务查询的目标
     *
     * @param searchTarget 服务查询的目标
     * @return MSearchRequest
     */
    public MSearchRequest setSearchTarget(SsdpSearchTarget searchTarget) {
        return this.setHeader(SsdpHeaderType.ST, searchTarget.getValue());
    }

    /**
     * 设置设备响应最长等待时间，单位：秒。
     *
     * @param maxDelayTime 设备响应最长等待时间
     * @return MSearchRequest
     */
    public MSearchRequest setMaxDelayTime(int maxDelayTime) {
        return this.setHeader(SsdpHeaderType.MX, maxDelayTime);
    }

    /**
     * 设置目标地址和目标端口
     *
     * @param targetHostname 目标地址
     * @param targetPort     目标端口
     * @return MSearchRequest
     */
    public MSearchRequest setHost(String targetHostname, int targetPort) {
        SocketAddress targetSocketAddress = new InetSocketAddress(targetHostname, targetPort);
        this.setTargetSocketAddress(targetSocketAddress);
        return this.setHeader(SsdpHeaderType.HOST, String.format("%s:%d", targetHostname, targetPort));
    }
}
