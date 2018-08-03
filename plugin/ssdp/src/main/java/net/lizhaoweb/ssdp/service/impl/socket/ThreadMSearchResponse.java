/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 21:31
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.common.util.base.IOUtil;
import net.lizhaoweb.ssdp.dto.MSearchResponse;
import net.lizhaoweb.ssdp.dto.Request;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.service.impl.ResponseMessageConverter;

import java.io.IOException;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor
public class ThreadMSearchResponse extends Thread {

    /**
     * SSDP 配置对象
     */
    @NonNull
    private Configuration configuration;

    /**
     * 客户端地址
     */
    @NonNull
    private Request request;

    @Override
    public void run() {
        MulticastSocketSender sender = null;
        try {
            MSearchResponse response = new MSearchResponse(configuration);
            ResponseMessageConverter responseConverter = new ResponseMessageConverter(configuration);// 响应转换器
            String responseString = responseConverter.convert(response);
            sender = new MulticastSocketSender(configuration);
            sender.send(request.getRemoteSocketAddress(), responseString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(sender);
        }
    }
}
