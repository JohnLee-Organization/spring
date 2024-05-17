/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model._enum
 * @date : 2024-03-06
 * @time : 12:31
 */
package net.lizhaoweb.ssdp.model._enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.ssdp.exception.SsdpUnknownProtocolException;

/**
 * [枚举] SSDP传输协议
 * <p>
 * Created by Jhon.Lee on 3/6/2024 12:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public enum SsdpTransportProtocol {

    HTTP_1_0("HTTP", "1.0"), // Http 1.0
    HTTP_1_1("HTTP", "1.1"), // Http 1.1
    HTTP_2_0("HTTP", "2.0")  // Http 2.0
    ;

    /**
     * 协议
     */
    @Getter
    @NonNull
    private final String protocol;

    /**
     * 版本号
     */
    @Getter
    @NonNull
    private final String version;

//    public static SsdpTransportProtocol fromProtocol(String protocol) {
//        for (SsdpTransportProtocol type : values()) {
//            if (type.protocol.equalsIgnoreCase(protocol)) {
//                return type;
//            }
//        }
//        throw new SsdpUnknownProtocolException(protocol, null);
//    }

    public static SsdpTransportProtocol fromProtocol(String protocol, String version) {
        for (SsdpTransportProtocol type : values()) {
            if (type.protocol.equalsIgnoreCase(protocol) && type.version.equalsIgnoreCase(version)) {
                return type;
            }
        }
        throw new SsdpUnknownProtocolException(protocol, version);
    }

    public static SsdpTransportProtocol fromProtocolVersion(String protocolVersion) {
        String[] protocolAndVersion = protocolVersion.split("/");
        return fromProtocol(protocolAndVersion[0], protocolAndVersion[1]);
    }
}
