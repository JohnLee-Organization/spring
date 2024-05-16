/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.listener
 * @date : 2024-05-16
 * @time : 15:24
 */
package net.lizhaoweb.ssdp.socket.listener;

import net.lizhaoweb.ssdp.socket.IServerApplication;
import net.lizhaoweb.ssdp.socket.config.ServerConfiguration;
import net.lizhaoweb.ssdp.socket.model.ServerStatus;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * [接口] 服务器生命周期事件
 * <p>
 * Created by jhon on 2024/5/16 15:24
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public interface IServerLifeEvent extends IServerEvent {

    /**
     * 获取服务右路配置信息
     *
     * @return ServerConfiguration
     */
    ServerConfiguration getServerConfig();

    /**
     * 获取服务器状态
     *
     * @return ServerStatus
     */
    ServerStatus getServerStatus();

    /**
     * 获取服务器应用信息
     *
     * @return IServerApplication
     */
    IServerApplication getServerApplication();

    /**
     * 获取线程池执行器
     *
     * @return ThreadPoolExecutor
     */
    ThreadPoolExecutor getThreadPoolExecutor();
}
