/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 10:36
 */
package net.lizhaoweb.ssdp.model._enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * [枚举] SSDP方法名称
 * <p>
 * Created by Jhon.Lee on 3/6/2024 10:36
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public enum SsdpMethod {

    M_SEARCH("M-SEARCH"), // 设备查询消息
    GET("GET"), // 获取
    NOTIFY("NOTIFY") // 设备通知消息
    ;


    /**
     * 方法名称
     */
    @Getter
    @NonNull
    private final String name;

    public static SsdpMethod fromName(String name) {
        for (SsdpMethod method : values()) {
            if (method.name.equalsIgnoreCase(name)) {
                return method;
            }
        }
        throw new IllegalArgumentException(String.format("The method name is '%s'", name));
    }
}
