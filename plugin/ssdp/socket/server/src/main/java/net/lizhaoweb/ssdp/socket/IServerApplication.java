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

import net.lizhaoweb.ssdp.config.SsdpConfiguration;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;
import net.lizhaoweb.ssdp.service.impl.ResponseMessageConverter;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;

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

    SsdpConfiguration getConfig();

    RequestMessageConverter getRequestMessageConverter();

    ResponseMessageConverter getResponseMessageConverter();

    List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> getHandlerList(SsdpMethod method);

    InetAddress getGroupInetAddress();

    int getGroupPort();
}
