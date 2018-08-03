/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.dto
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:51
 */
package net.lizhaoweb.ssdp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <h1>传输模型 [抽象] - SSDP 协议响应</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class Response extends AbstractMessage {

    /**
     * 状态码
     */
    @Setter
    @Getter
    private int statusCode;

    /**
     * 消息
     */
    @Setter
    @Getter
    private String message;
}
