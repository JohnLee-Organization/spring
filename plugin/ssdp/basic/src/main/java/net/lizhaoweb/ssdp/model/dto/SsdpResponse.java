/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 14:02
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [传输模型] SSDP响应
 * <p>
 * Created by Jhon.Lee on 3/6/2024 14:02
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused"})
public class SsdpResponse extends AbstractMessage {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码对应的消息
     */
    private String codeMessage;
}
