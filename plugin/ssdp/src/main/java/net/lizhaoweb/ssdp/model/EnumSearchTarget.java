/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 22:02
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - SSDP 查询目标</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumSearchTarget {
    ALL("ssdp:all"),// 所有设备和服务
    ROOT_DEVICE("upnp:rootdevice"),// 网络中的根设备
    UUID("uuid:device-%s"),// 查询UUID标识的设备
    DEVICE_TYPE("urn:schemas-upnp-org:device:device-Type:version"),// 查询device-Type字段指定的设备类型，设备类型和版本由UPNP组织定义。
    SERVICE_TYPE("urn:schemas-upnp-org:service:service-Type:version");// 查询service-Type字段指定的服务类型，服务类型和版本由UPNP组织定义。

    /**
     * 服务类型
     */
    @Getter
    @NonNull
    private String value;

    public static EnumSearchTarget fromName(String searchTarget) {
        for (EnumSearchTarget type : values()) {
            if (type.value.equalsIgnoreCase(searchTarget)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The search target is '%s'", searchTarget));
    }
}
