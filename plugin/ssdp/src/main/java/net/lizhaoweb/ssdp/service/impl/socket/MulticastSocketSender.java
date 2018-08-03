/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:43
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import lombok.NoArgsConstructor;
import net.lizhaoweb.ssdp.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.service.ISender;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketAddress;

/**
 * <h1>业务层 [实现] - 接收器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
public class MulticastSocketSender<T extends AbstractMessage> implements ISender<T> {

    /**
     * SSDP 配置对象
     */
    @Autowired
    private Configuration configuration;

    /**
     * 发送套接子。
     */
    private MulticastSocket sendSocket;

    public void init() {
        try {
            sendSocket = new MulticastSocket();
            sendSocket.setBroadcast(true);
            sendSocket.setTimeToLive(1);
            sendSocket.setLoopbackMode(false);//设置本MulticastSocket发送的数据报被回送到自身
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MulticastSocketSender(Configuration configuration) throws IOException {
        this();
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean send(SocketAddress sendSocketAddress, byte[] message) throws IOException {
        DatagramPacket request = new DatagramPacket(message, message.length, sendSocketAddress);
        sendSocket.send(request);
        return false;
    }

    /**
     * 关闭套接子
     *
     * @throws IOException 输入输出异常。
     */
    @Override
    public void close() throws IOException {
        sendSocket.close();
        sendSocket = null;
    }
}
