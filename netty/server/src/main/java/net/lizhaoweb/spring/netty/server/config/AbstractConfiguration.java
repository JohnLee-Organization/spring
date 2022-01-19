/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:51
 */
package net.lizhaoweb.spring.netty.server.config;

import io.netty.channel.EventLoopGroup;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Netty 配置 - 抽象
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
@RequiredArgsConstructor
public abstract class AbstractConfiguration implements Cloneable, Serializable, Closeable {

    private static final String CONFIG = AbstractConfiguration.class.getName() + "#CONFIG";

    private Map<String, Map<String, Object>> maps = new ConcurrentHashMap<>();

    private EventLoopGroup parentGroup; // 父事件组

    private EventLoopGroup childGroup; // 子事件组

    @NonNull
    private String host;// 监听地址

    @NonNull
    private int port; // 监听端口


    private int socketBackLog = 128; // 套节子...
    private boolean socketKeepalive = true; // 是否保持套节子
    private boolean tcpNoDelay = true; // 是否延迟

    public AbstractConfiguration() {
        maps.put(CONFIG, new ConcurrentHashMap<String, Object>());
    }

    public void set(String namespace, String name, Object value) {
        Map<String, Object> namespaceMap = maps.get(namespace);
        if (namespaceMap == null) {
            maps.put(namespace, new ConcurrentHashMap<String, Object>());
        }
        maps.get(namespace).put(name, value);
    }

    public <T> T get(String namespace, String name, T defaultValue) {
        Map<String, Object> namespaceMap = maps.get(namespace);
        if (namespaceMap == null) {
            return defaultValue;
        }
        return (T) maps.get(namespace).get(name);
    }

    public <T> T remove(String namespace, String name) {
        Map<String, Object> namespaceMap = maps.get(namespace);
        if (namespaceMap == null) {
            return null;
        }
        return (T) maps.get(namespace).remove(name);
    }

    public void setConfig(String name, Object value) {
        set(CONFIG, name, value);
    }

    public Byte getConfig(String name, Byte value) {
        return get(CONFIG, name, value);
    }

    public byte getConfig(String name, byte value) {
        return get(CONFIG, name, value);
    }

    public Short getConfig(String name, Short value) {
        return get(CONFIG, name, value);
    }

    public short getConfig(String name, short value) {
        return get(CONFIG, name, value);
    }

    public Integer getConfig(String name, Integer value) {
        return get(CONFIG, name, value);
    }

    public int getConfig(String name, int value) {
        return get(CONFIG, name, value);
    }

    public Long getConfig(String name, Long value) {
        return get(CONFIG, name, value);
    }

    public long getConfig(String name, long value) {
        return get(CONFIG, name, value);
    }

    public Float getConfig(String name, Float value) {
        return get(CONFIG, name, value);
    }

    public float getConfig(String name, float value) {
        return get(CONFIG, name, value);
    }

    public Double getConfig(String name, Double value) {
        return get(CONFIG, name, value);
    }

    public double getConfig(String name, double value) {
        return get(CONFIG, name, value);
    }

    public Character getConfig(String name, Character value) {
        return get(CONFIG, name, value);
    }

    public char getConfig(String name, char value) {
        return get(CONFIG, name, value);
    }

    public Boolean getConfig(String name, Boolean value) {
        return get(CONFIG, name, value);
    }

    public boolean getConfig(String name, boolean value) {
        return get(CONFIG, name, value);
    }

    public String getConfig(String name, String value) {
        return get(CONFIG, name, value);
    }

    public Date getConfig(String name, Date value) {
        return get(CONFIG, name, value);
    }

    public Pattern getConfig(String name, Pattern value) {
        return get(CONFIG, name, value);
    }

    public <T> T getConfig(String name, T value) {
        return get(CONFIG, name, value);
    }

    public <T> T removeConfig(String name) {
        return remove(CONFIG, name);
    }

    /**
     * 关闭配置中的一些设置
     */
    @Override
    public void close() {
        if (childGroup != null) {
            childGroup.shutdownGracefully();
        }
        if (parentGroup != null) {
            parentGroup.shutdownGracefully();
        }
    }
}
