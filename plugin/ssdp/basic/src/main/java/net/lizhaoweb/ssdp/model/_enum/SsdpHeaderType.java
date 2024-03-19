/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 10:46
 */
package net.lizhaoweb.ssdp.model._enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.ssdp.util.Constant;
import net.lizhaoweb.ssdp.util.SystemUtil;

/**
 * [枚举] SSDP传输头
 * <p>
 * Created by Jhon.Lee on 3/6/2024 10:46
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public enum SsdpHeaderType {

    HOST("Host", "239.255.255.250:1900"), // 协议保留多播地址和端口，必须是 239.255.255.250:1900（IPv4）或FF0x::C(IPv6)。

    CACHE_CONTROL("Cache-Control", "max-age=1810"), // max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在
    S("S", String.format("uuid:%s", Constant.Message.OS.SYSTEM_UUID)), // uuid
    ST("ST", SsdpServiceType.ROOT_DEVICE.getValue()), // 设置服务查询的目标
    USN("USN", null), // 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力
    SERVER("Server", String.format("%s %s/%s UPnP/1.0 JohnLeeDLNA/1.1.0", SystemUtil.OS_ARCH, SystemUtil.OS_NAME, SystemUtil.OS_VERSION)), // 服务器信息
    LOCATION("Location", null), // 包含根设备描述得URL地址

    NT("NT", null), // 在此消息中，NT头必须为服务的服务类型
    NTS("NTS", SsdpNotificationType.ALIVE.getValue()), // 表示通知消息的子类型，必须为ssdp:alive

    MAN("Man", SsdpNotificationType.DISCOVER.getValue()), // 设置协议查询的类型，必须是"ssdp:discover"。
    MX("MX", 5), // 设置设备响应最长等待时间，单位：秒。设备响应在0和这个值之间随机选择响应延迟的值。这样可以为控制点响应平衡网络负载。

    DATE("Date", null), // 指定响应生成的时间
    EXT("EXT", null), // 向控制点确认MAN头域已经被设备理解
    CONTENT_LENGTH("Content-Length", null), // 内容长度
//
//    CONTENT_TYPE("Content-Type"), // 内容类型
//    CONNECTION("Connection") // 连接情况
    ;


    /**
     * 头名称
     */
    @Getter
    @NonNull
    private final String name;

    /**
     * 头值
     */
    @Getter
    private final Object defaultValue;

    public static SsdpHeaderType fromName(String name) {
        for (SsdpHeaderType header : values()) {
            if (header.name.equalsIgnoreCase(name)) {
                return header;
            }
        }
        throw new IllegalArgumentException(String.format("The header name is '%s'", name));
    }
}
