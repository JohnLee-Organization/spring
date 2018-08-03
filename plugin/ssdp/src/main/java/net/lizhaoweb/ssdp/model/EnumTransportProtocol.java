/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:15
 */
package net.lizhaoweb.ssdp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - 传输协议</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumTransportProtocol {
    HTTP_1_1("HTTP", "1.1");

    /**
     * 协议
     */
    @Getter
    @NonNull
    private String protocol;

    /**
     * 版本号
     */
    @Getter
    @NonNull
    private String version;

    /**
     * @param protocol
     * @return
     */
    public static EnumTransportProtocol fromProtocol(String protocol, String version) {
        for (EnumTransportProtocol type : values()) {
            if (type.protocol.equalsIgnoreCase(protocol) && type.version.equalsIgnoreCase(version)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("protocol/version [%s/%s]", protocol, version));
    }
}
