/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.model
 * @date : 2021-09-03
 * @time : 16:53
 */
package net.lizhaoweb.sys.area.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * [模型] - 拼音API接口返回对象
 * <p>
 * Created by Admin on 2021/9/3 16:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
@Data
public class PinYinAPIResponse {

    @JsonProperty("ReturnCode")
    private String returnCode;

    @JsonProperty("ReturnMessage")
    private String returnMessage;

    @JsonProperty("Data")
    private String data;
}
