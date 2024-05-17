/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.handler
 * @date : 2024-05-17
 * @time : 17:10
 */
package net.lizhaoweb.ssdp.socket.handler;

import net.lizhaoweb.ssdp.model.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.dto.MSearchResponse;
import net.lizhaoweb.ssdp.socket.handler.impl.AbstractMSearchHandler;

/**
 * [模型] 查询处理器
 * <p>
 * Created by jhon on 2024/5/17 17:10
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class MSearchHandler extends AbstractMSearchHandler {

    @Override
    protected MSearchResponse handle(MSearchRequest request) {
        MSearchResponse response = new MSearchResponse();
        response.setBody("");
        return response;
    }
}
