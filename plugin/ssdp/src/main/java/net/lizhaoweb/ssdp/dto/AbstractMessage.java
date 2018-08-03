/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.dto
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:57
 */
package net.lizhaoweb.ssdp.dto;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.ssdp.model.EnumHeaderName;
import net.lizhaoweb.ssdp.model.EnumTransportProtocol;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>传输模型 [抽象] - 传输消息</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractMessage implements Serializable {

    /**
     * 传输协议
     */
    @Setter
    @Getter
    private EnumTransportProtocol transportProtocol;

    /**
     * 头
     */
    @Getter
    private Map<String, String> headers;

    /**
     * 体
     */
    @Setter
    @Getter
    private String body;

    protected static final String OS_NAME = System.getProperty("os.name");
    protected static final String OS_ARCH = System.getProperty("os.arch");
    protected static final String OS_VERSION = System.getProperty("os.version");

    /**
     * 有参构造。
     */
    public AbstractMessage() {
        super();
        this.headers = new HashMap<String, String>();
    }

    /**
     * 设置头参数。
     *
     * @param name  头参数的名称。
     * @param value 头参数的值。
     * @param <T>   类型
     * @return T
     */
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
    public <T extends AbstractMessage> T setHeader(EnumHeaderName name, String value) {
        return this.setHeader(name.getValue(), value);
    }
}
