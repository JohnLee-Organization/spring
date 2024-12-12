/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 14:59
 */
package net.lizhaoweb.mikefox.net;

import lombok.Getter;
import lombok.Setter;

import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * a
 * <p>
 * Created by jhon on 2024/6/6 14:59
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class SocketWrapper<S> {

    @Getter
    protected volatile S socket;

    @Getter
    protected volatile DatagramPacket datagramPacket;

    @Getter
    protected volatile long lastAccess = -1;
    @Setter
    @Getter
    protected volatile boolean currentAccess = false;
    @Setter
    @Getter
    protected long timeout = -1;
    @Setter
    @Getter
    protected boolean error = false;
    protected long lastRegistered = 0;
    @Setter
    @Getter
    protected volatile int keepAliveLeft = 100;
    @Setter
    @Getter
    protected boolean async = false;
    @Setter
    @Getter
    protected boolean keptAlive = false;
    @Setter
    @Getter
    protected boolean initialized = false;
    public AtomicBoolean processing = new AtomicBoolean(false);

    public SocketWrapper(DatagramPacket datagramPacket, S socket) {
        this.datagramPacket = datagramPacket;
        this.socket = socket;
    }

    public void access() {
        access(System.currentTimeMillis());
    }

    public void access(long access) {
        lastAccess = access;
    }

    public int decrementKeepAlive() {
        return (--keepAliveLeft);
    }

}
