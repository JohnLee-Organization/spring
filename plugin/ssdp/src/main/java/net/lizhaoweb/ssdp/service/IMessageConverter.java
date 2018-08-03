/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 01:11
 */
package net.lizhaoweb.ssdp.service;

import net.lizhaoweb.ssdp.dto.AbstractMessage;

import java.io.UnsupportedEncodingException;

/**
 * <h1>业务层 [接口] - 消息转换器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IMessageConverter {

    /**
     * 将消息对象转换成字符串。
     *
     * @param message 消息对象
     * @param <T>     消息对象类型
     * @return String
     */
    <T extends AbstractMessage> String convert(T message);

    /**
     * 将字符串转换成消息对象。
     *
     * @param message 消息字符串
     * @param <T>     消息对象类型
     * @return T
     */
    <T extends AbstractMessage> T convert(String message);

    /**
     * 转换字节数组。
     *
     * @param message 消息对象
     * @return byte[]
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    <T extends AbstractMessage> byte[] toBytes(T message) throws UnsupportedEncodingException;
}
