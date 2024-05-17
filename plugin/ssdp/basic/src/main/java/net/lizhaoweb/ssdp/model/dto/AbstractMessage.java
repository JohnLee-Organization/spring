/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 13:09
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:09
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"unused"})
public abstract class AbstractMessage implements Serializable, Cloneable {

    /**
     * 传输协议
     */
    private SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.HTTP_1_1;

    /**
     * 头
     */
    @Setter(AccessLevel.NONE)
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * 体
     */
    private String body;

    /**
     * 获取头参数的值
     *
     * @param name 参数名
     * @return 值
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * 获取头参数的值
     *
     * @param name 参数名
     * @return 值
     */
    public String getHeader(SsdpHeaderType name) {
        return this.getHeader(name.getName());
    }

    /**
     * 设置头参数。
     *
     * @param name  头参数的名称。
     * @param value 头参数的值。
     * @param <T>   类型
     * @return T
     */
    @SuppressWarnings({"unchecked"})
    public <T extends AbstractMessage> T setHeader(String name, String value) {
        headers.put(name, value);
        return (T) this;
    }

    /**
     * 设置头参数。
     *
     * @param name  头参数的名称。
     * @param value 头参数的值。
     * @param <T>   类型
     * @return T
     */
    public <T extends AbstractMessage> T setHeader(String name, Object value) {
        return this.setHeader(name, value == null ? null : value.toString());
    }

    /**
     * 设置头参数。
     *
     * @param header 头参数的名称。
     * @param <T>    类型
     * @return T
     */
    public <T extends AbstractMessage> T setHeader(SsdpHeaderType header) {
        return this.setHeader(header, header.getDefaultValue());
    }

    /**
     * 设置头参数。
     *
     * @param name  头参数的名称。
     * @param value 头参数的值。
     * @param <T>   类型
     * @return T
     */
    public <T extends AbstractMessage> T setHeader(SsdpHeaderType name, String value) {
        return this.setHeader(name, (Object) value);
    }

    /**
     * 设置头参数。
     *
     * @param name  头参数的名称。
     * @param value 头参数的值。
     * @param <T>   类型
     * @return T
     */
    public <T extends AbstractMessage> T setHeader(SsdpHeaderType name, Object value) {
        return this.setHeader(name.getName(), value == null ? name.getDefaultValue() : value);
    }

    /**
     * 转换成消息字符串
     *
     * @return String
     */
    public String turnToString() {
        StringBuilder builder = new StringBuilder();
        this.convertFirstLine(builder); // 转换消息第一行
        this.convertHeaders(builder); // 转换消息头信息
        this.convertBody(builder); // 转换消息体
        return builder.toString();
    }

    /**
     * 转换成消息对象
     *
     * @param message 消息字符串
     */
    public void turnToMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("The argument [message] is null");
        }
        if (message.trim().length() == 0) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }
        String[] headerAndBodyArray = message.split(Constant.Message.EOF.REGEX_LINE + Constant.Message.EOF.REGEX_LINE);
        if (headerAndBodyArray.length < 1) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }
        String[] headerArray = headerAndBodyArray[0].split(Constant.Message.EOF.REGEX_LINE);
        if (headerArray.length < 1) {
            throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
        }

//        T bean = this.buildBean(message);
        for (String headerString : headerArray) {
            if (StringUtils.isBlank(headerString)) {
                throw new IllegalArgumentException(String.format("The argument [message] is '%s'", message));
            }
            if (headerString.contains(":")) {// 消息头
                this.convertHeaders(headerString);
            } else {// 第一行
                this.convertFirstLine(headerString);
            }
        }

        // 消息体
        if (headerAndBodyArray.length > 1) {
            this.convertBody(headerAndBodyArray[1]);// 转换体
        }
    }

    @Override
    public AbstractMessage clone() {
        try {
            AbstractMessage clone = (AbstractMessage) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 转换消息第一行。
     * Bean -> String
     *
     * @param builder 字符串构建器。
     */
    protected abstract void convertFirstLine(StringBuilder builder);

    /**
     * 转换消息第一行。
     * String -> Bean
     *
     * @param headerLine 消息对象。
     */
    protected abstract void convertFirstLine(String headerLine);

    // 转换头参数 Bean -> String
    private void convertHeaders(StringBuilder builder) {
        for (Map.Entry<String, String> entry : this.getHeaders().entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(Constant.Message.EOF.LINE);
        }
        builder.append(Constant.Message.EOF.LINE);
    }

    // 转换头参数 String -> Bean
    private void convertHeaders(String headerLine) {
        int index = headerLine.indexOf(':');
        String name = headerLine.substring(0, index).trim();
        String value = headerLine.substring(index + 1).trim();
        this.setHeader(name, value);
    }

    // 转换体 Bean -> String
    private void convertBody(StringBuilder builder) {
        if (StringUtils.isNotBlank(this.getBody())) {
            builder.append(this.getBody()).append(Constant.Message.EOF.LINE);
        }
        builder.append(Constant.Message.EOF.LINE);
    }

    // 转换体 String -> Bean
    private void convertBody(String body) {
        String temp = body;
        while (temp.endsWith(Constant.Message.EOF.LINE)) {
            int index = temp.lastIndexOf(Constant.Message.EOF.LINE);
            temp = temp.substring(0, index);
        }
        this.setBody(temp);
    }
}
