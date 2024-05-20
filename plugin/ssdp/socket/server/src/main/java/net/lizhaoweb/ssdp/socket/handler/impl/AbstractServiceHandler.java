/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.handler.impl
 * @date : 2024-05-20
 * @time : 09:53
 */
package net.lizhaoweb.ssdp.socket.handler.impl;

import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.socket.IServerContext;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;

/**
 * [实现] 服务器处理器
 * <p>
 * Created by jhon on 2024/5/20 9:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class AbstractServiceHandler<CXT extends IServerContext, REQ extends SsdpRequest, RES extends SsdpResponse> implements IServiceHandler<CXT, REQ, RES> {

    @Override
    public SsdpMethod getMethod() {
        return null;
    }

    @Override
    public RES handle(CXT context, REQ request) {
        return null;
    }
}
