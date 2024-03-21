/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.config
 * @date : 2024-03-11
 * @time : 12:01
 */
package net.lizhaoweb.ssdp.socket.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.socket.IServerContext;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 3/11/2024 12:01
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfiguration extends SocketConfiguration implements Cloneable {

    private List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> handlerList = new ArrayList<>();

    @Override
    public ServerConfiguration clone() {
        try {
            return (ServerConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}