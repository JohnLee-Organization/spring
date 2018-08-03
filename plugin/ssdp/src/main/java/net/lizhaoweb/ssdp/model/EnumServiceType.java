/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:10
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - SSDP 服务类型</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumServiceType {
    ROOT_DEVICE("upnp:rootdevice"),// 网络中的根设备
    MEDIA_SERVER("urn:schemas-upnp-org:device:MediaServer:"),// 媒体服务器
    CONTENT_DIRECTORY("urn:schemas-upnp-org:service:ContentDirectory:"),// 内容管理服务
    CONNECTION_MANAGER("urn:schemas-upnp-org:service:ConnectionManager:"),// 连接管理服务
    X_MS_MEDIA_RECEIVER_REGISTRAR("urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:");

    /**
     * 服务类型
     */
    @Getter
    @NonNull
    private String value;

    public static EnumServiceType fromName(String serviceType) {
        for (EnumServiceType type : values()) {
            if (type.value.equalsIgnoreCase(serviceType)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The service type is '%s'", serviceType));
    }
}
