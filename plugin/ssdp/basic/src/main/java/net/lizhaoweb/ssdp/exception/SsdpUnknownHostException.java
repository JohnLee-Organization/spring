/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.exception
 * @date : 2024-03-06
 * @time : 10:21
 */
package net.lizhaoweb.ssdp.exception;

/**
 * [异常] [SSDP] 服务主机不存在
 * <p>
 * Created by Jhon.Lee on 3/6/2024 10:21
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class SsdpUnknownHostException extends RuntimeException {

    public SsdpUnknownHostException() {
        super();
    }

    public SsdpUnknownHostException(String message) {
        super(message);
    }

    public SsdpUnknownHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public SsdpUnknownHostException(Throwable cause) {
        super(cause);
    }

    protected SsdpUnknownHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
