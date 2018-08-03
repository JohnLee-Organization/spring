/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 21:11
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.service.ISSDPServer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * <h1>业务层 [实现] - 服务端</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class MulticastSocketSSDPServer implements ISSDPServer {

    /**
     * SSDP 配置对象
     */
    private Configuration configuration;

    /**
     * 发送器
     */
    private MulticastSocketSender sender;

    /**
     * 接收器
     */
    private MulticastSocketReceiver receiver;

    /**
     * 停止
     */
    private boolean stop;

    private void listen() {
        while (!stop) {
            try {
                DatagramPacket receiverPacket = receiver.receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
