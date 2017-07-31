/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 14:11
 */
package net.lizhaoweb.spring.mvc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

/**
 * <h1>模型 [数据] - 数据传输封装</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DataDeliveryWrapper<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @JsonProperty(value = "code")
    @NonNull
    private int code;

    /**
     * 提示信息
     */
    @JsonProperty(value = "msg")
    @NonNull
    private String message;

    /**
     * 数据。这个约定必须是对象。
     */
    @JsonProperty(value = "result")
    private T data;

    /**
     * 有参数构造
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     */
    public DataDeliveryWrapper(StatusCode code, String message, T data) {
        this.setCode(code.getCode());
        this.setMessage(message);
        this.setData(data);
    }

    /**
     * 有参数构造
     *
     * @param code    状态码
     * @param message 提示信息
     */
    public DataDeliveryWrapper(StatusCode code, String message) {
        this(code, message, null);
    }
}
