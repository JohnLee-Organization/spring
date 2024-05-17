/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-14
 * @time : 11:12
 */
package net.lizhaoweb.ssdp.socket;

import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import net.lizhaoweb.ssdp.socket.model.ServerStatus;

import java.net.InetAddress;
import java.util.List;

/**
 * [接口] 服务器应用对象
 * <p>
 * Created by Jhon.Lee on 2024/3/14 11:12
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public interface IServerApplication extends IApplication {

//    RequestMessageConverter getRequestMessageConverter();
//
//    ResponseMessageConverter getResponseMessageConverter();

    IMessageFactory getMessageFactory();

    List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> getHandlerList(SsdpMethod method);

    void setGroupInetAddress(InetAddress groupInetAddress);

    InetAddress getGroupInetAddress();

    void setGroupPort(int groupPort);

    int getGroupPort();

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
    void setServerStatus(ServerStatus serverStatus);

    IServerApplication registerHandler(IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> handler);

    byte[] getPacketBuffer();

    int getPacketSize();

    void destroy();

    void close();
}
