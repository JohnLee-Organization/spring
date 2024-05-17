/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service
 * @date : 2024-05-17
 * @time : 12:17
 */
package net.lizhaoweb.ssdp.service;

import net.lizhaoweb.ssdp.model.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;

/**
 * [接口] 消息工厂
 * <p>
 * Created by jhon on 2024/5/17 12:17
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public interface IMessageFactory {

    /**
     * 注册消息对象
     *
     * @param message 消息对象
     * @param <T>     T
     */
    <T extends AbstractMessage> void register(T message);

    /**
     * 根据消息字符串转换成请求对象
     *
     * @param message 消息字符串
     * @param <T>
     * @return T
     */
    <T extends SsdpRequest> T toRequest(String message) throws InstantiationException, IllegalAccessException;

    /**
     * 根据消息字符串转换成响应对象
     *
     * @param message 消息字符串
     * @param <T>
     * @return T
     */
    <T extends SsdpResponse> T toResponse(String message);
}
