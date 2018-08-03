/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:53
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import lombok.NoArgsConstructor;
import net.lizhaoweb.ssdp.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.service.IReceiver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
public class MulticastSocketReceiver<T extends AbstractMessage> implements IReceiver<T> {

    private final int cacheSize = 1024;

    /**
     * SSDP 配置对象
     */
    @Autowired
    private Configuration configuration;

    /**
     * 接收套接子。
     */
    private MulticastSocket receiveSocket;

    public void init() {
        try {
            InetAddress groupInetAddress = InetAddress.getByName(configuration.getBroadcastAddress());
            receiveSocket = new MulticastSocket(configuration.getBroadcastPort());
            receiveSocket.setReuseAddress(true);
            receiveSocket.joinGroup(groupInetAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MulticastSocketReceiver(Configuration configuration) throws IOException {
        this();
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatagramPacket receive() throws IOException {
        DatagramPacket response = new DatagramPacket(new byte[cacheSize], cacheSize);
        receiveSocket.receive(response);
        return response;
    }

    /**
     * 关闭套接子
     *
     * @throws IOException 输入输出异常。
     */
    @Override
    public void close() {
        receiveSocket.close();
        receiveSocket = null;
    }
}
