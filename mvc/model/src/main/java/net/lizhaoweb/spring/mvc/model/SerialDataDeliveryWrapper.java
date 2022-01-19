/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 16:55
 */
package net.lizhaoweb.spring.mvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <h1>模型 [数据] - 带流水的数据传输封装</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年10月31日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class SerialDataDeliveryWrapper<T> extends DataDeliveryWrapper<T> {

    @JsonProperty(value = "req_id")
    private String requestId;

    /**
     * 有参数构造
     *
     * @param requestId 请求标识
     * @param code      状态码
     * @param message   提示信息
     * @param data      数据
     */
    public SerialDataDeliveryWrapper(String requestId, StatusCode code, String message, T data) {
        super(code, data, message);
        this.requestId = requestId;
    }

    /**
     * 有参数构造
     *
     * @param requestId     请求标识
     * @param code          状态码
     * @param messageFormat 提示信息格式
     * @param messageArgs   提示信息参数
     */
    public SerialDataDeliveryWrapper(String requestId, StatusCode code, String messageFormat, Object... messageArgs) {
        super(code, null, messageFormat, messageArgs);
        this.requestId = requestId;
    }
}
