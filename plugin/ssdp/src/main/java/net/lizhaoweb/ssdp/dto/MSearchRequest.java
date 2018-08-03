/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.dto
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 18:03
 */
package net.lizhaoweb.ssdp.dto;

import lombok.*;
import net.lizhaoweb.ssdp.model.*;
import net.lizhaoweb.ssdp.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <h1>传输模型 [实现] - SSDP 协议查询请求</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class MSearchRequest extends Request {

    // SSDP 配置对象
    @NonNull
    @Autowired
    private Configuration configuration;

    /**
     * 查询目标。
     */
    @Setter
    @Getter
    private EnumSearchTarget searchTarget;

    /**
     * 安装
     */
    public void install() {
        this.setTransportProtocol(EnumTransportProtocol.HTTP_1_1);
        this.setMethod(EnumMethod.M_SEARCH);
        this.setQueryString("*");
        this.setHeader(EnumHeaderName.HOST, String.format("%s:%d", configuration.getBroadcastAddress(), configuration.getBroadcastPort()))// 设置为协议保留多播地址和端口，必须是239.255.255.250:1900。
                .setHeader(EnumHeaderName.S, String.format("uuid:%s", Constant.Message.OS.SYSTEM_UUID))
                .setHeader(EnumHeaderName.MAN, this.getManValue())// 设置协议查询的类型，必须是"ssdp:discover"。
                .setHeader(EnumHeaderName.MX, configuration.getMaxDelayTime() + "")// 设置设备响应最长等待时间，设备响应在0和这个值之间随机选择响应延迟的值。这样可以为控制点响应平衡网络负载。
                .setHeader(EnumHeaderName.ST, searchTarget.getValue());// 设置服务查询的目标
//                .setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_IP, configuration.getHostAddress())// 设置服务查询的目标
//                .setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_PORT_WEB, )// 设置服务查询的目标
//                .setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_PORT_NETTY, );// 设置服务查询的目标
    }

    private String getManValue() {
        return String.format("\"%s\"", EnumNotificationType.DISCOVER.getValue());
    }
}
