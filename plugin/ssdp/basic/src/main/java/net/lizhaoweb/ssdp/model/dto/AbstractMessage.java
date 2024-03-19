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
}
