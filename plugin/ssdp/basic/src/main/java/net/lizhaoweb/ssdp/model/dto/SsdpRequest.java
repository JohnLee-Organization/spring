/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 13:54
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;

import java.net.SocketAddress;

/**
 * [传输模型] SSDP请求
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:54
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused"})
public abstract class SsdpRequest extends AbstractMessage {

    /**
     * 目标地址.
     */
    private SocketAddress targetSocketAddress;

    /**
     * SSDP 方法
     */
    private SsdpMethod method;

    /**
     * 请求路径
     */
    private String queryString;
}
