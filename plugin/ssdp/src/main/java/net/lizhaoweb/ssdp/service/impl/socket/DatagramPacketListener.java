/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 21:35
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.common.util.base.IOUtil;
import net.lizhaoweb.ssdp.dto.Request;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.model.EnumMethod;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;

import java.net.DatagramPacket;
import java.net.SocketAddress;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor
public class DatagramPacketListener {

    /**
     * SSDP 配置对象
     */
    @NonNull
    private Configuration configuration;

    /**
     * 停止
     */
    private boolean stop;

    private void listen() {
        MulticastSocketReceiver receiver = null;
        try {
            receiver = new MulticastSocketReceiver(configuration);
            RequestMessageConverter requestConverter = new RequestMessageConverter(configuration);// 请求转换器
            while (!stop) {
                try {
                    DatagramPacket receiverPacket = receiver.receive();
                    SocketAddress clientSocketAddress = receiverPacket.getSocketAddress();

                    String message = new String(receiverPacket.getData());
                    Request request = requestConverter.convert(message);
                    request.setRemoteSocketAddress(clientSocketAddress);

                    if (EnumMethod.M_SEARCH == request.getMethod()) {
                        ThreadMSearchResponse threadMSearchResponse = new ThreadMSearchResponse(configuration, request);
                        threadMSearchResponse.setDaemon(true);
                        threadMSearchResponse.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(receiver);
        }
    }
}
