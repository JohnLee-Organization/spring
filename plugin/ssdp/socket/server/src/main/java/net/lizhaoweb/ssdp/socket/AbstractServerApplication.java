/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-11
 * @time : 11:20
 */
package net.lizhaoweb.ssdp.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import net.lizhaoweb.ssdp.socket.model.ServerStatus;

import java.net.InetAddress;
import java.util.*;

/**
 * [服务器] 上下文
 * <p>
 * Created by Jhon.Lee on 3/11/2024 11:20
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
public abstract class AbstractServerApplication implements IServerApplication {

    /**
     * 服务器状态。
     * <p>
     * PRE_INSTANCE：开始实例化；INSTANCING：实例化；INSTANCED：已经实例化；
     * PRE_INITIALIZE：准备初始化；INITIALIZING：初始化；INITIALIZED：已经初始化；
     * PRE_START：准备启动；STARTING：启动；STARTED：已启动；
     * PRE_RUN：准备运行, RUNNING：运行中；
     * PRE_STOP：准备停止；STOPPING：停止；STOPPED：已经停止；
     * PRE_CLOSE：准备关闭；CLOSING：关闭；CLOSED：已经关闭；
     * PRE_DESTROY：准备销毁；DESTROYING：销毁；DESTROYED：已经销毁；
     */
    @Setter
    @Getter
    private ServerStatus serverStatus;

//    /**
//     * SSDP配置对象
//     */
//    @Setter(AccessLevel.NONE)
//    private ServerConfiguration config;


    /**
     * 组播的端口
     */
    @Setter
    @Getter
    private int groupPort;

    /**
     * 组播的地址
     */
    @Setter
    @Getter
    private InetAddress groupInetAddress;

    /**
     * 数据包大小
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private int packetSize;

    /**
     * 数据包缓存
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private byte[] packetBuffer;

//    /**
//     * 请求消息转换器
//     */
//    @Setter(AccessLevel.NONE)
//    @Getter
//    private RequestMessageConverter requestMessageConverter;
//
//    /**
//     * 响应消息转换器
//     */
//    @Setter(AccessLevel.NONE)
//    @Getter
//    private ResponseMessageConverter responseMessageConverter;

    /**
     * 消息工厂
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private IMessageFactory messageFactory;

    private Map<SsdpMethod, List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>> handlerMap;

    /**
     * [服务器] 运行上下文
     *
     * @param config         SSDP配置对象
     * @param messageFactory 消息工厂
     */
    public AbstractServerApplication(ServerConfig config, IMessageFactory messageFactory) {
        this.packetSize = Math.max(packetSize, 1024 * 64);
        this.packetBuffer = new byte[this.packetSize];
//        this.requestMessageConverter = new RequestMessageConverter(config);
//        this.responseMessageConverter = new ResponseMessageConverter(config);
        this.messageFactory = messageFactory;
        this.handlerMap = new HashMap<>();
    }

    public Map<SsdpMethod, List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>> getHandlerMap() {
        return Collections.unmodifiableMap(this.handlerMap);
    }

//    public ServerConfiguration getConfig() {
//        return this.config.clone();
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractServerApplication registerHandler(IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> handler) {
        if (this.handlerMap.get(handler.getMethod()) == null) {
            this.handlerMap.put(handler.getMethod(), new ArrayList<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>());
        }
        this.handlerMap.get(handler.getMethod()).add(handler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> getHandlerList(SsdpMethod method, List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> defaultValue) {
        List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> handlerList = this.handlerMap.get(method);
        if (handlerList == null) {
            return defaultValue;
        }
        return Collections.unmodifiableList(handlerList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
}
