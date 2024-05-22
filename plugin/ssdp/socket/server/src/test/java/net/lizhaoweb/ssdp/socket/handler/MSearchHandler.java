/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.handler
 * @date : 2024-05-22
 * @time : 18:17
 */
package net.lizhaoweb.ssdp.socket.handler;

import net.lizhaoweb.ssdp.model.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.dto.MSearchResponse;
import net.lizhaoweb.ssdp.socket.handler.impl.DefaultMSearchHandler;

/**
 * [模型] 查询
 * <p>
 * Created by jhon on 2024/5/22 18:17
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class MSearchHandler extends DefaultMSearchHandler {

    @Override
    protected MSearchResponse handle(MSearchRequest request) {
        MSearchResponse response = new MSearchResponse();
        response.setBody("[TEST] This is a M-Search demo. ----- Jhon.Lee");
        return response;
    }
}
