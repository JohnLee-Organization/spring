/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:41
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - SSDP 消息类型</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumNotificationType {
    ALIVE("ssdp:alive"),// 服务在线
    DISCOVER("ssdp:discover"),// 发现服务
    BYE_BYE("ssdp:byebye"),// 服务结束
    UPDATE("ssdp:update");// 更新服务

    /**
     * 消息类型值
     */
    @Getter
    @NonNull
    private String value;

    public static EnumNotificationType fromValue(String value) {
        for (EnumNotificationType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The notification type is '%s'", value));
    }
}
