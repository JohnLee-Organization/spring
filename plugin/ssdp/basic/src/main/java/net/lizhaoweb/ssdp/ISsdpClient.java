/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp
 * @date : 2024-03-07
 * @time : 11:39
 */
package net.lizhaoweb.ssdp;

import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;

/**
 * [接口] SSDP客户端
 * <p>
 * Created by Jhon.Lee on 3/7/2024 11:39
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
public interface ISsdpClient extends Cloneable {

    /**
     * 发送消息
     *
     * @param request 请求对象
     * @return 响应对象
     */
    SsdpResponse send(SsdpRequest request);
}
