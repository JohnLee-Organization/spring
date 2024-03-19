/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 14:47
 */
package net.lizhaoweb.ssdp.model._enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * [枚举] SSDP消息类型
 * <p>
 * Created by Jhon.Lee on 3/6/2024 14:47
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public enum SsdpNotificationType {

    ALIVE("ssdp:alive"), // 服务在线
    DISCOVER("\"ssdp:discover\""), // 发现服务
    BYE_BYE("ssdp:byebye"), // 服务结束
    UPDATE("ssdp:update") // 更新服务
    ;

    /**
     * 消息类型值
     */
    @Getter
    @NonNull
    private final String value;

    public static SsdpNotificationType fromValue(String value) {
        for (SsdpNotificationType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The notification type is '%s'", value));
    }
}
