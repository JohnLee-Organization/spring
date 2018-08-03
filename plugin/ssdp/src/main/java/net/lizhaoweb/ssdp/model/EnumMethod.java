/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:19
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - SSDP 方法名称</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumMethod {
    M_SEARCH("M-SEARCH"),
    GET("GET"),
    NOTIFY("NOTIFY");

    /**
     * 方法名称
     */
    @Getter
    @NonNull
    private String name;

    public static EnumMethod fromName(String methodName) {
        for (EnumMethod type : values()) {
            if (type.name.equalsIgnoreCase(methodName)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("The method name is '%s'", methodName));
    }
}
