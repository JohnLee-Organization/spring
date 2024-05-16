/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.dto
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 20:03
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.config.SSDPConfiguration;
import net.lizhaoweb.ssdp.model.*;
import net.lizhaoweb.ssdp.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <h1>传输模型 [实现] - SSDP 协议通告请求</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Slf4j
@NoArgsConstructor
public class NotifyRequest extends Request {

    // SSDP 配置对象
    @Autowired
    private SSDPConfiguration configuration;

    /**
     * 服务类型。
     */
    @Setter
    @Getter
    private ServiceType serviceType;

    /**
     * SSDP 通知类型。
     */
    @Setter
    @Getter
    private NotificationType notificationType;

    /**
     * 有参构造。
     *
     * @param configuration SSDP 配置对象
     */
    public NotifyRequest(SSDPConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    /**
     * 安装
     */
    public void install() {
        this.setTransportProtocol(TransportProtocol.HTTP_1_1);
        this.setMethod(Method.NOTIFY);
        this.setQueryString("*");
        this.setHeader(HeaderName.HOST, String.format("%s:%d", configuration.getBroadcastAddress(), configuration.getBroadcastPort()))// 设置为协议保留多播地址和端口，必须是239.255.255.250:1900。
                .setHeader(HeaderName.CACHE_CONTROL, String.format("max-age=%d", configuration.getMaxAge()))// max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在
                .setHeader(HeaderName.LOCATION, this.getLocationValue(configuration.getUnicastPort()))// 包含根设备描述得URL地址
                .setHeader(HeaderName.SERVER, this.getServerValue(configuration.getProductName(), configuration.getProductVersion()))
                .setHeader(HeaderName.NT, serviceType.getValue())// 在此消息中，NT头必须为服务的服务类型
                .setHeader(HeaderName.USN, this.getUSNValue())// 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力
                .setHeader(HeaderName.NTS, notificationType.getValue());// 表示通知消息的子类型，必须为ssdp:alive
    }

    private String getLocationValue(int port) {
        return String.format("http://%s:%d/rootDesc.xml", configuration.getHostAddress(), port);
    }

    private String getServerValue(String productName, String productVersion) {
        return String.format("%s %s/%s UPnP/1.0 %s/%s", OS_ARCH, OS_NAME, OS_VERSION, productName, productVersion);
    }

    private String getUSNValue() {
        return String.format("uuid:%s::upnp:rootdevice", Constant.Message.OS.SYSTEM_UUID);
    }
}
