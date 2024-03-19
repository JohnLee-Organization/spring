/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 14:41
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * [枚举] SSDP查询目标
 * <p>
 * Created by Jhon.Lee on 3/6/2024 14:41
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public class SsdpSearchTarget {

    /**
     * 服务类型
     */
    @Getter
    @NonNull
    private String value = "ssdp:all";

    /**
     * 所有设备和服务
     *
     * @return SsdpSearchTarget
     */
    public SsdpSearchTarget getAll() {
        this.value = "ssdp:all";
        return this;
    }

    /**
     * 查询UUID标识的设备
     *
     * @return SsdpSearchTarget
     */
    public SsdpSearchTarget getRootDevice() {
        this.value = "upnp:rootdevice";
        return this;
    }

    /**
     * 查询UUID标识的设备
     *
     * @param uuid 设备类型
     * @return SsdpSearchTarget
     */
    public SsdpSearchTarget getUUID(String uuid) {
        // uuid:device-%s
        this.value = String.format("uuid:device-%s", uuid);
        return this;
    }

    /**
     * 查询device-Type字段指定的设备类型，设备类型和版本由UPNP组织定义。
     *
     * @param deviceType 设备类型
     * @param version    设备版本
     * @return SsdpSearchTarget
     */
    public SsdpSearchTarget getDeviceType(String deviceType, String version) {
        // urn:schemas-upnp-org:device:device-Type:version
        this.value = String.format("urn:schemas-upnp-org:device:%s:%s", deviceType, version);
        return this;
    }

    /**
     * 查询service-Type字段指定的服务类型，服务类型和版本由UPNP组织定义。
     *
     * @param serviceType 业务类型
     * @param version     业务版本
     * @return SsdpSearchTarget
     */
    public SsdpSearchTarget getServiceType(String serviceType, String version) {
        // urn:schemas-upnp-org:service:service-Type:version
        this.value = String.format("urn:schemas-upnp-org:device:%s:%s", serviceType, version);
        return this;
    }
}
