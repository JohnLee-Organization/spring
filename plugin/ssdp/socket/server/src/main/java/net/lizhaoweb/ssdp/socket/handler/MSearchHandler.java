/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.service
 * @date : 2024-03-12
 * @time : 10:03
 */
package net.lizhaoweb.ssdp.socket.handler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model.dto.MSearchResponse;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.socket.IServerContext;

import static net.lizhaoweb.ssdp.model._enum.SsdpMethod.M_SEARCH;

/**
 * []
 * <p>
 * Created by Jhon.Lee on 3/12/2024 10:03
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
@Slf4j
@NoArgsConstructor
public class MSearchHandler implements IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> {

    @Override
    public SsdpMethod getMethod() {
        return M_SEARCH;
    }

    @Override
    public SsdpResponse handle(IServerContext context, SsdpRequest request) {
        if (M_SEARCH != request.getMethod()) {
            return null;
        }

        return this.handleResponse();
    }

    SsdpResponse handleResponse() {
        return new MSearchResponse();
    }
}
