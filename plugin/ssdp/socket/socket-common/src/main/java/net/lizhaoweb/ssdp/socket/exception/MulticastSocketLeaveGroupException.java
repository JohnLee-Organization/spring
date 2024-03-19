/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.exception
 * @date : 2024-03-11
 * @time : 09:59
 */
package net.lizhaoweb.ssdp.socket.exception;

/**
 * [异常] [SSDP] 组播套节子组异常
 * <p>
 * Created by Jhon.Lee on 3/11/2024 09:59
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
public class MulticastSocketLeaveGroupException extends RuntimeException {

    public MulticastSocketLeaveGroupException() {
        super();
    }

    public MulticastSocketLeaveGroupException(String message) {
        super(message);
    }

    public MulticastSocketLeaveGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public MulticastSocketLeaveGroupException(Throwable cause) {
        super(cause);
    }

    protected MulticastSocketLeaveGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
