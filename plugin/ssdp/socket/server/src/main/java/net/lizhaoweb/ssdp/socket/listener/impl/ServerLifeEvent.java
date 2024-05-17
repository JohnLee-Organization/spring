/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.listener.impl
 * @date : 2024-05-16
 * @time : 14:59
 */
package net.lizhaoweb.ssdp.socket.listener.impl;

import lombok.Getter;
import net.lizhaoweb.ssdp.socket.IServerApplication;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import net.lizhaoweb.ssdp.socket.listener.IServerLifeEvent;
import net.lizhaoweb.ssdp.socket.model.ServerStatus;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 服务器生命周期事件
 * <p>
 * Created by jhon on 2024/5/16 14:59
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class ServerLifeEvent implements IServerLifeEvent {

    @Getter
    private ServerConfig config;
    @Getter
    private ServerStatus status;

    @Getter
    private IServerApplication application;

    @Getter
    private ThreadPoolExecutor threadPoolExecutor;

    public ServerLifeEvent(ServerConfig serverConfig, ServerStatus serverStatus) {
        this.config = serverConfig;
        this.status = serverStatus;
    }

//    public ServerLifeEvent(ServerConfiguration serverConfig, ServerStatus serverStatus, IServerApplication application) {
//        this(serverConfig, serverStatus);
//        this.application = application;
//    }
//
//    public ServerLifeEvent(ServerConfiguration serverConfig, ServerStatus serverStatus, IServerApplication application, ThreadPoolExecutor threadPool) {
//        this(serverConfig, serverStatus, application);
//        this.threadPool = threadPool;
//    }

    public ServerLifeEvent(ServerStatus serverStatus, IServerApplication application) {
        this.status = serverStatus;
        this.application = application;
    }

    public ServerLifeEvent(ServerStatus serverStatus, IServerApplication application, ThreadPoolExecutor threadPoolExecutor) {
        this(serverStatus, application);
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public ServerConfig getServerConfig() {
        return this.config;
    }

    @Override
    public ServerStatus getServerStatus() {
        return this.status;
    }

    @Override
    public IServerApplication getServerApplication() {
        return this.application;
    }
}
