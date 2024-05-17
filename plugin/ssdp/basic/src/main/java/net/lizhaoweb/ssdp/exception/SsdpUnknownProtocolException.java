/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.exception
 * @date : 2024-03-06
 * @time : 17:44
 */
package net.lizhaoweb.ssdp.exception;

import lombok.Getter;

/**
 * [异常] [SSDP] 未知消息类型异常
 * <p>
 * Created by Jhon.Lee on 3/6/2024 17:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class SsdpUnknownProtocolException extends SsdpException {

    /**
     * 协议
     */
    @Getter
    private String protocol;

    /**
     * 版本号
     */
    @Getter
    private String version;


    public SsdpUnknownProtocolException(String protocol, String version) {
        this(String.format("The protocol [%s/%s] is unknown.", protocol, version));
        this.protocol = protocol;
        this.version = version;
    }

    private SsdpUnknownProtocolException(String protocol, String version, Throwable cause) {
        this(String.format("The protocol [%s/%s] is unknown.", protocol, version), cause);
        this.protocol = protocol;
        this.version = version;
    }

    private SsdpUnknownProtocolException() {
        super();
    }

    private SsdpUnknownProtocolException(String message) {
        super(message);
    }

    private SsdpUnknownProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    private SsdpUnknownProtocolException(Throwable cause) {
        super(cause);
    }

    private SsdpUnknownProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
