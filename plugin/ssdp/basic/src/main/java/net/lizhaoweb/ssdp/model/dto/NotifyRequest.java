/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-19
 * @time : 17:53
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;

import static org.apache.commons.lang3.SystemUtils.*;

/**
 * []
 * <p>
 * Created by Jhon.Lee on 2024/3/19 17:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class NotifyRequest extends SsdpRequest {

    public NotifyRequest() {
        this.setTransportProtocol(SsdpTransportProtocol.HTTP_1_1);
        this.setMethod(SsdpMethod.NOTIFY);
        this.setQueryString("*");
        this.setHeader(SsdpHeaderType.HOST)// 设置为协议保留多播地址和端口，必须是239.255.255.250:1900。
                .setHeader(SsdpHeaderType.CACHE_CONTROL)// max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在
                .setHeader(SsdpHeaderType.LOCATION)// 包含根设备描述得URL地址
                .setHeader(SsdpHeaderType.SERVER) //
                .setHeader(SsdpHeaderType.NT)// 在此消息中，NT头必须为服务的服务类型
                .setHeader(SsdpHeaderType.USN)// 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力
                .setHeader(SsdpHeaderType.NTS);// 表示通知消息的子类型，必须为ssdp:alive
    }

    private String getLocationValue(int port) {
//        return String.format("http://%s:%d/rootDesc.xml", configuration.getHostAddress(), port);
        return null;
    }

    private String getServerValue(String productName, String productVersion) {
        return String.format("%s %s/%s UPnP/1.0 %s/%s", OS_ARCH, OS_NAME, OS_VERSION, productName, productVersion);
    }

    private String getUSNValue() {
        return String.format("uuid:%s::upnp:rootdevice", Constant.Message.OS.SYSTEM_UUID);
    }

}
