/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.handler
 * @date : 2024-03-14
 * @time : 11:00
 */
package net.lizhaoweb.ssdp.socket.handler;

import net.lizhaoweb.ssdp.model._enum.SsdpMethod;

/**
 * [接口] 服务器处理器
 * <p>
 * Created by Jhon.Lee on 2024/3/14 11:00
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public interface IServiceHandler<CTX, REQ, RES> extends IHandler {

    SsdpMethod getMethod();

    /**
     * 处理请求
     *
     * @param context SSDP请求上下文
     * @param request SSDP请求对象
     * @return SSDP响应对象
     */
    RES handle(CTX context, REQ request);
}
