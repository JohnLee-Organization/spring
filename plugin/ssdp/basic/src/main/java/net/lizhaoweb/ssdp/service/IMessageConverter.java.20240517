/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service
 * @date : 2024-03-06
 * @time : 13:14
 */
package net.lizhaoweb.ssdp.service;

import net.lizhaoweb.ssdp.model.dto.AbstractMessage;

import java.io.UnsupportedEncodingException;

/**
 * [业务层] [接口] SSDP消息转换器
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:14
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
public interface IMessageConverter {

    /**
     * 将消息对象转换成字符串。
     *
     * @param message 消息对象
     * @param <T>     消息对象类型
     * @return String
     */
    <T extends AbstractMessage> String toString(T message);

    /**
     * 将字符串转换成消息对象。
     *
     * @param message 消息字符串
     * @param <T>     消息对象类型
     * @return T
     */
    <T extends AbstractMessage> T toBean(String message);

    /**
     * 转换字节数组。
     *
     * @param message 消息对象
     * @return byte[]
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    <T extends AbstractMessage> byte[] toBytes(T message) throws UnsupportedEncodingException;
}
