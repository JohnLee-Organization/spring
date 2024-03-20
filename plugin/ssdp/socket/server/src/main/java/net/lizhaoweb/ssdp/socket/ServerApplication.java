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
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;
import net.lizhaoweb.ssdp.service.impl.ResponseMessageConverter;
import net.lizhaoweb.ssdp.socket.config.ServerSsdpConfiguration;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class ServerApplication implements IServerApplication {

    /**
     * 服务器状态。
     * <p>
     * 0x00：开始实例化；0x01：实例化；0x02：已经实例化；
     * 0x10：准备初始化；0x11：初始化；0x12：已经初始化；
     * 0x20：准备启动；0x21：启动；0x22：已启动；
     * 0x32：运行中；
     * 0x80：准备停止；0x81：停止；0x82：已经停止；
     * 0x90：准备关闭；0x91：关闭；0x92：已经关闭；
     * 0xA0：准备销毁；0xA1：销毁；0xA2：已经销毁；
     */
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private short serverStatus;

    /**
     * SSDP配置对象
     */
    @Setter(AccessLevel.NONE)
    private ServerSsdpConfiguration config;


    /**
     * 组播的端口
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private int groupPort;

    /**
     * 组播的地址
     */
    @Setter(AccessLevel.NONE)
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

    /**
     * 请求消息转换器
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private RequestMessageConverter requestMessageConverter;

    /**
     * 响应消息转换器
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private ResponseMessageConverter responseMessageConverter;

    private Map<SsdpMethod, List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>> handlerMap;

    /**
     * [服务器] 运行上下文
     *
     * @param config SSDP配置对象
     */
    public ServerApplication(ServerSsdpConfiguration config) {
        this.config = config;
        String hostname = "239.255.255.250";//TODO hostname
        if (StringUtils.isNotBlank(this.config.getBroadcastAddress())) {
            hostname = this.config.getBroadcastAddress();
        }
        try {
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(int port);
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            this.groupInetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new SsdpUnknownHostException(e);
        }
        this.groupPort = 1900;//TODO port
        if (this.config.getBroadcastPort() > 1024) {
            this.groupPort = this.config.getBroadcastPort();
        }
        this.packetSize = Math.max(packetSize, 1024 * 64);
        this.packetBuffer = new byte[this.packetSize];
        this.requestMessageConverter = new RequestMessageConverter(config);
        this.responseMessageConverter = new ResponseMessageConverter(config);
        this.handlerMap = new HashMap<>();
    }

    public Map<SsdpMethod, List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>> getHandlerMap() {
        return Collections.unmodifiableMap(this.handlerMap);
    }

    public ServerSsdpConfiguration getConfig() {
        return this.config.clone();
    }

    public ServerApplication registerHandler(IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> handler) {
        if (this.handlerMap.get(handler.getMethod()) == null) {
            this.handlerMap.put(handler.getMethod(), new ArrayList<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>>());
        }
        this.handlerMap.get(handler.getMethod()).add(handler);
        return this;
    }

    public List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> getHandlerList(SsdpMethod method) {
        return Collections.unmodifiableList(this.handlerMap.get(method));
    }

    public void close() {
    }

    public void destroy() {
    }
}
