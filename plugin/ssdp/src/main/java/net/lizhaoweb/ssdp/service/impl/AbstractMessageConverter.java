/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 01:18
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.ssdp.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.service.IMessageConverter;
import net.lizhaoweb.ssdp.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <h1>业务层 [抽象] - 消息转换器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMessageConverter implements IMessageConverter {

    /**
     * SSDP 配置对象
     */
    @Autowired
    protected Configuration configuration;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends AbstractMessage> byte[] toBytes(T message) throws UnsupportedEncodingException {
        return this.convert(message).getBytes(net.lizhaoweb.common.util.base.Constant.Charset.UTF8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends AbstractMessage> String convert(T message) {
        if (message == null) {
            throw new IllegalArgumentException("The argument [message] is null");
        }
        StringBuilder builder = new StringBuilder();
        this.convertTop(builder, message);// 转换顶部
        this.convertHeaders(builder, message);// 转换头参数
        this.convertBody(builder, message); // 转换体
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends AbstractMessage> T convert(String message) {
        if (message == null) {
            throw new IllegalArgumentException("The argument [message] is null");
        }
        if (message.trim().length() == 0) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }
        String[] headerAndBodyArray = message.split(Constant.Message.EOF.REGEX_LINE + Constant.Message.EOF.REGEX_LINE);
        if (headerAndBodyArray == null || headerAndBodyArray.length < 1) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }
        String[] headerArray = headerAndBodyArray[0].split(Constant.Message.EOF.REGEX_LINE);
        if (headerArray == null || headerArray.length < 1) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }

        T bean = this.buildBean(message);
        for (String headerString : headerArray) {
            if (StringUtil.isBlank(headerString)) {
                throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
            }
            if (headerString.indexOf(":") > -1) {// 头
                this.convertHeaders(bean, headerString);
            } else {// 顶部
                this.convertTop(bean, headerString);
            }
        }

        // 体
        if (headerAndBodyArray.length > 1) {
            this.convertBody(bean, headerAndBodyArray[1]);// 转换体
        }
        return bean;
    }

    /**
     * 创建一个消息对象。
     *
     * @param message 消息字符串。
     * @param <T>     消息对象类型。
     * @return T
     */
    protected abstract <T extends AbstractMessage> T buildBean(String message);

    /**
     * 转换顶部。
     *
     * @param builder 字符串构建器。
     * @param message 消息对象。
     */
    protected abstract void convertTop(StringBuilder builder, AbstractMessage message);

    /**
     * 转换顶部。
     *
     * @param bean       字符串构建器。
     * @param headerLine 消息对象。
     */
    protected abstract void convertTop(AbstractMessage bean, String headerLine);

    // 转换头参数 Bean -> String
    private void convertHeaders(StringBuilder builder, AbstractMessage message) {
        for (Map.Entry<String, String> entry : message.getHeaders().entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(Constant.Message.EOF.LINE);
        }
        builder.append(Constant.Message.EOF.LINE);
    }

    // 转换头参数 String -> Bean
    private void convertHeaders(AbstractMessage bean, String headerLine) {
        int index = headerLine.indexOf(':');
        String name = headerLine.substring(0, index).trim();
        String value = headerLine.substring(index + 1).trim();
        bean.setHeader(name, value);
    }

    // 转换体 Bean -> String
    private void convertBody(StringBuilder builder, AbstractMessage message) {
        if (StringUtil.isNotBlank(message.getBody())) {
            builder.append(message.getBody()).append(Constant.Message.EOF.LINE);
        }
        builder.append(Constant.Message.EOF.LINE);
    }

    // 转换体 String -> Bean
    private void convertBody(AbstractMessage bean, String body) {
        String temp = body;
        while (temp.endsWith(Constant.Message.EOF.LINE)) {
            int index = temp.lastIndexOf(Constant.Message.EOF.LINE);
            temp = temp.substring(0, index);
        }
        bean.setBody(temp);
    }
}
