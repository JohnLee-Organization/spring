/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 12:37
 */
package net.lizhaoweb.ssdp.model._enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * [枚举] SSDP服务类型
 * <p>
 * Created by Jhon.Lee on 3/6/2024 12:37
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public enum SsdpServiceType {

    ROOT_DEVICE("upnp:rootdevice"), // 网络中的根设备
    MEDIA_SERVER("urn:schemas-upnp-org:device:MediaServer:"), // 媒体服务器
    CONTENT_DIRECTORY("urn:schemas-upnp-org:service:ContentDirectory:"), // 内容管理服务
    CONNECTION_MANAGER("urn:schemas-upnp-org:service:ConnectionManager:"), // 连接管理服务
    X_MS_MEDIA_RECEIVER_REGISTRAR("urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:") //
    ;

    /**
     * 服务类型
     */
    @Getter
    @NonNull
    private final String value;

    public static SsdpServiceType from(String value) {
        for (SsdpServiceType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The service type is '%s'", value));
    }
}
