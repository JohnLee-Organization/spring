/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:05
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - SSDP 头名称</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum EnumHeaderName {
    HOST("Host"),// 协议保留多播地址和端口，必须是239.255.255.250:1900。

    CACHE_CONTROL("Cache-Control"),// max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在
    S("S"),// uuid
    ST("ST"),// 设置服务查询的目标
    USN("USN"),// 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力
    SERVER("Server"),// 服务器信息
    LOCATION("Location"),// 包含根设备描述得URL地址

    NT("NT"),// 在此消息中，NT头必须为服务的服务类型
    NTS("NTS"),// 表示通知消息的子类型，必须为ssdp:alive

    MAN("Man"),// 设置协议查询的类型，必须是"ssdp:discover"。
    MX("MX"),// 设置设备响应最长等待时间，设备响应在0和这个值之间随机选择响应延迟的值。这样可以为控制点响应平衡网络负载。

    DATE("Date"),// 指定响应生成的时间
    EXT("EXT"),// 向控制点确认MAN头域已经被设备理解
    CONTENT_LENGTH("Content-Length"),// 内容长度

    CONTENT_TYPE("Content-Type"),// 内容类型
    CONNECTION("Connection");// 连接情况

    /**
     * 头名称
     */
    @Getter
    @NonNull
    private String value;

    public static EnumHeaderName fromName(String headerName) {
        for (EnumHeaderName type : values()) {
            if (type.value.equalsIgnoreCase(headerName)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The header name is '%s'", headerName));
    }
}
