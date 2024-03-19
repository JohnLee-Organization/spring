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

/**
 * [异常] [SSDP] 输入输出异常
 * <p>
 * Created by Jhon.Lee on 3/6/2024 17:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class SsdpIOException extends RuntimeException {

    public SsdpIOException() {
        super();
    }

    public SsdpIOException(String message) {
        super(message);
    }

    public SsdpIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public SsdpIOException(Throwable cause) {
        super(cause);
    }

    protected SsdpIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
